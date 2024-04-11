package org.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.api.entity.PostEntity;
import org.api.entity.PostImageUrlEntity;
import org.api.repository.PostImageUrlRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SaveS3UrlService {
    private final PostImageUrlRepository postImageUrlRepository;

    public void savePostImageUrl(PostEntity postEntity, List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            PostImageUrlEntity postImageUrlEntity = new PostImageUrlEntity(postEntity, fileUrl);
            postImageUrlRepository.save(postImageUrlEntity);
        }
    }
}
