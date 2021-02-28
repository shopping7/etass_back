package cn.shopping.etass_back.service;

import cn.shopping.etass_back.domain.UserVO;
import cn.shopping.etass_back.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UserService extends IService<User> {
    public List<UserVO> getAllUsers();

    public UserVO loginUser(String username, String password);

    public UserVO getOneUser(String username);
}
