package cn.shopping.etass_back.service;

import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.SK;
import cn.shopping.etass_back.entity.US;
import cn.shopping.etass_back.entity.UploadFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UploadFileService extends IService<UploadFile> {
    public void Enc(PP pp, SK sk, US user_s, File file, String policy, String[] KW);

    public List<UploadFile> getFile(String[] KW);
}
