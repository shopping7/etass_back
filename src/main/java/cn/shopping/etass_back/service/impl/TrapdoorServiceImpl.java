package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.CrytptpFile;
import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.config.lsss.Vector;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.service.TrapdoorService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.util.Base64;

@Service
public class TrapdoorServiceImpl implements TrapdoorService {

    private Element u;

    @Override
    public TKW Trapdoor(PP pp, SK sk, String[] KW) {
        Pairing pairing = DoublePairing.pairing;
        Field G1 = DoublePairing.G1;
        Field Zr = DoublePairing.Zr;
        Element g = G1.newElementFromBytes(pp.getG()).getImmutable();
        Element f = G1.newElementFromBytes(pp.getF()).getImmutable();
        u = Zr.newRandomElement().getImmutable();
        Element sigma2 = Zr.newRandomElement().getImmutable();


        Element D1 = G1.newElementFromBytes(sk.getD1()).getImmutable();
        Element D1_1 = Zr.newElementFromBytes(sk.getD1_1()).getImmutable();
        Element D2 = G1.newElementFromBytes(sk.getD2()).getImmutable();
        Element D2_1 = G1.newElementFromBytes(sk.getD2_1()).getImmutable();
        D3_Map[] D3 = sk.getD3();
        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();
        Element xid = Zr.newElementFromBytes(sk.getXid()).getImmutable();
        //  计算TKW
        Element T1 = D1.powZn(u.mulZn(D4)).getImmutable();
        Element T1_1 = D1_1.getImmutable().getImmutable();
        Element T2 = D2.powZn(u.mulZn(D4)).getImmutable();
        Element T2_1 = D2_1.powZn(u.mulZn(D4)).getImmutable();
        int l2 = KW.length;
        Element l2_E = Zr.newElement(l2).getImmutable();
        T3_Map[] T3 = new T3_Map[D3.length];
        for (int i = 0; i < D3.length; i++) {
            Element D3_t = G1.newElementFromBytes(D3[i].getD3()).getImmutable();
            Element T3_t1 = D3_t.powZn((u.mulZn(D4).mulZn(sigma2)).mul(l2_E.invert())).getImmutable();
            T3_Map T3_t = new T3_Map();
            T3_t.setAttr(D3[i].getAttr());
            T3_t.setT3(T3_t1);
            T3[i] = T3_t;
        }

        Element T4 = (u.mulZn(D4).sub(xid)).mul(sigma2).mul(l2_E.invert()).getImmutable();
        Element T5 = (pairing.pairing(g, f)).powZn(u.mulZn(D4).sub(xid)).getImmutable();

        String kw_u;
        Element[] kw_us = new Element[l2];
        for (int i = 0; i < l2; i++) {
            kw_u= DigestUtils.md5DigestAsHex(KW[i].getBytes());
            kw_us[i] = Zr.newElementFromHash(kw_u.getBytes(),0,kw_u.length()).getImmutable();
        }
        //这里不明白怎么获得l1
        int l1 = l2;
        Element[] T6 = new Element[l1];
        for (int i = 0; i < l1; i++) {
            Element sum = Zr.newZeroElement().getImmutable();
            for (int j = 0; j < l2; j++) {
                sum = sum.add(kw_us[i].pow(BigInteger.valueOf(i))).getImmutable();
            }
            T6[i] = (sigma2.invert()).mul(sum).getImmutable();
        }


        TKW tkw = new TKW();
        tkw.setT1(T1);
        tkw.setT1_1(T1_1);
        tkw.setT2(T2);
        tkw.setT2_1(T2_1);
        tkw.setT3(T3);
        tkw.setT4(T4);
        tkw.setT5(T5);
        tkw.setT6(T6);

        return tkw;
    }



    @Override
    public byte[] Dec(CTout CTout, SK sk, VKM vkm) {
        Field G1 = DoublePairing.G1;
        Field Zr = DoublePairing.Zr;
        Field K = DoublePairing.K;
        if(CTout == null&& u == null){
            return null;
        }

        Element C = CTout.getC().getImmutable();
        Element L = CTout.getL().getImmutable();
        Element V = CTout.getV().getImmutable();
        byte[] CM = CTout.getCM();

        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();

        Element gamma_verify = C.sub((L.sub(V)).powZn((u.mulZn(D4)).invert())).getImmutable();
        String vkm_t = gamma_verify.toString() + Base64.getEncoder().encodeToString(CM);
        String VKM_T = DigestUtils.md5DigestAsHex(vkm_t.getBytes());
        Element VKM_verify = G1.newElementFromHash(VKM_T.getBytes(),0,VKM_T.length()).getImmutable();
        String kse_v = DigestUtils.md5DigestAsHex(gamma_verify.toBytes());
        Element kse_verify = K.newElementFromBytes(kse_v.getBytes()).getImmutable();
//            byte[] m = Crytpto.SDec(CM,kse_verify.toBytes());

        try {
            byte[] bytes = CrytptpFile.SDec(CM, kse_verify.toBytes());
            System.out.println("解密成功");
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
