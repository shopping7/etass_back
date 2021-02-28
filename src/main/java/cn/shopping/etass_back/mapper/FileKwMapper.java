package cn.shopping.etass_back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-27
 */
public interface FileKwMapper {

    @Update("INSERT INTO file_kw(file_id,kw) VALUES(#{file_id},#{kw})")
    public void insertFileKW(int file_id, String kw);
}
