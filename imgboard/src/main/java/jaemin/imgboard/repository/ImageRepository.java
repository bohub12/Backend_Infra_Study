package jaemin.imgboard.repository;

import jaemin.imgboard.domain.ImageFile;
import jaemin.imgboard.dto.ImageDto;
import jaemin.imgboard.dto.ImageRenderDto;

import java.util.List;

public interface ImageRepository {
    void save(ImageDto file) throws  Exception;
    boolean delete(Long fileId) throws Exception;

    List<ImageRenderDto> getImages();
    List<ImageFile> getFileList();
}
