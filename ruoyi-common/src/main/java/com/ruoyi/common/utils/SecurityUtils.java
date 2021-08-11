package com.ruoyi.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.CustomException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 安全服务工具类
 * 
 * @author ruoyi
 */
public class SecurityUtils
{
    private static RestTemplate restTemplate;

    /**
     * 获取用户账户nwsId
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户工号
     **/
    public static String getEeId()
    {
        try
        {
            return getLoginUser().getEmployee().getEeId();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     * 
     * @param roleId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1l == roleId;
    }


    /**
     * 域登录验证
     *
     * @param nwsId 用户域账号ID, password 密码
     * @return 结果
     */
    public static boolean nwsLogin(String nwsId, String password)
    {
        Map<String,String> uriVariables = new HashMap();
        uriVariables.put("username",nwsId);
        uriVariables.put("password",password);
        if (StringUtils.isNull(restTemplate)){
            restTemplate = new RestTemplate();
        }
        ResponseEntity<String> res = restTemplate.getForEntity("http://172.17.192.115:81/Service.asmx/ValidateUser?username={username}&password={password}", String.class, uriVariables);
        return !res.getBody().contains("xsi:nil=\"true\"");
    }
}
