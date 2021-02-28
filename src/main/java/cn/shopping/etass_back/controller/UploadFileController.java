package cn.shopping.etass_back.controller;


import cn.shopping.etass_back.config.Serial;
import cn.shopping.etass_back.config.lsss.LSSSEngine;
import cn.shopping.etass_back.config.lsss.LSSSMatrix;
import cn.shopping.etass_back.domain.ApiResult;
import cn.shopping.etass_back.domain.ApiResultUtil;
import cn.shopping.etass_back.domain.UserVO;
import cn.shopping.etass_back.entity.*;
import cn.shopping.etass_back.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
public class UploadFileController {

    @Autowired
    UlService ulService;

    @Autowired
    USService usService;

    @Autowired
    TrapdoorService trapdoorService;

    @Autowired
    TransformService transformService;

    @Autowired
    UserAttrService userAttrService;

    @Autowired
    UploadFileService uploadFileService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping("/user/uploadFile")
    public ApiResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String policy = request.getParameter("policy");
        System.out.println(policy);
        if (file.isEmpty()) {
            ApiResultUtil.errorAuthorized("无文件");
        }
        String[] keywords = request.getParameterValues("keywords");

        String format = sdf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/") + format;
        System.out.println(realPath);
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String newName = UUID.randomUUID().toString() + ".txt";
        System.out.println(newName);
        File upload_file = new File(folder, newName);
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + format + "/" + newName;
        System.out.println(url);
        try {
            file.transferTo(upload_file);
            HttpSession session = request.getSession();
            PP pp = (PP) session.getAttribute("pp");
            SK sk = (SK) session.getAttribute("sk");
            System.out.println(pp);
            System.out.println(sk);
            System.out.println(upload_file);
            US us = usService.getS("zhangsan");
            if ((pp != null) && (sk != null) && (upload_file != null)) {
                uploadFileService.Enc(pp, sk, us, upload_file, policy, keywords);
            }
            return ApiResultUtil.success();
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResultUtil.errorParam("上传失败");
        }
    }

    @RequestMapping("/user/getFile")
    public ApiResult getFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String[] keywords = request.getParameterValues("keywords");
        String format = sdf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/") + format;
        System.out.println(realPath);
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (file.isEmpty()) {
            ApiResultUtil.errorAuthorized("无文件");
        }
        InputStream in = file.getInputStream();
        ObjectInputStream is = new ObjectInputStream(in);

        HttpSession session = request.getSession();
        Serial serial = new Serial();
        List<String> fileUrl = new ArrayList<>();
        try {
            SK sk = (SK) is.readObject();
            System.out.println(sk);
            PP pp = (PP) session.getAttribute("pp");
            byte[] Did = (byte[])session.getAttribute("Did");
            UserVO loginUser = (UserVO) session.getAttribute("loginUser");
            List<String> userAttr = loginUser.getAttr();
            String[] attrs = userAttr.toArray(new String[userAttr.size()]);
            List<UploadFile> fileList = uploadFileService.getFile(keywords);
            System.out.println(fileList.size());
            if (fileList.size() > 0) {
                TKW tkw = trapdoorService.Trapdoor(pp, sk, keywords);
                for (UploadFile key_file : fileList) {
                    CT ct = (CT) serial.deserial(key_file.getCt());
                    VKM vkm = (VKM) serial.deserial(key_file.getVkm());
                    LSSSMatrix lsss = (LSSSMatrix) serial.deserial(key_file.getLsss());
                    LSSSMatrix lsssD1 = lsss.extract(attrs);
                    int lsssIndex[] = lsssD1.getIndex();
                    CTout ctout = transformService.Transform(ct, tkw, Did, lsssD1, lsssIndex);
                    int i=1;
                    if (ctout != null) {
                        String newName = UUID.randomUUID().toString() +i+ ".txt";
                        System.out.println(newName);
                        File sk_file = new File(folder, newName);
                        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + format + "/" + newName;
                        byte[] dec = trapdoorService.Dec(ctout, sk,vkm);
                        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sk_file));
                        os.writeObject(dec);
                        os.close();
                        fileUrl.add(url);
                    }
                }
            } else {
                return ApiResultUtil.errorParam("无匹配文件");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(fileUrl.size() > 0){
            return ApiResultUtil.successReturn(fileUrl);
        }else{
            return ApiResultUtil.errorParam("无权限解密");
        }

    }
}
