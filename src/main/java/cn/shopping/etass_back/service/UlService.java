package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.omg.CORBA.ULongLongSeqHelper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface UlService extends IService<Ul> {

    public void addUL(MSK msk, PK pk, String username);

    public Ul getDid(String theta);

    public String Trace(PP pp, MSK msk, SK sk);

    public void UserRevo(String username, MSK msk);

}
