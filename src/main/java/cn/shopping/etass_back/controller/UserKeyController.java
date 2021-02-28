package cn.shopping.etass_back.controller;


import cn.shopping.etass_back.domain.ApiResult;
import cn.shopping.etass_back.domain.ApiResultUtil;
import cn.shopping.etass_back.entity.MSK;
import cn.shopping.etass_back.entity.PK;
import cn.shopping.etass_back.entity.PP;
import cn.shopping.etass_back.entity.SK;
import cn.shopping.etass_back.service.SysParaService;
import cn.shopping.etass_back.service.UserAttrService;
import cn.shopping.etass_back.service.UserKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
public class UserKeyController {
    @Autowired
    UserKeyService userKeyService;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @RequestMapping(value = "/user/getPK")
    public ApiResult getPK(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        PK pk = (PK)session.getAttribute("pk");
        System.out.println(pk);
        String format = sdf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/")+format;
//        System.out.println(realPath);
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = "pk"+ UUID.randomUUID().toString() + ".txt";
        System.out.println(realPath+newName);
        File sk_file = new File(folder,newName);
        String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+format+"/"+newName;
        System.out.println(url);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sk_file));
            os.writeObject(pk);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResultUtil.successReturn(url);
    }

    @RequestMapping("/user/getSK")
    public ApiResult getSK(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        SK sk = (SK)session.getAttribute("sk");
        System.out.println(sk);
        String format = sdf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/")+format;
//        System.out.println(realPath);
        File folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String newName = "sk"+UUID.randomUUID().toString() + ".txt";
        System.out.println(realPath+newName);
        File sk_file = new File(folder,newName);
        String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+format+"/"+newName;
        System.out.println(url);
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sk_file));
            os.writeObject(sk);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResultUtil.successReturn(url);
    }

    @RequestMapping("/keyGen")
    public ApiResult KeyGen(HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        PP pp = (PP)session.getAttribute("pp");
        MSK msk = (MSK)session.getAttribute("msk");

//        userKeyService.KeyGen();
        return ApiResultUtil.success();
    }
}
