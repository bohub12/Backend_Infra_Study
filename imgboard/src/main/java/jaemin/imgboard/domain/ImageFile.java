package jaemin.imgboard.domain;

import lombok.*;

@Getter @Setter
@RequiredArgsConstructor
public class ImageFile {

    private final Long fileId;
    private final String fileName;

    private String viewName;
}
