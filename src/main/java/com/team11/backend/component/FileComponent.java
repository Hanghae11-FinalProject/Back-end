package com.team11.backend.component;

import com.team11.backend.dto.ImageDto;
import com.team11.backend.model.Image;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileComponent {

    //파일업로드용 함수
    public List<ImageDto> fileUploadAndGetImageList(List<MultipartFile> images) throws IOException {
        List<ImageDto> imageDtos = new ArrayList<>();
        // 이미지 파일을 저장할 경로
       // String saveLocation = "/Users/jeong-yeongbin/Desktop/image/";
        String saveLocation = "/C://woojin/";

        for (MultipartFile image: images) {
            String fileName = createRandomFileNameAndGetFilePath(image);
            //savelocation 위치로 파일 업로드를 실행한다.
            image.transferTo(new File(saveLocation + fileName));
            ImageDto imageDto = ImageDto.builder()
                    .imageName(fileName)
                    .imageUrl("images/"+fileName)
                    .build();
            imageDtos.add(imageDto);
        }

        return imageDtos;

    }

    //파일이름을 UUID로 랜덤적으로 생성한다.
    public String createRandomFileNameAndGetFilePath(MultipartFile image){
       UUID uuid = UUID.randomUUID();
       return uuid+getContentType(image);
    }

    //파일확장자를 추출하는 함수.
    public String getContentType(MultipartFile image){
        // unprocessedContentType 예시 - > image/png
        String[] unprocessedContentType = image.getContentType().split("/");
        // 예시 -> .png
        return "."+unprocessedContentType[1];
    }

}
