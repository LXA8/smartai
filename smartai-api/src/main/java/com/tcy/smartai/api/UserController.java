package com.tcy.smartai.api;

import com.tcy.smartai.service.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/smartai")
public class UserController {

    public static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserDao userDao;
    "寻寻觅觅"
    @RequestMapping("/login")
    @ResponseBody
    public Map login() {
        logger.info("用户登录,user:{}", "yonghuhsda");
        Map result = new HashMap();
        result.put("returncode", "0");
        result.put("errormsg", "ok");

        return result;
    }
}
