package jaemin.imgboard.mapper;

import jaemin.imgboard.domain.ImageFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImageMapper {

    @Insert("INSERT INTO IMAGE(filename,viewname,filepath,suffix)" +
            " VALUES( '${fileName}', '${viewName}', '${filePath}', '${suffix}' )")
    void save(ImageFile imageFile);

    @Select("SELECT * FROM IMAGE WHERE fileId = #{fileId}")
    ImageFile selectById(long fileId);

    @Delete("DELETE FROM IMAGE WHERE fileId = #{#fileId}")
    void delete(Long fileId);

    @Select("SELECT * from image")
    List<ImageFile> selectAll();

}
