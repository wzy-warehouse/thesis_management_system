package com.laboratory.paper.utils;

import com.laboratory.paper.domain.chat.ChatMessage;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * 通义千问 API 静态工具类
 * 依赖：OpenAI SDK 2.6.0
 * 注意：使用前必须先调用 init() 方法初始化配置
 */
public class QwenApiUtil {
    // 静态配置项（通义千问专属配置，与DeepSeek结构一致）
    private static String BASE_URL; // 通义千问兼容OpenAI协议的地址
    private static String API_KEY;  // 通义千问 API 密钥
    private static String MODEL;    // 通义千问模型名称

    // 静态依赖组件（通义千问 OpenAI 客户端）
    private static OpenAIClient openAIClient;
    private static String SYSTEM_PROMPT; // 复用原提示文本（无需修改）

    // 提示文本文件路径（与DeepSeek共用同一文件，保持一致）
    private static final String PROMPT_FILE_PATH = "prompt/paper_extraction_prompt.txt";

    // 私有构造函数：禁止实例化（同DeepSeek设计）
    private QwenApiUtil() {}

    /**
     * 初始化配置（用法同 DeepSeekApiUtil.init()）
     * @param baseUrl 通义千问兼容地址
     * @param apiKey  通义千问 API 密钥
     * @param model   通义千问模型名称
     * @throws IOException 提示文件读取失败
     */
    public static void init(String baseUrl, String apiKey, String model) throws IOException {
        // 初始化配置（与DeepSeek结构完全一致）
        BASE_URL = baseUrl;
        API_KEY = apiKey;
        MODEL = model;

        // 初始化通义千问 OpenAI 客户端（基于官方SDK）
        openAIClient = OpenAIOkHttpClient.builder()
                .apiKey(API_KEY)
                .baseUrl(BASE_URL)
                .build();

        // 读取提示文本（完全复用DeepSeek的逻辑，共用同一文件）
        loadSystemPrompt();
    }

    /**
     * 读取 classpath 下的提示文本文件（完全复用DeepSeek实现）
     */
    private static void loadSystemPrompt() throws IOException {
        ClassLoader classLoader = QwenApiUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PROMPT_FILE_PATH)) {
            if (inputStream == null) {
                throw new IOException("提示文件不存在：classpath:" + PROMPT_FILE_PATH);
            }
            byte[] bytes = org.springframework.util.FileCopyUtils.copyToByteArray(inputStream);
            SYSTEM_PROMPT = new String(bytes, StandardCharsets.UTF_8);
        }
    }

    /**
     * 静态调用：论文信息提取核心方法
     * @param pdfText PDF 论文文本
     * @return 大模型返回的 JSON 格式结果
     */
    public static Mono<String> extractPaperInfo(String pdfText) {
        // 初始化状态校验（同DeepSeek的校验逻辑）
        if (openAIClient == null || SYSTEM_PROMPT == null || API_KEY == null) {
            throw new IllegalStateException("QwenApiUtil 未初始化！请先调用 init() 方法");
        }

        // 构建消息列表（system提示 + user论文文本，与DeepSeek完全一致）
        List<ChatMessage> messages = List.of(
                new ChatMessage("system", SYSTEM_PROMPT),
                new ChatMessage("user", pdfText)
        );

        // 构建通义千问请求参数（兼容OpenAI协议）
        ChatCompletionCreateParams requestParams = ChatCompletionCreateParams.builder()
                .model(MODEL) // 通义千问模型名称
                // 转换为通义千问支持的消息格式
                .addSystemMessage(messages.get(0).getContent())
                .addUserMessage(messages.get(1).getContent())
                .build();

        // 响应式调用
        return Mono.fromCallable(() -> {
                    // 调用通义千问API
                    ChatCompletion chatCompletion = openAIClient.chat().completions().create(requestParams);
                    // 解析响应结果
                    if (chatCompletion != null && chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
                        return safeExtractJson(chatCompletion.choices().get(0).message().content().toString());
                    }
                    return getDefaultJson(); // 无结果兜底
                })
                .timeout(Duration.ofSeconds(30)) // 30秒超时
                .onErrorReturn(getDefaultJson()); // 异常兜底
    }

    /**
     * 重载：同步调用
     * @param pdfText PDF 论文文本
     * @return JSON 格式结果（阻塞获取）
     */
    public static String extractPaperInfoSync(String pdfText) {
        return extractPaperInfo(pdfText)
                .block(Duration.ofSeconds(30)); // 阻塞30秒，超时返回兜底JSON
    }

    /**
     * 格式化文本
     * @param optionalJson 文本
     * @return json
     */
    private static String safeExtractJson(String optionalJson) {
        // 空值处理
        if (optionalJson.isEmpty()) {
            return "";
        }

        // 安全截取
        try {
            return optionalJson.substring("Optional[".length(), optionalJson.length() - 1).trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new StringIndexOutOfBoundsException(optionalJson);
        }
    }

    /**
     * 默认JSON
     */
    private static String getDefaultJson() {
        return "{\"题目\":\"未知\",\"作者\":\"未知\",\"发表时间\":\"未知\",\"发表期刊\":\"未知\",\"期刊类型\":\"未知\",\"研究方向\":\"未知\",\"创新点\":\"未知\",\"不足\":\"未明确提及\",\"关键字\":\"未知\"}";
    }
}