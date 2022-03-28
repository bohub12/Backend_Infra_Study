package jaemin.imgboard.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString
@RequiredArgsConstructor
public class ImageFile {
    private final Long fileId;
    private final String fileName;
    private final String viewName;
    private final String filePath;
    private final String suffix;
}
