package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.SysPara;
import cn.shopping.etass_back.entity.UserKey;
import cn.shopping.etass_back.service.SysParaService;
import cn.shopping.etass_back.service.UserAttrService;
import cn.shopping.etass_back.service.UserKeyService;
import com.baomidou.mybatisplus.annotation.TableId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserKeyServiceImplTest {

    @Autowired
    UserAttrService userAttrService;

    @Autowired
    SysParaService sysParaService;

    @Autowired
    UserKeyService userKeyService;

    @Test
    public void KeyGen(){
        SysPara sysPara = sysParaService.getSysPara();
        byte[] pp_b = sysPara.getPp();
        byte[] msk_b = sysPara.getMsk();
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        Serial serial = new Serial();
        PP pp = (PP) serial.deserial(pp_b);
        MSK msk = (MSK)serial.deserial(msk_b);
        String username = "zhangsan";
        List<String> userAttrList = userAttrService.getUserAttr(username);
        String[] userAttr = userAttrList.toArray(new String[userAttrList.size()]);
        System.out.println(userAttr);
        userKeyService.KeyGen(pp,msk,username,userAttr,true);
    }

    @Test
    public void get(){
        String username = "zhangsan";
        List<String> userAttrList = userAttrService.getUserAttr(username);
        String[] userAttr = userAttrList.toArray(new String[userAttrList.size()]);
        for (int i = 0; i < userAttr.length; i++) {
            System.out.println(userAttr[i]);
        }
    }
}