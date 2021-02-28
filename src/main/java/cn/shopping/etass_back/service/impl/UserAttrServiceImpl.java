package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.entity.UserAttr;
import cn.shopping.etass_back.mapper.UserAttrMapper;
import cn.shopping.etass_back.service.UserAttrService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class UserAttrServiceImpl extends ServiceImpl<UserAttrMapper, UserAttr> implements UserAttrService {
    @Autowired
    UserAttrMapper mapper;

    @Override
    public List<String> getUserAttr(String username) {
        return mapper.getUserAttr(username);
    }

    @Override
    public void DeleteUserAttr(String username) {

        mapper.deleteUserAttr(username);
    }

    @Override
    public void addUserAttr(String username, String attr) {
        mapper.addUserAttr(username,attr);
    }
}
