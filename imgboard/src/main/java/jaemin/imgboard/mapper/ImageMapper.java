package jaemin.imgboard.mapper;

import jaemin.imgboard.domain.ImageFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImageMapper {

    @Select("SELECT * from image")
    public List<ImageFile> selectAll();

}
