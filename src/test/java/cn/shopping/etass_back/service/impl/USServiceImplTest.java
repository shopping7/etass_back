package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.entity.US;
import cn.shopping.etass_back.service.USService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class USServiceImplTest {

    @Autowired
    USService usService;

    @Test
    public void userS(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        usService.addS("zhangsan");
        US zhangsan = usService.getS("zhangsan");
        System.out.println(zhangsan);
    }

}