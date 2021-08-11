package com.ruoyi.prepare.domain.vo;

import com.ruoyi.prepare.domain.Rating;

/**
 * @author ：nx014924
 * @date ：Created in 8/2/2021 2:58 PM
 * @description：
 * @modified By：
 * @version:
 */
public class RatingVo extends Rating {
    private Long positionId;

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }
}
