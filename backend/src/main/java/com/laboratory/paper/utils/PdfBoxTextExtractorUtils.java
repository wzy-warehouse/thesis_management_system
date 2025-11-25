package com.laboratory.paper.utils;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PdfBoxTextExtractorUtils {

    /**
     * 提取 PDF 文本（支持 MultipartFile，前端上传场景）
     */
    public static String extractText(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("PDF 文件不能为空");
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return extractText(inputStream);
        }
    }

    /**
     * 提取PDF文本，通过路径提取
     */
    public static String extractText(Path path) throws IOException {
        // 校验 Path 合法性（避免空指针/无效路径）
        if (path == null) {
            throw new IllegalArgumentException("PDF 文件路径不能为空");
        }
        // 校验文件是否存在且是文件（非目录）
        if (!Files.exists(path)) {
            throw new IOException("PDF 文件不存在：" + path.toAbsolutePath());
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("路径不是有效文件：" + path.toAbsolutePath());
        }

        // 通过 Path 直接获取输入流
        try (InputStream inputStream = Files.newInputStream(path);
             RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(inputStream)) {
            return extractText(randomAccessRead);
        } catch (IOException e) {
            // 包装异常信息，添加路径上下文，便于排查
            throw new IOException("提取 PDF 文本失败（路径：" + path.toAbsolutePath() + "）", e);
        }
    }

    /**
     * 提取 PDF 文本（支持 InputStream，通用场景）
     */
    public static String extractText(InputStream inputStream) throws IOException {
        try (RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(inputStream)) {
            return extractText(randomAccessRead);
        }
    }

    /**
     * 提取 PDF 文本（支持字节数组，二进制数据场景）
     */
    public static String extractText(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF 字节数组不能为空");
        }
        try (RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(pdfBytes)) {
            return extractText(randomAccessRead);
        }
    }

    /**
     * 提取 PDF 文本（支持本地文件，测试/批量处理场景）
     */
    public static String extractText(File pdfFile) throws IOException {
        if (!pdfFile.exists() || !pdfFile.isFile()) {
            throw new IllegalArgumentException("PDF 文件不存在或不是有效文件");
        }
        try (RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(new FileInputStream(pdfFile))) {
            return extractText(randomAccessRead);
        }
    }

    /**
     * 核心提取方法
     */
    private static String extractText(RandomAccessRead randomAccessRead) throws IOException {

        try (PDDocument document = Loader.loadPDF(randomAccessRead)) {
            // 验证 PDF 是否加密
            if (document.isEncrypted()) {
                throw new IOException("加密的 PDF 暂不支持提取");
            }

            PDFTextStripper textStripper = new PDFTextStripper();
            // 提取文本并清洗
            String rawText = textStripper.getText(document);
            return cleanText(rawText);
        }
    }

    /**
     * 分页提取 PDF文本
     */
    public static List<String> extractTextByPage(InputStream inputStream) throws IOException {
        List<String> pageTexts = new ArrayList<>();

        try (RandomAccessRead randomAccessRead = new RandomAccessReadBuffer(inputStream)) {
            // 使用新的 Loader.loadPDF() 方法[citation:8]
            try (PDDocument document = Loader.loadPDF(randomAccessRead)) {
                if (document.isEncrypted()) {
                    throw new IOException("加密的 PDF 暂不支持提取");
                }

                int totalPages = document.getNumberOfPages();
                for (int pageNum = 1; pageNum <= totalPages; pageNum++) {
                    PDFTextStripper textStripper = new PDFTextStripper();
                    textStripper.setStartPage(pageNum);
                    textStripper.setEndPage(pageNum);
                    String pageText = cleanText(textStripper.getText(document));
                    pageTexts.add(pageText);
                }
            }
        }
        return pageTexts;
    }

    /**
     * 文本清洗：去除多余空格、换行、制表符，优化格式
     */
    private static String cleanText(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) {
            return "";
        }
        String cleaned = rawText.replaceAll("\\s+", " ");
        cleaned = cleaned.trim();
        cleaned = cleaned.replaceAll("[\\x00-\\x1F]", "");
        return cleaned;
    }
}