package com.team11.backend.model;


import com.team11.backend.dto.ImageDto;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Image {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private  String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "postId")
    private Post post;

    public void updateImage(ImageDto imageDto){
        this.imageName = imageDto.getImageName();
        this.imageUrl = imageDto.getImageUrl();
    }
}
