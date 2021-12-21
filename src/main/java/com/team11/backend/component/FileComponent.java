package com.team11.backend.component;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class FileComponent {

    //파일업로드용 함수
    public void fileUpload(List<MultipartFile> images) throws IOException {

        // 이미지 파일을 저장할 경로
        String saveLocation = "/Users/jeong-yeongbin/Desktop/image/";

        for (MultipartFile image: images) {
            String fileName = createRandomFileNameAndGetFilePath(image);
            //savelocation 위치로 파일 업로드를 실행한다.
            image.transferTo(new File(saveLocation + fileName));
        }

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
