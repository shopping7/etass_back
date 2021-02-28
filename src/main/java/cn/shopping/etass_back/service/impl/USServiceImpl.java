package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.entity.US;
import cn.shopping.etass_back.mapper.USMapper;
import cn.shopping.etass_back.service.USService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class USServiceImpl extends ServiceImpl<USMapper, US> implements USService {

    @Autowired
    USMapper mapper;

    @Override
    public void addS(String username) {
        Field Zr = DoublePairing.Zr;
        Element s = Zr.newRandomElement().getImmutable();
        US us = new US();
        us.setS(s.toBytes());
        us.setUsername(username);
        mapper.insert(us);
    }

    @Override
    public US getS(String username) {
        return mapper.selectById(username);
    }
}
