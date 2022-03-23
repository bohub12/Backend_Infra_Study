package jaemin.imgboard.repository;

import jaemin.imgboard.dto.ImageMetaDto;
import jaemin.imgboard.dto.ImageUploadDto;
import jaemin.imgboard.dto.ImageRenderDto;

import java.util.List;

public interface ImageRepository {
    void save(ImageUploadDto file) throws  Exception;
    boolean delete(Long fileId) throws Exception;
    ImageRenderDto read(Long fileId);
    List<ImageMetaDto> getImageList();
}
