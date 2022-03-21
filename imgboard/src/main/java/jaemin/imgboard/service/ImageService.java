package jaemin.imgboard.service;

import jaemin.imgboard.dto.ImageDto;
import jaemin.imgboard.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveUploadedImage(ImageDto dto) throws Exception {
        imageRepository.save(dto);
    }

    public void deleteImage(Long filedId) throws Exception {
        imageRepository.delete(filedId);
    }
}
