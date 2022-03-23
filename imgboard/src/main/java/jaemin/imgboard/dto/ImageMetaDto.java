package jaemin.imgboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
@AllArgsConstructor
public class ImageMetaDto {
    private Long fileId;
    private String viewName;
}
