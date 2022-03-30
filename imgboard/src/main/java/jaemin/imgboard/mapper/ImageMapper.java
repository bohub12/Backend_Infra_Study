package jaemin.imgboard.mapper;

import jaemin.imgboard.domain.ImageFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageMapper {

    @Insert("INSERT INTO IMAGE VALUES( #{fileId}, #{fileName}, #{viewName}, #{filePath}, #{suffix} )")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void save(ImageFile imageFile);

    @Select("SELECT * FROM IMAGE WHERE fileId = #{fileId}")
    ImageFile selectById(long fileId);

    @Delete("DELETE FROM IMAGE WHERE fileId = #{#fileId}")
    void delete(Long fileId);

    @Select("SELECT * from image")
    List<ImageFile> selectAll();

}
