package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.SK;
import cn.shopping.etass_back.entity.SysPara;
import cn.shopping.etass_back.entity.UserKey;
import cn.shopping.etass_back.service.SysParaService;
import cn.shopping.etass_back.service.UploadFileService;
import cn.shopping.etass_back.service.UserKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrapdoorServiceImplTest {

    @Autowired
    SysParaService sysParaService;

    //    @Autowired
//    User userService;

//    @Autowired
//    TransformService transformService;

    @Autowired
    UserKeyService userKeyService;

    @Autowired

    //    @Autowired
//    UserAttrService userAttrService;
//
//    @Autowired
//    TrapdoorService trapdoorService;
    @Test
    public void getFile(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        SysPara sysPara = sysParaService.getSysPara();
        Serial serial = new Serial();
        PP pp = (PP)serial.deserial(sysPara.getPp());
        UserKey userKey = userKeyService.getUserKey("zhangsan");
        byte[] sk_b = userKey.getSk();
        SK sk = (SK)serial.deserial(sk_b);
    }
}