package com.laboratory.paper.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.laboratory.paper.domain.paper.PaperListItem;
import com.laboratory.paper.domain.paper.PaperResponse;
import com.laboratory.paper.domain.paper.PaperResponseData;
import com.laboratory.paper.domain.paper.PaperUploadRequest;
import com.laboratory.paper.entity.Paper;
import com.laboratory.paper.entity.PaperFolder;
import com.laboratory.paper.entity.RecycleBin;
import com.laboratory.paper.mapper.PaperFolderMapper;
import com.laboratory.paper.mapper.PaperMapper;
import com.laboratory.paper.mapper.RecycleBinMapper;
import com.laboratory.paper.service.PaperService;
import com.laboratory.paper.service.ex.NoLoginException;
import com.laboratory.paper.service.ex.PaperExistsInRecycleBinException;
import com.laboratory.paper.service.ex.PapersIsEmptyException;
import com.laboratory.paper.utils.DeepSeekApiUtil;
import com.laboratory.paper.utils.FileUtils;
import com.laboratory.paper.utils.PdfBoxTextExtractorUtils;
import com.laboratory.paper.utils.QwenApiUtil;
import com.laboratory.paper.vo.paper.QueryPaperBaseInfoVo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IPaperServiceImpl implements PaperService {

    @Value("${file.upload-path}")
    private String fileUploadPath;

    @Value("${deepseek.api.type}")
    private String type;

    @Value("${deepseek.api.url}")
    private String url;

    @Value("${deepseek.api.key}")
    private String key;

    @Value("${deepseek.api.model}")
    private String model;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Resource
    private PaperMapper paperMapper;

    @Resource
    private PaperFolderMapper paperFolderMapper;

    @Resource
    private RecycleBinMapper recycleBinMapper;

    /**
     * Spring加载完成后调用一次，完成deepseek的初始化
     * @throws IOException 异常
     */
    @PostConstruct
    public void initDeepSeekUtil() throws IOException {
        // 初始化静态工具类
        switch (type) {
            case "qwen":
                QwenApiUtil.init(url, key, model);
                break;
            case "deepseek":
            default:
                DeepSeekApiUtil.init(url, key, model);
        }

    }

    @Override
    public List<PaperListItem> searchPaperList(Long folderId, String keyword) {
        if (folderId == null || folderId == 0) {
            return paperMapper.searchAllPapers(keyword);
        } else {
            return paperMapper.searchPaperByFolder(folderId, keyword);
        }

    }

    @Override
    public PaperResponse queryBaseInfo(QueryPaperBaseInfoVo queryPaperBaseInfo) {
        List<Paper> papers;
        Integer totalPage = 1;
        if (queryPaperBaseInfo.getPaperId() == null || queryPaperBaseInfo.getPaperId() == 0) {
            papers = paperMapper.queryAllPapersBaseInfo(queryPaperBaseInfo);
            totalPage = paperMapper.queryAllPapersBaseInfoPages(queryPaperBaseInfo);
        } else {
            papers = paperMapper.queryPapersById(queryPaperBaseInfo);
        }

        PaperResponse paperResponse = new PaperResponse();

        // 遍历数据添加
        for (Paper paper : papers) {
            paperResponse.getData().add(new PaperResponseData(paper.getId(), paper.getTitle(), paper.getAuthor(), paper.getPublishTime(), paper.getJournal(), paper.getJournalType(), paper.getResearchDirection(), paper.getInnovation(), paper.getDeficiency(), paper.getKeywords(), paper.getCreateTime()));
        }

        // 设置总页数
        paperResponse.setTotalPage(totalPage);

        return paperResponse;
    }

    @Override
    public void deletePaper(Long id, Long parentId, Long userId) {
        // 判断回收站中是否存在该文件
        List<RecycleBin> recycleBinsList = recycleBinMapper.queryRecycleBinByIdFolderId(id, parentId);

        if (recycleBinsList != null && !recycleBinsList.isEmpty()) {
            throw new PaperExistsInRecycleBinException();
        }

        // 从目录移除
        paperFolderMapper.deleteByPaperIdFolderId(id, parentId);

        // 移动到回收站
        recycleBinMapper.removeToRecycleBin(id, parentId, userId);
    }

    @Override
    public void upload(PaperUploadRequest paperUploadRequest, MultipartFile file, Long userId) {
        try {
            // 文件hash
            String hash = paperUploadRequest.getIdentifier().split("_")[0];

            // 判断当前文件是否已经存在
            Integer existNum = paperMapper.fileExists(hash);

            if (existNum != null && existNum > 0) {
                // 设置论文id
                paperUploadRequest.setPaperId(existNum.longValue());
                // 不进行上传，直接写数据库
                record2Database(true, paperUploadRequest, userId);
                return;
            }

            // 当前日期
            String currentDate = LocalDate.now().format(DATE_FORMATTER);

            // 构建上传临时路径：fileUploadPath/temp/20251011/identifier
            Path rootPath = Paths.get(fileUploadPath);
            Path tempIdentifierPath = FileUtils.buildFilePath(rootPath, "temp", currentDate, paperUploadRequest.getIdentifier());

            // 最终文件路径：fileUploadPath/files/20251011/identifier
            Path finalIdentifierPath = FileUtils.buildFilePath(rootPath, "files", currentDate, paperUploadRequest.getIdentifier());

            // 设置数据库中文件的相对路径
            paperUploadRequest.setFilePath("/files/" + currentDate + "/" + paperUploadRequest.getIdentifier());

            // 依次创建所有必要路径
            Files.createDirectories(tempIdentifierPath);
            Files.createDirectories(finalIdentifierPath);

            // 当前文件分片
            Integer chunkNumber = paperUploadRequest.getChunkNumber();
            Path chunkFilePath = tempIdentifierPath.resolve(chunkNumber.toString());

            // 如果分片已经存在，则直接返回
            if (Files.exists(chunkFilePath)) {
                // 判断是不是最后一个分片，如果是，则合并分片
                verifyIfItIsTheLastShard(paperUploadRequest, tempIdentifierPath, finalIdentifierPath, userId);
                return;
            }

            // 上传
            file.transferTo(chunkFilePath);

            // 判断是不是最后一个分片，如果是，则合并分片
            verifyIfItIsTheLastShard(paperUploadRequest, tempIdentifierPath, finalIdentifierPath, userId);

        } catch (Exception e) {
            throw new RuntimeException("上传文件出错，" + e.getMessage());
        }

    }

    @Override
    public Mono<String> chat(Long paperId) {
        try {
            // 如果未登录，直接抛出异常
            if(!StpUtil.isLogin()) throw new NoLoginException();

            // 获取id对应的论文路径
            Paper paper = paperMapper.queryPaperById(paperId);

            if(paper == null || paper.getFilePath() == null) return Mono.empty();

            // 构建本地路径
            Path rootPath = Paths.get(fileUploadPath);
            Path filePath = FileUtils.buildFilePath(rootPath, paper.getFilePath());

            // 读取pdf内容
            String pdfText = PdfBoxTextExtractorUtils.extractText(filePath);

            return switch (type) {
                case "qwen" -> QwenApiUtil.extractPaperInfo(pdfText);
                default -> DeepSeekApiUtil.extractPaperInfo(pdfText);
            };
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void batchDeletePaper(List<Long> paperIds, Long parentId, Long userId) {
        // 校验传入的 paperIds 非空
        if (paperIds == null || paperIds.isEmpty()) {
            throw new PapersIsEmptyException();
        }

        // 查询回收站中已存在的记录（paperIds + parentId 联合判断，确保精准匹配）
        List<RecycleBin> recycleBinsList = recycleBinMapper.queryAllRecycleBinByIdFolderId(paperIds, parentId);

        // 已存在回收站的 paperId + 不在回收站的 paperId
        List<Long> existPaperIds = new ArrayList<>();
        // 先收集回收站中已存在的 paperId
        for (RecycleBin recycleBin : recycleBinsList) {
            existPaperIds.add(recycleBin.getPaperId());
        }
        // 筛选出：原始 paperIds 中 不在 existPaperIds 里的（即需要删除并移入回收站的）
        List<Long> notExistPaperIds = paperIds.stream()
                .filter(paperId -> !existPaperIds.contains(paperId))
                .collect(Collectors.toList());

        // 若没有需要处理的 paperId（全部已在回收站），直接返回，不执行后续操作
        if (notExistPaperIds.isEmpty()) {
            return;
        }

        // 批量从目录中删除
        paperFolderMapper.batchDeleteByPaperIdFolderId(notExistPaperIds, parentId);
        // 批量移入回收站
        recycleBinMapper.batchRemoveToRecycleBin(notExistPaperIds, parentId, userId);
    }

    /**
     * 验证与后续操作
     *
     * @param paperUploadRequest  请求
     * @param tempIdentifierPath  临时路径
     * @param finalIdentifierPath 最终路径
     * @param userId              用户id
     * @throws IOException IO异常
     */
    private void verifyIfItIsTheLastShard(PaperUploadRequest paperUploadRequest, Path tempIdentifierPath, Path finalIdentifierPath, Long userId) throws IOException {
        long uploadedChunkCount = countValidUploadedChunks(tempIdentifierPath, paperUploadRequest.getTotalChunks());
        if (uploadedChunkCount != paperUploadRequest.getTotalChunks()) {
            return;
        }

        // 合并分片
        mergeAndMoveChunks(paperUploadRequest, tempIdentifierPath, finalIdentifierPath);

        // 删除临时存储
        deleteTempChunkDir(tempIdentifierPath);

        // 记录到数据库
        record2Database(false, paperUploadRequest, userId);
    }

    /**
     * 统计已上传的有效分片数量（仅计数1~totalChunks的数字文件名）
     */
    private long countValidUploadedChunks(Path tempIdentifierPath, Integer totalChunks) throws IOException {
        return Files.list(tempIdentifierPath).filter(Files::isRegularFile) // 仅保留文件（排除子目录）
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    try {
                        return Integer.parseInt(fileName);
                    } catch (NumberFormatException e) {
                        return -1; // 非法文件名，标记为无效
                    }
                }).filter(chunkNum -> chunkNum >= 1 && chunkNum <= totalChunks) // 仅保留有效分片编号
                .count();
    }


    /**
     * 合并分片并移动到最终路径（生成随机文件名）
     *
     * @param request             上传请求参数
     * @param tempIdentifierPath  临时分片目录（temp/日期/identifier）
     * @param finalIdentifierPath 最终文件目录（files/日期/identifier）
     */
    private void mergeAndMoveChunks(PaperUploadRequest request, Path tempIdentifierPath, Path finalIdentifierPath) throws IOException {
        // 提取文件后缀（从identifier下划线后获取，如"abc123_pdf"→"pdf"）
        String identifier = request.getIdentifier();
        String suffix = identifier.split("_")[1].toLowerCase();

        // 生成随机文件名
        String randomFileName = UUID.randomUUID() + "." + suffix;

        // 最终文件完整路径
        Path finalFilePath = finalIdentifierPath.resolve(randomFileName);

        // 设置数据库中存储路径
        request.setFilePath(request.getFilePath() + "/" + randomFileName);

        // 按分片编号升序排序
        File[] chunkFiles = tempIdentifierPath.toFile().listFiles((dir, name) -> {
            try {
                Integer.parseInt(name);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });

        assert chunkFiles != null;
        Arrays.sort(chunkFiles, Comparator.comparingInt(file -> Integer.parseInt(file.getName())));

        // 合并分片到最终文件路径
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(finalFilePath))) {
            byte[] buffer = new byte[1024 * 5]; // 5MB缓冲区
            int len;

            for (File chunkFile : chunkFiles) {
                try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(chunkFile.toPath()))) {
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                }
            }
        }
    }

    /**
     * 合并后删除临时分片目录（temp/日期/identifier）
     */
    private void deleteTempChunkDir(Path tempIdentifierPath) {
        try {
            Files.walk(tempIdentifierPath).sorted(Comparator.reverseOrder()) // 倒序删除：先删文件→再删子目录→最后删当前目录
                    .map(Path::toFile).forEach(File::delete);
        } catch (Exception e) {
            throw new RuntimeException("删除临时分片目录失败，可后续通过定时任务清理：" + tempIdentifierPath + "\n" + e.getMessage());
        }
    }

    /**
     * 将论文数据记录到数据库
     *
     * @param exist              是否已经存在
     * @param paperUploadRequest 请求体
     * @param userId             用户id
     */
    private void record2Database(boolean exist, PaperUploadRequest paperUploadRequest, Long userId) {
        // 不存在就添加
        if (!exist) {
            Paper paper = new Paper(
                    userId,
                    paperUploadRequest.getFilename(),
                    paperUploadRequest.getTotalSize().longValue(),
                    paperUploadRequest.getFilePath(),
                    paperUploadRequest.getIdentifier().split("_")[0]);
            paperMapper.insertPaper(paper);
            paperUploadRequest.setPaperId(paper.getId());
        }

        // 判断目录关系是否已经存在
        PaperFolder paperFolder = new PaperFolder(paperUploadRequest.getPaperId(), paperUploadRequest.getFolderId());

        Integer paperFolderNum = paperFolderMapper.queryPaperFolder(paperFolder);

        if (paperFolderNum != null && paperFolderNum > 0) {
            throw new RuntimeException("该目录下已存在该文件，请勿重复上传。");
        }

        // 写入到文件、目录表
        paperFolderMapper.insertPaperFolder(paperFolder);

        // 将回收站中的相同文件删除
        recycleBinMapper.deleteRecycleBinByPaperIdFolderId(paperFolder.getPaperId(), paperFolder.getFolderId());
    }
}
