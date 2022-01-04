package com.team11.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewMessage {
    private String data;
    private String roomId;

    @Override
    public String toString() {
        return "Message{" +
                "data='" + data + '\'' +
                '}';
    }
}
