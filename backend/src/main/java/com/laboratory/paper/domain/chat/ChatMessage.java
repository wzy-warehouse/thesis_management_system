package com.laboratory.paper.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    /** 角色：system/user/assistant */
    private String role;
    /** 消息内容 */
    private String content;
}
