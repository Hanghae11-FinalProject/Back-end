package com.team11.backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String imageName;
    private String imageUrl;
}
