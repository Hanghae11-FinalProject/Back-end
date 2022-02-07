package com.team11.backend.component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class AwsS3UploadService{

    private final AmazonS3 amazonS3;
    private final S3Component component;

    //S3Component를 주입받아서 프로퍼티에 추가한 버킷 정보를 가져와서 awsS3Client.putObject(...)를 통해 AWS S3로 파일을 업로드합니다.
    public void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName) {
        amazonS3.putObject(new PutObjectRequest(component.getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    //그리고 awsClient.getUrl(...)을 통해 업로드된 파일의 URI을 가져옵니다.
    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(component.getBucket(), fileName).toString();
    }

    //사파일 삭제
    public void deleteFile(String fileName){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(component.getBucket(), fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}
