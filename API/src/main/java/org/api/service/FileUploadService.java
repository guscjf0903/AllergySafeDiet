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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final ExecutorService sharedExecutorService;  // 생성자를 통한 주입


    public List<String> uploadFiles(List<MultipartFile> files) throws InterruptedException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<Future<String>> futures = new ArrayList<>();
        List<String> fileUrls = Collections.synchronizedList(new ArrayList<>());

        try {
            for (MultipartFile file : files) {
                futures.add(sharedExecutorService.submit(() -> uploadFile(file)));
            }

            for (Future<String> future : futures) {
                try {
                    fileUrls.add(future.get());
                } catch (ExecutionException e) {
                    throw new CustomException(AWS_S3_UPLOAD_FAILED);
                }
            }
        } catch (Exception e) {
            throw new CustomException(AWS_S3_UPLOAD_FAILED);
        }

        return fileUrls;
    }

    private String uploadFile(MultipartFile file) throws IOException {
        String fileKey = UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);
        return amazonS3Client.getUrl(bucketName, fileKey).toString();
    }


//    public List<String> uploadFiles(List<MultipartFile> files) {
//        if (files == null || files.isEmpty()) {
//            return Collections.emptyList();
//        }
//        try {
//            List<String> fileUrls = new ArrayList<>();
//
//            for (MultipartFile file : files) {
//                String fileKey = UUID.randomUUID() + "-" + file.getOriginalFilename();
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentLength(file.getSize());
//
//                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(),
//                        metadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead);
//
//                amazonS3Client.putObject(putObjectRequest);
//                String fileUrl = amazonS3Client.getUrl(bucketName, fileKey).toString();
//                fileUrls.add(fileUrl);
//            }
//
//            return fileUrls;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            throw new CustomException(AWS_S3_UPLOAD_FAILED);
//        }
//    }

}

