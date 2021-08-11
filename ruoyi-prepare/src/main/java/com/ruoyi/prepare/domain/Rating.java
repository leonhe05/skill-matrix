package com.ruoyi.prepare.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * @author ：nx014924
 * @date ：Created in 8/2/2021 9:50 AM
 * @description：
 * @modified By：
 * @version:
 */
public class Rating extends BaseEntity {
    /** 用户账号 */
    @Excel(name = "eeId")
    private String eeId;
    /** 用户账号 */
    @Excel(name = "chiName")
    private String chiName;
    /** 用户账号 */
    @Excel(name = "engName")
    private String engName;
    /** 用户账号 */
    @Excel(name = "competenceId", type = Excel.Type.IMPORT)
    private Long competenceId;
    private int rating;
    /** 用户账号 */
    @Excel(name = "expectRating")
    private int expectRating;
    /** 用户账号 */
    @Excel(name = "lastRating")
    private int lastRating;
    private int archiveRating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date archiveDate;

    public Rating() {

    }

    public int getLastRating() {
        return lastRating;
    }

    public void setLastRating(int lastRating) {
        this.lastRating = lastRating;
    }

    public String getChiName() {
        return chiName;
    }

    public void setChiName(String chiName) {
        this.chiName = chiName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId;
    }

    public Long getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(Long competenceId) {
        this.competenceId = competenceId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getExpectRating() {
        return expectRating;
    }

    public void setExpectRating(int expectRating) {
        this.expectRating = expectRating;
    }

    public int getArchiveRating() {
        return archiveRating;
    }

    public void setArchiveRating(int archiveRating) {
        this.archiveRating = archiveRating;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }
}
