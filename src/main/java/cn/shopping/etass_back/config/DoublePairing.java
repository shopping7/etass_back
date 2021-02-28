package cn.shopping.etass_back.config;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class DoublePairing {
   public static Pairing pairing;
   public static Field G1;
    public static Field GT;
    public static Field Zr;
    public static Field K;

   public void getStart(){
      pairing = PairingFactory.getPairing("a.properties");
      PairingFactory.getInstance().setUsePBCWhenPossible(true);
      if(!pairing.isSymmetric()){
         throw new RuntimeException("密钥不对称");
      }

      Zr = pairing.getZr();
      G1 = pairing.getG1();
      K = pairing.getG2();
      GT = pairing.getGT();
   }
}