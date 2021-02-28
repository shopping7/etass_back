package cn.shopping.etass_back.service;

import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.entity.*;
import it.unisa.dia.gas.jpbc.Element;

public interface TrapdoorService {

    public TKW Trapdoor(PP pp, SK sk, String[] KW);

    public byte[] Dec(CTout CTout, SK sk, VKM vkm);
}
