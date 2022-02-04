package com.team11.backend.component;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.team11.backend.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/*AWS의 S3를 사용해서 입력받은 파일 데이터를 저장하고, 이미지 url을 반환하여 이 Url을 이용해 사진을 보여주는 방식을 주로 사용했습니다 */
@RequiredArgsConstructor
@Component
public class FileUploadService {
    private final AwsS3UploadService s3Service;

    //파일을 s3에 업로드와 동시에 Response
    public ImageDto uploadImage(MultipartFile file) throws IOException {
        String filename = createFileName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();
        /*inputStream을 매개변수로 받는 메소드인 경우에는 마지막으로 ObjectMetadata를 추가로 받는 것을 볼 수있다.
          이는 InputStream을 통해 Byte 만이 전달 되기 때문에, 해당 파일에 대한 정보가 없기 때문입니다. 따라서 ObjectMetadata 에 파일에
          대한 정보를 추가하여 매개변수로 같이 전달한다.*/
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
