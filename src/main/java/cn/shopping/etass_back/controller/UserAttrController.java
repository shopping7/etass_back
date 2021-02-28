package cn.shopping.etass_back.controller;


import cn.shopping.etass_back.domain.ApiResult;
import cn.shopping.etass_back.domain.ApiResultUtil;
import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.service.UserAttrService;
import cn.shopping.etass_back.service.UserKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-26
 */
@RestController
public class UserAttrController {
    @Autowired
    UserAttrService userAttrService;

    @Autowired
    UserKeyService userKeyService;


    @RequestMapping("/userAttrEdit")
    public ApiResult userAttrEdit(@RequestBody List<String> attrs, HttpServletRequest request){
        String username = request.getParameter("username");
        System.out.println(username);
        System.out.println(attrs);
        userAttrService.DeleteUserAttr(username);
        for(String attr : attrs){
            userAttrService.addUserAttr(username,attr);
        }

        List<String> userAttrList = userAttrService.getUserAttr(username);
        String[] userAttr = userAttrList.toArray(new String[userAttrList.size()]);
        HttpSession session = request.getSession();
        PP pp = (PP)session.getAttribute("pp");
        MSK msk = (MSK)session.getAttribute("msk");
        userKeyService.KeyGen(pp, msk, username,userAttr,false);
        return ApiResultUtil.success();
    }
}
