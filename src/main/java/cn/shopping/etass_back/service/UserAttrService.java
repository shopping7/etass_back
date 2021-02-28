package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.UserAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-26
 */
public interface UserAttrService extends IService<UserAttr> {
    public List<String> getUserAttr(String username);

    public void DeleteUserAttr(String username);

    public void addUserAttr(String username, String attr);
}
