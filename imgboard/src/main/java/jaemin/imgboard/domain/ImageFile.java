package jaemin.imgboard.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ImageFile {
    private final Long fileId;
    private final String fileName;
    private String viewName;
    private String filePath;
}
