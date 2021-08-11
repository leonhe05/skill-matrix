package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.entity.Employee;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.prepare.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Employee employee = employeeService.getEmployeeByNwsId(username);
        if (StringUtils.isNull(employee))
        {
            log.info("登录账号：{} 尚未与员工进行绑定，请寻找管理员为您绑定.", username);
            throw new UsernameNotFoundException("登录账号：" + username + " 尚未与员工进行绑定，请寻找管理员为您绑定.");
        } else if (employee.getEnableLogin().equals("0")){
            log.info("登录用户：{} 登录状态未开启.", username);
            throw new BaseException("对不起，您的账号：" + username + " 登录状态未开启");
        }

        return createLoginUser(employee);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }

    public UserDetails createLoginUser(Employee employee)
    {
        return new LoginUser(employee, permissionService.getMenuPermission(employee));
    }
}
