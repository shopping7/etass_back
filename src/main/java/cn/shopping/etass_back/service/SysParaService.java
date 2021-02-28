package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.SysPara;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
public interface SysParaService extends IService<SysPara> {
    public void Setup();

    public SysPara getSysPara();
}
