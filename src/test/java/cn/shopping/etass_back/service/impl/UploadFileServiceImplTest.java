package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.config.lsss.LSSSEngine;
import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.service.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UploadFileServiceImplTest {

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    UlService ulService;

    @Autowired
    TransformService transformService;

    @Autowired
    UserKeyService userKeyService;

    @Autowired
    UserAttrService userAttrService;

    @Autowired
    TrapdoorService trapdoorService;

    @Autowired
    SysParaService sysParaService;

    @Autowired
    USService usService;

    @Test
    public void enc(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        SysPara sysPara = sysParaService.getSysPara();
        Serial serial = new Serial();
        PP pp = (PP)serial.deserial(sysPara.getPp());
        UserKey userKey = userKeyService.getUserKey("zhangsan");
        byte[] sk_b = userKey.getSk();
        SK sk = (SK)serial.deserial(sk_b);
        String policy = "hospital&doctor&(headache|(flu&heart))";
        LSSSEngine engine = new LSSSEngine();
        LSSSMatrix lsss = engine.genMatrix(policy);
        File file = new File("C:\\Users\\shopping3\\Downloads\\12.txt");
        US us = usService.getS("zhangsan");
        String[] kw = {"123"};
        uploadFileService.Enc(pp,sk,us,file,policy,kw);
    }

    @Test
    public void getFile(){
        String[] kw = {"123","456"};
        List<UploadFile> file = uploadFileService.getFile(kw);
        System.out.println(file.size());
    }
    
    @Test
    public void dec(){
        DoublePairing doublePairing = new DoublePairing();
        doublePairing.getStart();
        Field GT = DoublePairing.GT;
        SysPara sysPara = sysParaService.getSysPara();
        Serial serial = new Serial();
        PP pp = (PP)serial.deserial(sysPara.getPp());
        UserKey userKey = userKeyService.getUserKey("zhangsan");
        SK sk = (SK)serial.deserial(userKey.getSk());
        String theta = userKey.getTheta();
        List<String> userAttr = userAttrService.getUserAttr("zhangsan");
        String[] attrs = userAttr.toArray(new String[userAttr.size()]);
        Ul ul = ulService.getDid(theta);
        String[] kw = {"789"};
        TKW tkw = trapdoorService.Trapdoor(pp, sk, kw);
        List<UploadFile> fileList = uploadFileService.getFile(kw);
        for(UploadFile key_file : fileList){
            CT ct = (CT)serial.deserial(key_file.getCt());
            LSSSMatrix lsss = (LSSSMatrix) serial.deserial(key_file.getLsss());
            LSSSMatrix lsssD1 = lsss.extract(attrs);
            int lsssIndex[] = lsssD1.getIndex();
            CTout ctout = transformService.Transform(ct, tkw, ul.getDid(),lsssD1,lsssIndex);
            VKM vkm = (VKM)serial.deserial(key_file.getVkm());
            if(ctout != null) {
                    byte[] dec = trapdoorService.Dec(ctout, sk, vkm);
                    System.out.println(dec);
                    String cm = new String(dec);
                    System.out.println(cm);
                }
            }
    }
}