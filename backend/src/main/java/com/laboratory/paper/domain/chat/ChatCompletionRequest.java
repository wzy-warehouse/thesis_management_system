package com.laboratory.paper.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatCompletionRequest {
    /** 模型名称 */
    private String model;
    /** 消息列表 */
    private List<ChatMessage> messages;
    /** 是否流式响应 */
    @JsonProperty("stream")
    private boolean stream = false;
}