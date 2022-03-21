package jaemin.imgboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @ToString
@AllArgsConstructor
public class ImageDto {
    private String viewName;
    private MultipartFile file;
}
