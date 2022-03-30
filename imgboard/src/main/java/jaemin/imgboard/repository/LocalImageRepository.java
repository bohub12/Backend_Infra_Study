package jaemin.imgboard.repository;

import jaemin.imgboard.domain.ImageFile;
import jaemin.imgboard.dto.ImageMetaDto;
import jaemin.imgboard.dto.ImageUploadDto;
import jaemin.imgboard.dto.ImageRenderDto;
import jaemin.imgboard.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LocalImageRepository implements ImageRepository {

    private final ImageMapper imageMapper;

    private final String PERIOD = "\\.";

    /**
     *
     */
    private final String uploadDir = "/Users/jaemin/desktop/uploaded/";

    /**
     * dto 넘겨받아서 store에 저장
     * multipartfile 로컬에 저장
     *
     * @param dto
     */
    @Override
    public void save(ImageUploadDto dto) throws Exception {

        MultipartFile file = dto.getFile();
        String viewName = dto.getViewName();

        if (!file.isEmpty()) {
            String suffix = getSuffix(file);
            String fullPath = getFullPath(viewName, suffix);
            try {
                file.transferTo(new File(fullPath));
                ImageFile imageFile = new ImageFile(
                        file.getOriginalFilename(), viewName, fullPath, suffix);
                log.info("imageFile = {}", imageFile);
                imageMapper.save(imageFile);
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
            ImageFile target = imageMapper.selectById(fileId);
            File targetFile = new File(target.getFilePath());
            if (targetFile.exists()) {
                if (!targetFile.delete()) {
                    return false;
                }
            }
        } catch (NullPointerException e) {
            throw e;
        } finally {
            imageMapper.delete(fileId);
            return true;
        }
    }

    @Override
    public ImageRenderDto read(Long fileId) {
        ImageFile imageMeta = imageMapper.selectById(fileId);

        String imagePath = imageMeta.getFilePath();
        return new ImageRenderDto(new FileSystemResource(imagePath), imagePath);
    }

    @Override
    public List<ImageMetaDto> getImageList() {
        List<ImageMetaDto> list = new ArrayList<>();
        List<ImageFile> imageList = imageMapper.selectAll();
        for (ImageFile image : imageList) {
            list.add(new ImageMetaDto(image.getFileId(), image.getViewName()));
        }
        return list;
    }

    /**
     * Test Function : Initiation
     *
     * @throws IOException
     */
//    @PostConstruct
    public void testInit() throws Exception {

        File file = new File("/Users/jaemin/desktop/test.png");

        InputStream input = new FileInputStream(file);
        MultipartFile mFile = new MockMultipartFile("test", input);

        String viewName = "0";
        for (int i = 0; i < 10; i++) {
            viewName = String.valueOf((Integer.parseInt(viewName) + 1));
            ImageUploadDto dto = new ImageUploadDto(viewName, mFile);
            this.save(dto);
        }

        List<ImageFile> list = imageMapper.selectAll();
        log.info("list.size() = {}", list.size());
        for (ImageFile imageFile : list) {
            log.info("imageFile = {}", imageFile);
        }
    }
}
