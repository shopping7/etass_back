package cn.shopping.etass_back.entity;

import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    @TableField("CT")
    private byte[] ct;

    @TableField("VKM")
    private byte[] vkm;

    private byte[] lsss;


}
