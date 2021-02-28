package cn.shopping.etass_back.service;

import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.entity.CT;
import cn.shopping.etass_back.entity.CTout;
import cn.shopping.etass_back.entity.TKW;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.stereotype.Service;


public interface TransformService {

    public CTout Transform(CT ct, TKW tkw, byte[] Did, LSSSMatrix lsssD1, int lsssIndex[]);

}
