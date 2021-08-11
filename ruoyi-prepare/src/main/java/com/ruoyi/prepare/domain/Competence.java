package com.ruoyi.prepare.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;
import java.util.PrimitiveIterator;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public class Competence extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 用户ID */
    @Excel(name = "技能编号", cellType = Excel.ColumnType.NUMERIC)
    private Long competenceId;

    /** 部门ID */
    @Excel(name = "中文名称")
    private String cLabel;

    /** 部门ID */
    @Excel(name = "英文名称")
    private String eLabel;

    @Excel(name = "父技能编号")
    private Long parentId;

    private List<Competence> children;

    private String path;

    private int scorePlan;

    @Excel(name = "是否禁用")
    private String disable;

    private String flagTag;

    private String parentName;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Competence> getChildren() {
        return children;
    }

    public void setChildren(List<Competence> children) {
        this.children = children;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(Long competenceId) {
        this.competenceId = competenceId;
    }

    public String getcLabel() {
        return cLabel;
    }

    public void setcLabel(String cLabel) {
        this.cLabel = cLabel;
    }

    public String geteLabel() {
        return eLabel;
    }

    public void seteLabel(String eLabel) {
        this.eLabel = eLabel;
    }

    public String getCLabel() {
        return cLabel;
    }

    public void setCLabel(String cLabel) {
        this.cLabel = cLabel;
    }

    public String getELabel() {
        return eLabel;
    }

    public void setELabel(String eLabel) {
        this.eLabel = eLabel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getScorePlan() {
        return scorePlan;
    }

    public void setScorePlan(int scorePlan) {
        this.scorePlan = scorePlan;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getFlagTag() {
        return flagTag;
    }

    public void setFlagTag(String flagTag) {
        this.flagTag = flagTag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Competence{" +
                "competenceId=" + competenceId +
                ", cLabel='" + cLabel + '\'' +
                ", eLabel='" + eLabel + '\'' +
                ", parentId=" + parentId +
                ", path='" + path + '\'' +
                ", scorePlan=" + scorePlan +
                ", disable='" + disable + '\'' +
                ", flagTag='" + flagTag + '\'' +
                '}';
    }
}
