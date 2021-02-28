package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.Crytpto;
import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.mapper.UserKeyMapper;
import cn.shopping.etass_back.service.UserKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Base64;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@Service
public class UserKeyServiceImpl extends ServiceImpl<UserKeyMapper, UserKey> implements UserKeyService {

    @Autowired
    UserKeyMapper mapper;

    @Override
    public void KeyGen(PP pp, MSK msk, String username, String[] attributes, boolean isNew) {
        Field G1 = DoublePairing.G1;
        Field GT = DoublePairing.GT;
        Field K = DoublePairing.K;
        Field Zr = DoublePairing.Zr;

        Element g = G1.newElementFromBytes(pp.getG()).getImmutable();
        Element Y = GT.newElementFromBytes(pp.getY()).getImmutable();
        Element alpha = Zr.newElementFromBytes(msk.getAlpha()).getImmutable();
        Element beta = Zr.newElementFromBytes(msk.getBeta()).getImmutable();
        Element lambda = Zr.newElementFromBytes(msk.getLambda()).getImmutable();
        Element k1 = K.newElementFromBytes(msk.getK1()).getImmutable();
        Element k2 = K.newElementFromBytes(msk.getK2()).getImmutable();

        Element t = Zr.newRandomElement().getImmutable();
        Element kappa = Zr.newRandomElement().getImmutable();
        Element xid = Zr.newRandomElement().getImmutable();
        Element Yid = Y.powZn(xid).getImmutable().getImmutable();

        //计算每个用户的theta
        byte[] theta_id = Crytpto.SEnc(username.getBytes(), k1.toBytes());
        //计算每个用户的zeta
        String encoded = Base64.getEncoder().encodeToString(theta_id);
        String zeta_t1 = encoded + kappa.toString();
        byte[] zeta_t2 = Crytpto.SEnc(zeta_t1.getBytes(), k2.toBytes());
        String zeta_s = Base64.getEncoder().encodeToString(zeta_t2);
        Element zeta= Zr.newElementFromBytes(zeta_t2).getImmutable();

        Element D1 = g.powZn(alpha.mulZn((lambda.add(zeta)).invert())).getImmutable();
        D1 = D1.mul(g.powZn(beta.mul(t))).getImmutable();
        Element D1_1 = zeta.getImmutable().getImmutable();
        Element D2 = g.powZn(t).getImmutable().getImmutable();
        Element D2_1 = g.powZn(lambda.mulZn(t)).getImmutable();
        D3_Map[] D3 = new D3_Map[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            Element pi = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
            Element D3_t = pi.powZn((lambda.add(zeta)).mul(t)).getImmutable();
            D3_Map d3_t = new D3_Map();
            d3_t.setAttr(attributes[i]);
            d3_t.setD3(D3_t.toBytes());
            D3[i] = d3_t;
        }
        Element D4 = Zr.newRandomElement().getImmutable();

        SK sk = new SK();
        sk.setD1(D1.toBytes());
        sk.setD1_1(D1_1.toBytes());
        sk.setD2(D2.toBytes());
        sk.setD2_1(D2_1.toBytes());
        sk.setD3(D3);
        sk.setD4(D4.toBytes());
        sk.setXid(xid.toBytes());
        sk.setZeta(zeta_s);
        sk.setKappa(kappa.toBytes());

        PK pk = new PK();
        pk.setYid(Yid.toBytes());

        Serial serial = new Serial();
        byte[] pk_b = serial.serial(pk);
        byte[] sk_b = serial.serial(sk);

        UserKey userKey = new UserKey();
        userKey.setUsername(username);
        userKey.setPk(pk_b);
        userKey.setSk(sk_b);
        userKey.setTheta(encoded);

        if(isNew){
            mapper.insert(userKey);
        }else{
            mapper.updateById(userKey);
        }
    }

    @Override
    public UserKey getUserKey(String username) {
        return mapper.selectById(username);
    }
}
