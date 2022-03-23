package jaemin.imgboard.service;

import jaemin.imgboard.dto.ImageMetaDto;
import jaemin.imgboard.dto.ImageUploadDto;
import jaemin.imgboard.dto.ImageRenderDto;
import jaemin.imgboard.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveUploadedImage(ImageUploadDto dto) throws Exception {
        imageRepository.save(dto);
    }

    public void deleteImage(Long filedId) throws Exception {
        imageRepository.delete(filedId);
    }

    public List<ImageMetaDto> getImageIdList() {
        return imageRepository.getImageList();
    }

    public ImageRenderDto loadImages(Long fileId) {
        return imageRepository.read(fileId);
    }

}
