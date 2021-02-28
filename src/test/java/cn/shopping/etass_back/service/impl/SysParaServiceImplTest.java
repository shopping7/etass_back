package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.entity.SysPara;
import cn.shopping.etass_back.service.SysParaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysParaServiceImplTest {

    @Autowired
    SysParaService sysParaService;

    @Test
    public void SysPara(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        sysParaService.Setup();
        SysPara sysPara = sysParaService.getSysPara();
        System.out.println(sysPara);
    }
}