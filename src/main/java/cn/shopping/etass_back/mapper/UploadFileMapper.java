package cn.shopping.etass_back.mapper;

import cn.shopping.etass_back.entity.UploadFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UploadFileMapper extends BaseMapper<UploadFile> {

    public void insertFile(UploadFile uploadFile);

//    @Select("SELECT * FROM upload_File WHERE id IN (SELECT file_id FROM file_kw WHERE kw IN (#{kw}) GROUP BY file_id)")
    public List<UploadFile> getFile(List<Integer> fileId);

//    @Select("SELECT file_id FROM file_kw WHERE kw IN (#{kw}) GROUP BY file_id")
    public List<Integer> getFileId(String[] kw);
}
