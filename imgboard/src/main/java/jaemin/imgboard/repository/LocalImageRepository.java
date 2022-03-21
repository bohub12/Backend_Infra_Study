package jaemin.imgboard.repository;

import jaemin.imgboard.domain.ImageFile;
import jaemin.imgboard.dto.ImageDto;
import jaemin.imgboard.dto.ImageRenderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class LocalImageRepository implements ImageRepository {

    private static final Map<Long, ImageFile> store = new HashMap<>();
    private static long sequence = 0L;

    private final String PERIOD = "\\.";

    /**
     *
     */
    private final String uploadDir = "/Users/jaemin/desktop/workspace/infra-study/imgboard/src/main/resources/static/images/";
    /**
     * dto 넘겨받아서 store에 저장
     * multipartfile 로컬에 저장
     *
     * @param dto
     */
    @Override
    public void save(ImageDto dto) throws Exception {

        MultipartFile file = dto.getFile();
        String viewName = dto.getViewName();

        if (!file.isEmpty()) {
            String suffix = getSuffix(file);
            String fullPath = getFullPath(viewName, suffix);
            try {
                file.transferTo(new File(fullPath));
                ImageFile imageFile = new ImageFile(
                        ++sequence, file.getOriginalFilename(), viewName, fullPath, suffix);
                store.put(imageFile.getFileId(), imageFile);
            } catch (IOException e) {
                throw e;
            }
        }
    }

    private String getSuffix(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String[] tempSplit = originalName.split(PERIOD);
        String suffix = tempSplit[tempSplit.length - 1];
        return suffix;
    }

    private String getFullPath(String viewName, String suffix) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadDir);
        sb.append(viewName);
        sb.append(".");
        sb.append(suffix);
        String fullPath = sb.toString();
        return fullPath;
    }

    @Override
    public boolean delete(Long fileId) throws Exception {
        log.info("DELETE START");
        try {
            ImageFile target = store.get(fileId);
            log.info("target = {}", target);
            File targetFile = new File(target.getFilePath());
            log.info("file = {}", targetFile.getPath());
            if (targetFile.exists()) {
                if (!targetFile.delete()) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            throw e;
        } finally {
            return true;
        }
    }

    @Override
    public List<ImageRenderDto> getImages() {
        List<ImageRenderDto> list = getImageRenderDtos();
        return list;
    }

    private List<ImageRenderDto> getImageRenderDtos() {
        List<ImageRenderDto> list = new ArrayList<>();
        getFileList().forEach(file -> {
            list.add(new ImageRenderDto(
                    file.getFileId(), file.getViewName() + "." + file.getSuffix()));
        });
        return list;
    }

    @Override
    public List<ImageFile> getFileList() {
        return new ArrayList<>(store.values());
    }
}
