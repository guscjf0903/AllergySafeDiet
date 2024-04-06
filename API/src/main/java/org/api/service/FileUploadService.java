package org.api.service;

import static org.api.exception.ErrorCodes.AWS_S3_UPLOAD_FAILED;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            return Collections.emptyList();
        }
        try{
            List<String> fileUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                String fileKey = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));
                String fileUrl = amazonS3Client.getUrl(bucketName, fileKey).toString();
                fileUrls.add(fileUrl);
            }

            return fileUrls;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(AWS_S3_UPLOAD_FAILED);
        }
    }
}
