package cn.shopping.etass_back.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class MSK implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] alpha;

    private byte[] beta;

    private byte[] lambda;

    private byte[] k1;

    private byte[] k2;
}
