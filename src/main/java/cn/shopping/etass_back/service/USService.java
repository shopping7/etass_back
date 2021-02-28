package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.US;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-27
 */
public interface USService extends IService<US> {
    public void addS(String username);

    public US getS(String username);
}
