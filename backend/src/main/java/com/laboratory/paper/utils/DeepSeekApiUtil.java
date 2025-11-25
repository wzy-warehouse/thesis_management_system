package com.laboratory.paper.utils;

import com.laboratory.paper.domain.chat.ChatCompletionRequest;
import com.laboratory.paper.domain.chat.ChatCompletionResponse;
import com.laboratory.paper.domain.chat.ChatMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * DeepSeek API 静态工具类，支持直接静态调用
 * 注意：使用前必须先调用 init() 方法初始化配置
 */
public class DeepSeekApiUtil {
    // 静态配置项
    private static String BASE_URL;
    private static String API_KEY;
    private static String MODEL;

    // 静态依赖组件
    private static WebClient webClient;
    private static String SYSTEM_PROMPT;

    // 提示文本文件路径
    private static final String PROMPT_FILE_PATH = "prompt/paper_extraction_prompt.txt";

    // 私有构造函数：禁止实例化
    private DeepSeekApiUtil() {}

    /**
     * 初始化配置（必须在使用前调用，建议项目启动时初始化）
     * @param baseUrl DeepSeek API 基础地址（如 https://api.deepseek.com）
     * @param apiKey DeepSeek API 密钥
     * @param model 模型名称（如 deepseek-chat）
     * @throws IOException 提示文件读取失败
     */
    public static void init(String baseUrl, String apiKey, String model) throws IOException {
        // 初始化配置
        BASE_URL = baseUrl;
        API_KEY = apiKey;
        MODEL = model;

        // 初始化 WebClient（线程安全，静态初始化一次即可）
        webClient = WebClient.builder().baseUrl(BASE_URL).build();

        // 读取提示文本（从 classpath 读取，无需 Spring ResourceLoader）
        loadSystemPrompt();
    }

    /**
     * 读取 classpath 下的提示文本文件
     */
    private static void loadSystemPrompt() throws IOException {
        // 用类加载器读取 resources 下的文件（无 Spring 依赖）
        ClassLoader classLoader = DeepSeekApiUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(PROMPT_FILE_PATH)) {
            if (inputStream == null) {
                throw new IOException("提示文件不存在：classpath:" + PROMPT_FILE_PATH);
            }
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            SYSTEM_PROMPT = new String(bytes, StandardCharsets.UTF_8);
        }
    }

    /**
     * 静态调用：论文信息提取核心方法
     * @param pdfText PDF 论文文本
     * @return 大模型返回的 JSON 格式结果（Mono 响应式流，支持非阻塞）
     */
    public static Mono<String> extractPaperInfo(String pdfText) {
        // 校验初始化状态
        if (webClient == null || SYSTEM_PROMPT == null || API_KEY == null) {
            throw new IllegalStateException("DeepSeekApiUtil 未初始化！请先调用 init() 方法");
        }

        // 构建消息列表（与原逻辑一致）
        List<ChatMessage> messages = List.of(
                new ChatMessage("system", SYSTEM_PROMPT),
                new ChatMessage("user", pdfText)
        );

        // 构建请求体
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(messages)
                .stream(false)
                .build();

        // 发送 API 请求（保留原超时、异常兜底逻辑）
        return webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ChatCompletionResponse.class)
                .map(response -> {
                    if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                        return response.getChoices().get(0).getMessage().getContent().trim();
                    }
                    return "{\"题目\":\"未知\",\"作者\":\"未知\",\"发表时间\":\"未知\",\"发表期刊\":\"未知\",\"期刊类型\":\"未知\",\"研究方向\":\"未知\",\"创新点\":\"未知\",\"不足\":\"未明确提及\",\"关键字\":\"未知\"}";
                })
                .timeout(Duration.ofSeconds(30))
                .onErrorReturn("{\"题目\":\"未知\",\"作者\":\"未知\",\"发表时间\":\"未知\",\"发表期刊\":\"未知\",\"期刊类型\":\"未知\",\"研究方向\":\"未知\",\"创新点\":\"未知\",\"不足\":\"未明确提及\",\"关键字\":\"未知\"}");
    }

    /**
     * 重载：同步调用
     * @param pdfText PDF 论文文本
     * @return  JSON 格式结果
     */
    public static String extractPaperInfoSync(String pdfText) {
        return extractPaperInfo(pdfText)
                .block(Duration.ofSeconds(30)); // 阻塞 30 秒，超时返回 null（后续可兜底）
    }
}