package jaemin.imgboard.repository;

import jaemin.imgboard.domain.ImageFile;
import jaemin.imgboard.dto.ImageDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LocalImageRepository implements ImageRepository {

    private static final Map<Long, ImageFile> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public void save(ImageDto file) {
//        ImageFile imageFile = new ImageFile(++sequence, file.getOriginalName());
//        imageFile.setViewName(file.getViewName());
//        store.put(imageFile.getFileId(), imageFile);
    }

    @Override
    public void delete(Long fileId) {
//        store.remove(fileId);
    }

    @Override
    public List<ImageFile> getFileList() {
//        return new ArrayList<>(store.values());
        return null;
    }
}
