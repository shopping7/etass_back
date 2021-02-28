package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.service.SysParaService;
import cn.shopping.etass_back.service.UlService;
import cn.shopping.etass_back.service.UserKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UlServiceImplTest {

    @Autowired
    SysParaService sysParaService;

    @Autowired
    UserKeyService userKeyService;

    @Autowired
    UlService ulService;

    @Test
    public void UL(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        SysPara sysPara = sysParaService.getSysPara();
        Serial serial = new Serial();
        MSK msk = (MSK) serial.deserial(sysPara.getMsk());
        UserKey userKey = userKeyService.getUserKey("zhangsan");
        PK pk = (PK)serial.deserial(userKey.getPk());
        ulService.addUL(msk, pk, "zhangsan");
    }
}