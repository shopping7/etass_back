package cn.shopping.etass_back.service;

import cn.shopping.etass_back.entity.Attr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-28
 */
public interface AttrService extends IService<Attr> {
    public List<Attr> getAllAttr();

    public void deleteAttr(String attr);

    public void addAttr(Attr attr);

    public void editAttr(Attr attr);
}
