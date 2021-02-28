package cn.shopping.etass_back.entity;

import lombok.Data;

import java.io.Serializable;


@Data
public class PP implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] f;

    private byte[] g;

    private byte[] g_beta;

    private byte[] g_lambda;

    private byte[] Y;
}
