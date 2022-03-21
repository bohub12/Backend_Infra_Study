package jaemin.imgboard.service;

import jaemin.imgboard.dto.ImageDto;
import jaemin.imgboard.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    // properties에 넣어야함
    private final String uploadDir = "/Users/jaemin/desktop/test/";

    private final ImageRepository imageRepository;

    public void saveUploadedImage(ImageDto dto) throws IOException {
        MultipartFile file = dto.getFile();
        String viewName = dto.getViewName();

        if (!file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String[] ss = originalName.split("\\.");
            String suffix = ss[ss.length - 1];
            String fullPath = getFullPath(viewName, suffix);
            file.transferTo(new File(fullPath));
        }
    }

    public String getFullPath(String viewName, String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadDir);
        sb.append(viewName);
        sb.append(".");
        sb.append(suffix);
        String fullPath = sb.toString();
        return fullPath;
    }
}
