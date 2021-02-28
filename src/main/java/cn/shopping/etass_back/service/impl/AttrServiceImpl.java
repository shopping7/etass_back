package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.entity.Attr;
import cn.shopping.etass_back.mapper.AttrMapper;
import cn.shopping.etass_back.service.AttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {
    @Autowired
    AttrMapper mapper;

    @Override
    public List<Attr> getAllAttr() {
        return mapper.selectList(null);
    }

    @Override
    public void deleteAttr(String attr) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("attr", attr);
        mapper.delete(wrapper);
    }

    @Override
    public void addAttr(Attr attr) {
        mapper.insert(attr);
    }

    @Override
    public void editAttr(Attr attr) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("attr",attr.getAttr());
        mapper.update(attr,queryWrapper);
    }
}
