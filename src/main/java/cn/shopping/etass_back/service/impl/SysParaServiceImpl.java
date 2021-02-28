package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.DoublePairing;
import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.SysPara;
import cn.shopping.etass_back.mapper.SysParaMapper;
import cn.shopping.etass_back.service.SysParaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@Service
public class SysParaServiceImpl extends ServiceImpl<SysParaMapper, SysPara> implements SysParaService {

    @Autowired
    SysParaMapper sysParaMapper;

    @Override
    public void Setup() {
        Pairing pairing = DoublePairing.pairing;
        Field G1 = DoublePairing.G1;
        Field Zr = DoublePairing.Zr;
        Field K = DoublePairing.K;

        Element alpha = Zr.newRandomElement().getImmutable();
        Element beta = Zr.newRandomElement().getImmutable();
        Element lambda = Zr.newRandomElement().getImmutable();

        Element f = G1.newRandomElement().getImmutable();
        Element k1 = K.newRandomElement().getImmutable();
        Element k2 = K.newRandomElement().getImmutable();

        Element g = G1.newRandomElement().getImmutable();

        Element Y = pairing.pairing(g, g).powZn(alpha).getImmutable();

        MSK msk = new MSK();
        msk.setAlpha(alpha.toBytes());
        msk.setBeta(beta.toBytes());
        msk.setLambda(lambda.toBytes());
        msk.setK1(k1.toBytes());
        msk.setK2(k2.toBytes());

        PP pp = new PP();
        pp.setF(f.toBytes());
        pp.setG(g.toBytes());
        pp.setG_beta((g.powZn(beta)).toBytes());
        pp.setG_lambda((g.powZn(lambda)).toBytes());
        pp.setY(Y.toBytes());

        Serial serial = new Serial();
        byte[] msk_b = serial.serial(msk);
        byte[] pp_b = serial.serial(pp);

        SysPara sysPara = new SysPara();
        sysPara.setPp(pp_b);
        sysPara.setMsk(msk_b);

        sysParaMapper.insert(sysPara);
    }

    @Override
    public SysPara getSysPara() {
        SysPara sysPara = sysParaMapper.selectOne(null);
        return sysPara;
    }
}
