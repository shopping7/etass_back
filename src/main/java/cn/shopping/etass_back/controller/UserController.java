package cn.shopping.etass_back.controller;


import cn.shopping.etass_back.domain.ApiResult;
import cn.shopping.etass_back.domain.ApiResultUtil;
import cn.shopping.etass_back.domain.UserVO;
import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.User;
import cn.shopping.etass_back.service.UserAttrService;
import cn.shopping.etass_back.service.UserKeyService;
import cn.shopping.etass_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 公众号：java思维导图
 * @since 2021-02-25
 */
@RestController
public class UserController {
    @Autowired
    UserAttrService userAttrService;

    @Autowired
    UserService userService;

    @Autowired
    UserKeyService userKeyService;

    @RequestMapping("/users")
    public ApiResult getAllUsers(HttpServletRequest request, HttpServletResponse response){
        List<UserVO> allUsers = userService.getAllUsers();
        return ApiResultUtil.successReturn(allUsers);
    }

    @RequestMapping("/addUser")
    public ApiResult addUser(@RequestBody UserVO user, HttpServletRequest request){
        String username = user.getUsername();
        List<String> user_attr = user.getAttr();
        HttpSession session = request.getSession();
        PP pp = (PP)session.getAttribute("pp");
        MSK msk = (MSK)session.getAttribute("msk");
        User newUser = new User();
        if(username !=null && user_attr != null){
            newUser.setUsername(username);
//            userService.addUser(newUser);
//            for(String attr : user_attr){
//                userAttrService.addUserAttr(username,attr);
//            }
        }
        String[] userAttr = user_attr.toArray(new String[user_attr.size()]);
        userKeyService.KeyGen(pp, msk, username, userAttr,true);
        return ApiResultUtil.success();
    }

    @RequestMapping("/deleteUser")
    public ApiResult deleteUser(@RequestBody UserVO user, HttpServletRequest request){
        String username = user.getUsername();
        HttpSession session = request.getSession();

        return ApiResultUtil.success();
    }
}
