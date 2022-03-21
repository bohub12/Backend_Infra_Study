package jaemin.imgboard.dto;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor
public class ImageRenderDto {
    private Long fileId;
    private String fileName;
}
