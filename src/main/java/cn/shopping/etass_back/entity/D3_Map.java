package cn.shopping.etass_back.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class D3_Map implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] D3;
    private String attr;
}
