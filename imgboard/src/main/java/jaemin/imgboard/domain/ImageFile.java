package jaemin.imgboard.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageFile {
    private Long fileId;
    private String fileName;
    private String viewName;
    private String filePath;
    private String suffix;

    public ImageFile(String fileName, String viewName, String filePath, String suffix) {
        this.fileName = fileName;
        this.viewName = viewName;
        this.filePath = filePath;
        this.suffix = suffix;
    }
}
