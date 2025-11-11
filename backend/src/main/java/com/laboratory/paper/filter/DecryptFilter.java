package com.laboratory.paper.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.laboratory.paper.utils.safety.SM2Utils;
import com.laboratory.paper.utils.safety.SM4Utils;
import com.laboratory.paper.wrapper.Sm4KeyHolder;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求解密过滤器
 */
@WebFilter(urlPatterns = "/*")
@Order(1)
@Component
public class DecryptFilter implements Filter {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${safety.sm2.global}")
    private String sm2KeyPairRedisKey;

    // 无需解密的接口路径
    private static final String[] NO_DECRYPT_PATHS = {
            "/crypto/sm2/public-key"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestUri = request.getRequestURI();

        // 检查是否为无需解密的接口
        if (isNoDecryptPath(requestUri)) {
            chain.doFilter(request, servletResponse);
            return;
        }

        try {
            // 提取加密参数
            String[] encryptedParams = extractEncryptedParams(request);
            String encryptedData = encryptedParams[0];
            String sm4KeyEncrypted = encryptedParams[1];

            // 校验加密参数
            if (!StringUtils.hasText(encryptedData) || !StringUtils.hasText(sm4KeyEncrypted)) {
                throw new IllegalArgumentException("加密参数缺失（encryptedData或sm4KeyEncrypted）");
            }

            // 解密SM4密钥
            String sm2PrivateKey = getSm2PrivateKey();
            String sm4Key = SM2Utils.decrypt(sm2PrivateKey, sm4KeyEncrypted);

            // 根据会话存储SM4密钥
            Sm4KeyHolder.setSm4Key(sm4Key);

            // 解密请求数据
            String decryptedData = SM4Utils.decrypt(sm4Key, encryptedData);

            // 包装解密后的请求
            DecryptRequestWrapper wrappedRequest = wrapDecryptedRequest(request, decryptedData);
            chain.doFilter(wrappedRequest, servletResponse);

        } catch (Exception e) {
            handleDecryptError(servletResponse, e);
        }
    }

    /**
     * 检查是否为无需解密的路径
     */
    private boolean isNoDecryptPath(String requestUri) {
        for (String path : NO_DECRYPT_PATHS) {
            if (requestUri.contains(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 提取加密参数
     */
    private String[] extractEncryptedParams(HttpServletRequest request) throws IOException {
        String encryptedData = null;
        String sm4KeyEncrypted = null;
        String method = request.getMethod();
        String contentType = request.getContentType();

        if ("GET".equalsIgnoreCase(method)) {
            // GET请求从Query参数提取
            encryptedData = request.getParameter("encryptedData");
            sm4KeyEncrypted = request.getParameter("sm4KeyEncrypted");
        } else {
            // POST/PUT请求从Body提取
            if (contentType == null) {
                throw new IllegalArgumentException("请求Content-Type不能为空");
            }

            if (contentType.contains("application/json")) {
                String jsonBody = readRequestBody(request);
                Map<String, Object> bodyMap = JSON.parseObject(jsonBody, new TypeReference<Map<String, Object>>() {});
                encryptedData = (String) bodyMap.get("encryptedData");
                sm4KeyEncrypted = (String) bodyMap.get("sm4KeyEncrypted");
            } else if (contentType.contains("multipart/form-data")) {
                // FormData从表单参数提取
                encryptedData = request.getParameter("encryptedData");
                sm4KeyEncrypted = request.getParameter("sm4KeyEncrypted");
            } else if (contentType.contains("application/x-www-form-urlencoded")) {
                encryptedData = request.getParameter("encryptedData");
                sm4KeyEncrypted = request.getParameter("sm4KeyEncrypted");
            } else {
                throw new IllegalArgumentException("不支持的Content-Type: " + contentType);
            }
        }

        return new String[]{encryptedData, sm4KeyEncrypted};
    }

    /**
     * 获取SM2私钥
     */
    @SuppressWarnings("unchecked")
    private String getSm2PrivateKey() {
        Map<String, String> sm2KeyPair = (Map<String, String>) redisTemplate.opsForValue().get(sm2KeyPairRedisKey);
        if (sm2KeyPair == null || !sm2KeyPair.containsKey("privateKey")) {
            throw new RuntimeException("Redis中未找到SM2私钥，请先初始化密钥对");
        }
        return sm2KeyPair.get("privateKey");
    }

    /**
     * 包装解密后的请求
     */
    private DecryptRequestWrapper wrapDecryptedRequest(HttpServletRequest request, String decryptedData) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // GET请求：解析为参数Map
            Map<String, String[]> originalParams = parseGetParams(decryptedData);
            DecryptRequestWrapper wrapper = new DecryptRequestWrapper(request, new byte[0]);
            wrapper.setDecryptedParams(originalParams);
            return wrapper;
        } else {
            // POST请求：作为新的Body
            byte[] decryptedBody = decryptedData.getBytes(StandardCharsets.UTF_8);
            return new DecryptRequestWrapper(request, decryptedBody);
        }
    }

    /**
     * 解析GET参数
     */
    private Map<String, String[]> parseGetParams(String decryptedData) {
        Map<String, Object> paramMap = JSON.parseObject(decryptedData);
        Map<String, String[]> result = new HashMap<>(paramMap.size());

        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                result.put(key, new String[0]);
            } else if (value instanceof String) {
                result.put(key, new String[]{(String) value});
            } else {
                result.put(key, new String[]{value.toString()});
            }
        }
        return result;
    }

    /**
     * 读取请求体
     */
    private String readRequestBody(HttpServletRequest request) throws IOException {
        try (ServletInputStream is = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    /**
     * 处理解密错误
     */
    private void handleDecryptError(ServletResponse servletResponse, Exception e) throws IOException {
        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("code", 500);
        errorResult.put("msg", "请求解密失败: " + e.getMessage());
        errorResult.put("success", false);

        PrintWriter writer = servletResponse.getWriter();
        writer.write(JSON.toJSONString(errorResult));
        writer.flush();
    }

    /**
     * 自定义请求包装类
     */
    public static class DecryptRequestWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {
        private final byte[] decryptedBody;
        @Setter
        private Map<String, String[]> decryptedParams;

        public DecryptRequestWrapper(HttpServletRequest request, byte[] decryptedBody) {
            super(request);
            this.decryptedBody = decryptedBody;
            this.decryptedParams = new HashMap<>(request.getParameterMap());
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBody);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return bis.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                    // 无需实现
                }

                @Override
                public int read() {
                    return bis.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }

        @Override
        public String getParameter(String name) {
            String[] values = decryptedParams.get(name);
            return values != null && values.length > 0 ? values[0] : null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return decryptedParams;
        }

        @Override
        public String[] getParameterValues(String name) {
            return decryptedParams.get(name);
        }
    }
}