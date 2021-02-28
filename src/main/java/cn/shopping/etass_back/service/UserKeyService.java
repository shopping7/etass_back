package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.UserKey;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UserKeyService extends IService<UserKey> {
    public void KeyGen(PP pp, MSK msk, String username, String attributes[], boolean isNew);

    public UserKey getUserKey(String username);
}
