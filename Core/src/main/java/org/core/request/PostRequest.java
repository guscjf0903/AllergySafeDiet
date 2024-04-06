package org.core.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(
        @NotNull
        String title,
        @NotNull
        String content,
        List<Long> foodIds,
        List<Long> healthIds,
        List<MultipartFile> images

){}
