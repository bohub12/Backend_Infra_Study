package jaemin.imgboard.dto;

import lombok.*;
import org.springframework.core.io.Resource;

@Getter @Setter @ToString
@AllArgsConstructor
public class ImageRenderDto {
    private Resource resource;
    private String filePath;
}
