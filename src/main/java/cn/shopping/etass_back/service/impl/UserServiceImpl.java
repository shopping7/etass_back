package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.domain.UserVO;
import cn.shopping.etass_back.entity.User;
import cn.shopping.etass_back.mapper.UserMapper;
import cn.shopping.etass_back.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper mapper;

    @Override
    public List<UserVO> getAllUsers() {
        return mapper.getAllUsers();
    }

    @Override
    public UserVO loginUser(String username, String password) {
        return mapper.loginUser(username,password);
    }

    @Override
    public UserVO getOneUser(String username) {
        return mapper.getOneUser(username);
    }

}
