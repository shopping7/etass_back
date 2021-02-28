package cn.shopping.etass_back.service.impl;

import cn.shopping.etass_back.config.*;
import cn.shopping.etass_back.config.lsss.LSSSEngine;
import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.config.lsss.LSSSShares;
import cn.shopping.etass_back.config.lsss.Vector;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.mapper.FileKwMapper;
import cn.shopping.etass_back.mapper.UploadFileMapper;
import cn.shopping.etass_back.service.UploadFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.math.BigInteger;
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
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements UploadFileService {

    @Autowired
    UploadFileMapper mapper;

    @Autowired
    FileKwMapper fileKwMapper;

    @Override
    public void Enc(PP pp, SK sk, US user_s, File file, String policy, String[] KW) {
        Field Zr = DoublePairing.Zr;
        Field GT = DoublePairing.GT;
        Field K = DoublePairing.K;
        Field G1 = DoublePairing.G1;
        Pairing pairing = DoublePairing.pairing;

        Element g = G1.newElementFromBytes(pp.getG()).getImmutable();
        Element g_lambda = G1.newElementFromBytes(pp.getG_lambda()).getImmutable();
        Element Y = GT.newElementFromBytes(pp.getY()).getImmutable();
        Element g_beta = G1.newElementFromBytes(pp.getG_beta()).getImmutable();
        Element f = G1.newElementFromBytes(pp.getF()).getImmutable();

        LSSSEngine engine = new LSSSEngine();
        LSSSMatrix lsss = engine.genMatrix(policy);

        int l1 = KW.length;
        String attributes[] = lsss.getMap();

        Element s = Zr.newElementFromBytes(user_s.getS()).getImmutable();
        Element[] Yn2 = new Element[lsss.getMartix().getCols()];//  get zr secret share matrix
        Yn2[0] = s;
        for(int i = 1 ; i < Yn2.length; i++)
        {
            Yn2[i] = Zr.newRandomElement().getImmutable();
        }
        Vector secret2 = new Vector(false, Yn2);
        LSSSShares shares2 = lsss.genShareVector2(secret2);
//        （2）加密密文
        Element gamma = GT.newRandomElement().getImmutable();
        String kse_t = DigestUtils.md5DigestAsHex(gamma.toBytes());
        Element kse = K.newElementFromBytes(kse_t.getBytes()).getImmutable();
        byte[] CM = new byte[0];
        try {
            CM = CrytptpFile.encrypt(file, kse.toBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //（3）计算检验密钥
        String vkm_t = gamma.toString() + Base64.getEncoder().encodeToString(CM);
        String VKM_t = DigestUtils.md5DigestAsHex(vkm_t.getBytes());
        Element VKM = G1.newElementFromHash(VKM_t.getBytes(),0,VKM_t.length()).getImmutable();

        //（4）多项式
        Element[] H_kw = new Element[l1];
        Element[][] hash_kw = new Element[l1][l1];
        Element[] hash_b = new Element[l1];

        for (int i = 0; i < l1; i++) {
            String word = DigestUtils.md5DigestAsHex(KW[i].getBytes());
            H_kw[i] = Zr.newElementFromHash(word.getBytes(), 0, word.length()).getImmutable();
            for (int j = 0; j < l1; j++) {
                hash_kw[i][j] = H_kw[i].pow(BigInteger.valueOf(l1-j-1)).getImmutable();
            }
            hash_b[i] = Zr.newOneElement().getImmutable();
        }

        PolynomialSoluter ps = new PolynomialSoluter();
        Element nj[] = ps.getResult(hash_kw, hash_b);


        Element sigma1 = Zr.newRandomElement().getImmutable();
        Element C = gamma.mul(Y.powZn(s)).getImmutable();
        Element C0 = g.powZn(s).getImmutable();
        Element C0_1 = g_lambda.powZn(s).getImmutable();
        Element C0_11 = g.powZn(sigma1.square()).getImmutable();


        //pi后续再处理一下
        Element[] pi = new Element[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            String md5 = DigestUtils.md5DigestAsHex(attributes[i].getBytes());
            pi[i] = G1.newElementFromHash(md5.getBytes(), 0, md5.length()).getImmutable();
        }

        Element[] Ci = new Element[lsss.getMartix().getRows()];
        Element[]  lambda_i = new Element[Ci.length];
        byte[][] Ci_bytes = new byte[Ci.length][];
        for (int i = 0; i < Ci.length; i++) {
            lambda_i[i] = shares2.getVector().getValue2(i);
            Ci[i] = (g_beta.powZn(lambda_i[i])).mul(pi[i].powZn(sigma1.negate())).getImmutable();
            Ci_bytes[i] = Ci[i].toBytes();
        }
        byte[][] Cj_bytes = new byte[l1][];
        Element[] Cj = new Element[l1];
        for (int i = 0; i < l1; i++) {
            Cj[i] = (sigma1.invert()).mul(nj[l1-i-1]).getImmutable();
            Cj_bytes[i] = Cj[i].toBytes();
        }

        Element E = pairing.pairing(g, f).powZn(sigma1).getImmutable();
        E = E.mul(Y.powZn(s.mulZn(sigma1)));

        CT ct = new CT();
        ct.setC(C.toBytes());
        ct.setC0(C0.toBytes());
        ct.setC0_1(C0_1.toBytes());
        ct.setC0_11(C0_11.toBytes());
        ct.setE(E.toBytes());
        ct.setCM(CM);
        ct.setCi(Ci_bytes);
        ct.setCj(Cj_bytes);

        VKM vkm = new VKM();
        vkm.setVKM(VKM.toBytes());

        Serial serial = new Serial();
        byte[] ct_b = serial.serial(ct);
        byte[] vkm_b = serial.serial(vkm);
        byte[] lsss_b = serial.serial(lsss);

        UploadFile uploadFile = new UploadFile();
        uploadFile.setCt(ct_b);
        uploadFile.setVkm(vkm_b);
        uploadFile.setLsss(lsss_b);
        mapper.insertFile(uploadFile);
        int id= uploadFile.getId();

        for (int i = 0; i < KW.length; i++) {
            fileKwMapper.insertFileKW(id,KW[i]);
        }
    }

    @Override
    public List<UploadFile> getFile(String[] KW) {
        List<Integer> fileId = mapper.getFileId(KW);
        return mapper.getFile(fileId);
    }
}
