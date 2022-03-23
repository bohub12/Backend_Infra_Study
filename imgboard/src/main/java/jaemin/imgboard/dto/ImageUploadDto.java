package jaemin.imgboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @ToString
@AllArgsConstructor
public class ImageUploadDto {
    private final String viewName;
    private final MultipartFile file;
}
