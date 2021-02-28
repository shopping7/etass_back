package cn.shopping.etass_back.mapper;

import cn.shopping.etass_back.domain.UserVO;
import cn.shopping.etass_back.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UserMapper extends BaseMapper<User> {
    public List<UserVO> getAllUsers();

    public UserVO loginUser(String username, String password);

    public UserVO getOneUser(String username);
}

