package com.tcy.smartai.api;

import com.alibaba.fastjson.JSON;
import com.tcy.smartai.service.common.CommonResponse;
import com.tcy.smartai.service.dao.UserDao;
import com.tcy.smartai.service.entity.CamerasEntity;
import com.tcy.smartai.service.entity.User;
import com.tcy.smartai.service.enums.ErrorCodeEnum;
import com.tcy.smartai.service.exception.BusinessException;
import com.tcy.smartai.service.vo.CameraVo;
import com.tcy.smartai.service.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestMapping("/login")
    @ResponseBody
    public Map login() {
        logger.info("用户登录,user:{}", "yonghuhsda");
        Map result = new HashMap();
        result.put("returncode", "0");
        result.put("errormsg", "ok");

        return result;
    }
    @PostMapping("/adduser")
    @ResponseBody
    @ApiOperation("添加新用户")
    public CommonResponse addCamera(@RequestBody UserVO userVo) {
        logger.info("添加用户,UserVo:{}", JSON.toJSONString(userVo));
        if(StringUtils.isEmpty(userVo)){
            logger.error("添加用户失败，入参为空");
            return CommonResponse.fail(ErrorCodeEnum.ILLEGAL_ARGUMENT_INAVLID);
        }
        try{
            User user = new User();
            BeanUtils.copyProperties(userVo,user);
            userDao.addUser(user);
        }catch (BusinessException e){
//            e.printStackTrace();
            logger.error("添加用户失败,cameraVo:{},code:{},msg:{}",JSON.toJSONString(userVo),e.getCode(),e);
            return CommonResponse.fail(e);
        }catch (Exception e1){
//            e1.printStackTrace();
            logger.error("添加用户失败,cameraVo:{},msg:{}",JSON.toJSONString(userVo),e1);
            return CommonResponse.fail(ErrorCodeEnum.SERVER_CODE_ERROR);
        }
        return  CommonResponse.success(null);
    }
}
