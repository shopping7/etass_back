package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.config.lsss.Vector;
import cn.shopping.etass_back.entity.CT;
import cn.shopping.etass_back.entity.CTout;
import cn.shopping.etass_back.entity.T3_Map;
import cn.shopping.etass_back.entity.TKW;
import cn.shopping.etass_back.service.TransformService;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.springframework.stereotype.Service;

@Service
public class TransformServiceImpl implements TransformService {

    @Override
    public CTout Transform(CT ct, TKW tkw, byte[] Did_b, LSSSMatrix lsssD1, int[] lsssIndex) {
        Field G1 = DoublePairing.G1;
        Field GT = DoublePairing.GT;
        Field Zr = DoublePairing.Zr;
        Pairing pairing = DoublePairing.pairing;
        if(Did_b == null){
            return null;
        }
        Element Did = GT.newElementFromBytes(Did_b);
        //计算秘密值sss
        Vector w = lsssD1.getRv();
        if(w == null){
            return null;
        }

        Element T1 = tkw.getT1().getImmutable();
        Element T1_1 = tkw.getT1_1().getImmutable();
        Element T2 = tkw.getT2().getImmutable();
        Element T2_1 = tkw.getT2_1().getImmutable();
        T3_Map[] T3_t = tkw.getT3();
        Element T4 = tkw.getT4().getImmutable();
        Element T5 = tkw.getT5().getImmutable();
        Element[] T6 = tkw.getT6();

        Element C0 = G1.newElementFromBytes(ct.getC0()).getImmutable();
        Element C0_1 = G1.newElementFromBytes(ct.getC0_1()).getImmutable();
        Element C0_11 = G1.newElementFromBytes(ct.getC0_11()).getImmutable();
        byte[][] ci = ct.getCi();
        Element Ci[] = new Element[ci.length];
        for (int i = 0; i < Ci.length; i++) {
            Ci[i] = G1.newElementFromBytes(ci[i]).getImmutable();
        }
        byte[][] cj = ct.getCj();
        Element Cj[] = new Element[cj.length];
        for (int i = 0; i < Cj.length; i++) {
            Cj[i] = Zr.newElementFromBytes(cj[i]).getImmutable();
        }
        int l1 = Cj.length;

        Element E = GT.newElementFromBytes(ct.getE()).getImmutable();

        Element L = pairing.pairing(T1,(C0.powZn(T1_1)).mul(C0_1)).getImmutable();

        String[] user_file_attr = lsssD1.getMap();
        Element temp3 = G1.newOneElement();
        int k = 0;
        for (int i = 0; i < T3_t.length; i++) {
            for (int j = 0; j < user_file_attr.length; j++) {
                if(T3_t[i].getAttr().equals(user_file_attr[j])){
                    temp3 = temp3.mul(T3_t[i].getT3().powZn(Zr.newElement(w.getValue(k))).getImmutable());
                    k++;
                }
            }

        }
        //注意T3的取值
        Element temp1 =(T2.powZn(T1_1)).mul(T2_1).getImmutable();
        Element temp2 = Ci[lsssIndex[0]].powZn(Zr.newElement(w.getValue(0))).getImmutable();
//        Element temp3 = T3[0].powZn(Zr.newElement(w.getValue(0))).getImmutable();
//        Element temp3_t = T3_t[0].getT3().powZn(Zr.newElement(w.getValue(0))).getImmutable();
        Element temp4 = Cj[0].mul(T6[0]).getImmutable().getImmutable();


        for (int i = 1; i < lsssD1.getMartix().getRows(); i++) {
            Element t = Zr.newElement(w.getValue(i)).getImmutable();
            temp2 = temp2.mul(Ci[lsssIndex[i]].powZn(t));
//            temp3 = temp3.mul(T3[i].powZn(t));

        }

        for (int i = 1; i < l1; i++) {
            temp4 = temp4.add(Cj[i].mul(T6[i]));
        }


        Element V = pairing.pairing(temp1, temp2).mul((pairing.pairing(C0_11,temp3)).powZn(temp4)).getImmutable();

        Element temp5 = (E.powZn(T4.mul(temp4))).mul(Did).getImmutable();

        Element temp6 = T5.mul(L.sub(V)).getImmutable();

        if(temp6.equals(temp5)){
            Element C = GT.newElementFromBytes(ct.getC()).getImmutable();
            byte[] CM = ct.getCM();
            CTout ctout = new CTout();
            ctout.setC(C);
            ctout.setL(L);
            ctout.setV(V);
            ctout.setCM(CM);
            return ctout;
        }else{
            System.out.println("您的权限不够，无法获得此文件");
            return null;
        }
    }
}
