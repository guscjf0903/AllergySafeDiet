package org.api.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.ImageEntity;
import org.api.entity.PostEntity;
import org.api.entity.PostImageUrlEntity;
import org.api.repository.ImageRepository;
import org.api.repository.PostImageUrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SaveS3UrlService {
    private final PostImageUrlRepository postImageUrlRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void savePostImageUrl(PostEntity postEntity, List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            PostImageUrlEntity postImageUrlEntity = new PostImageUrlEntity(postEntity, fileUrl);
            postImageUrlRepository.save(postImageUrlEntity);
        }
    }

    @Transactional
    public void uploadImagesToDatabase(List<MultipartFile> files, PostEntity postEntity) throws IOException {
        for (MultipartFile file : files) {
            byte[] imageData = file.getBytes();

            ImageEntity imageEntity = new ImageEntity(postEntity, imageData);
            imageRepository.save(imageEntity);
        }
    }
}
