package com.ruoyi.prepare.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @author ：nx014924
 * @date ：Created in 7/26/2021 4:58 PM
 * @description：
 * @modified By：
 * @version:
 */
public class PositionEmployee extends BaseEntity {
    Long positionId;
    String eeId;
    String owner;

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
