package com.team11.backend.component;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.team11.backend.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileUploadService {
    private final AwsS3UploadService s3Service;

    public ImageDto uploadImage(MultipartFile file) throws IOException {
        String filename = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();

        s3Service.uploadFile(inputStream,objectMetadata,filename);
        return ImageDto.builder()
                .imageName(filename)
                .imageUrl(s3Service.getFileUrl(filename))
                .build();

    }

    //기존 확장자명을 유지한체 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalFileName){
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일 확장자명을 가져오는 함수
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
