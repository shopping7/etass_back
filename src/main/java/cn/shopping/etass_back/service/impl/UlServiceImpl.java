package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.Crytpto;
import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.mapper.USMapper;
import cn.shopping.etass_back.mapper.UlMapper;
import cn.shopping.etass_back.service.UlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.omg.CORBA.ULongLongSeqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@Service
public class UlServiceImpl extends ServiceImpl<UlMapper, Ul> implements UlService {

    @Autowired
    UlMapper ulMapper;

    @Autowired
    USMapper usMapper;

    @Override
    public void addUL(MSK msk, PK pk, String username) {
        Field GT = DoublePairing.GT;
        Field K = DoublePairing.K;
        Field Zr = DoublePairing.Zr;
        US us = usMapper.selectById(username);
        byte[] s_b = us.getS();
        Element s = Zr.newElementFromBytes(s_b).getImmutable();

        Element k1 = K.newElementFromBytes(msk.getK1()).getImmutable();
        byte[] theta_id = Crytpto.SEnc(username.getBytes(), k1.toBytes());
        String theta = Base64.getEncoder().encodeToString(theta_id);

        Element Yid = GT.newElementFromBytes(pk.getYid()).getImmutable();
        Element Did = Yid.powZn(s).getImmutable();
        Ul ul = new Ul();
        ul.setTheta(theta);
        ul.setDid(Did.toBytes());
        ulMapper.insert(ul);
    }

    @Override
    public Ul getDid(String theta) {
        return ulMapper.selectById(theta);
    }

    @Override
    public String Trace(PP pp, MSK msk, SK sk) {
        Field G1 = DoublePairing.G1;
        Field Zr = DoublePairing.Zr;
        Field GT = DoublePairing.GT;
        Field K = DoublePairing.K;
        Pairing pairing = DoublePairing.pairing;

        Element g = G1.newElementFromBytes(pp.getG());
        Element Y = GT.newElementFromBytes(pp.getY());
        Element lambda = Zr.newElementFromBytes(msk.getLambda());
        Element beta = Zr.newElementFromBytes(msk.getBeta());
        Element k1 = K.newElementFromBytes(msk.getK1());
        Element k2 = K.newElementFromBytes(msk.getK2());

        Element D1 = G1.newElementFromBytes(sk.getD1()).getImmutable();
        Element D1_1 = Zr.newElementFromBytes(sk.getD1_1()).getImmutable();
        Element D2 = G1.newElementFromBytes(sk.getD2()).getImmutable();
        Element D2_1 = G1.newElementFromBytes(sk.getD2_1()).getImmutable();
        D3_Map[] D3 = sk.getD3();
        Element D4 = Zr.newElementFromBytes(sk.getD4()).getImmutable();
        Element xid = Zr.newElementFromBytes(sk.getXid()).getImmutable();

        if(D1!=null&&D1_1!=null&&D2!=null&&D2_1!=null&&D3!=null&&D4!=null&&xid!=null){
            Element temp1 = pairing.pairing(g,D2_1).getImmutable();
            Element temp2 = pairing.pairing(g.powZn(lambda),D2).getImmutable();
            boolean b2 = temp1.equals(temp2);

            Element temp3 = pairing.pairing(g.powZn(lambda).mul(g.powZn(D1_1)),D1).getImmutable();
            Element temp4 = Y.mul(pairing.pairing(D2.powZn(D1_1).mul(D2_1),g.powZn(beta))).getImmutable();
            boolean b3 = temp3.equals(temp4);


            Element D3_sum = G1.newElementFromBytes(D3[0].getD3()).getImmutable().getImmutable();
            String md5 = DigestUtils.md5DigestAsHex(D3[0].getAttr().getBytes());
            Element pi_sum = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
            for (int i = 1; i < D3.length; i++) {
                String pi_t1 = DigestUtils.md5DigestAsHex(D3[i].getAttr().getBytes());
                Element pi_t2 = G1.newElementFromHash(pi_t1.getBytes(), 0, pi_t1.length()).getImmutable();
                pi_sum = pi_sum.mul(pi_t2);
                D3_sum = D3_sum.mul(G1.newElementFromBytes(D3[i].getD3()));
            }
            Element temp5 = pairing.pairing(pi_sum,(D2.powZn(D1_1)).mul(D2_1)).getImmutable();
            Element temp6 = pairing.pairing(g,D3_sum).getImmutable();
            boolean b4 = temp5.equals(temp6);
            if(b2 && b3 && b4){
                String zeta = sk.getZeta();
                Element kappa = Zr.newElementFromBytes(sk.getKappa());
                byte[] zeta_t2 = Base64.getDecoder().decode(zeta);
                byte[] theta_id_kappa = Crytpto.SDec(zeta_t2,k2.toBytes());
                String theta_id_kappa_s = new String(theta_id_kappa);
                String theta_t = theta_id_kappa_s.replace(kappa.toString(),"");
                byte[] decoded = Base64.getDecoder().decode(theta_t);
                byte[] id_t = Crytpto.SDec(decoded,k1.toBytes());
                String id_revo = new String(id_t);
                return id_revo;
            }else{
                System.out.println("追踪失败");
            }
        }else {
            System.out.println("密钥不完整");
        }
        return null;
    }

    @Override
    public void UserRevo(String username,MSK msk) {
        Field K = DoublePairing.K;
        Element k1 = K.newElementFromBytes(msk.getK1());
        byte[] theta_id = Crytpto.SEnc(username.getBytes(), k1.toBytes());
        Ul ul = new Ul();
        ul.setTheta(new String(theta_id));
        ul.setDid("false".getBytes());
    }

}
