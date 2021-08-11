package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户和角色关联 sys_user_role
 * 
 * @author ruoyi
 */
public class SysUserRole
{
    /** 用户ID */
    private String eeId;
    
    /** 角色ID */
    private Long roleId;

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "SysUserRole{" +
                "eeId='" + eeId + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
