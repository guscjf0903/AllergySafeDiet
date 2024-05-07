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

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadFiles(List<MultipartFile> files) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool(); // 적응적인 스레드 풀 사용

        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<Future<String>> futures = new ArrayList<>();
        List<String> fileUrls = Collections.synchronizedList(new ArrayList<>());

        try {
            for (MultipartFile file : files) {
                futures.add(executorService.submit(() -> uploadFile(file)));
            }

            for (Future<String> future : futures) {
                try {
                    fileUrls.add(future.get());
                } catch (ExecutionException e) {
                    throw new CustomException(AWS_S3_UPLOAD_FAILED);
                }
            }
        } finally {
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
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


}