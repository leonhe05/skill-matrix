package com.ruoyi.prepare.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;


/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public class Position extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long positionId;

    private String cLabel;

    private String eLabel;

    private Long parentId;

    private String parentName;

    private List<Competence> competences;

    private List<Position> children;

    private String path;

    private String disable;

    private String flagTag;

    public List<Position> getChildren() {
        return children;
    }

    public void setChildren(List<Position> children) {
        this.children = children;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public void setCompetences(List<Competence> competences) {
        this.competences = competences;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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
        return "Position{" +
                "positionId=" + positionId +
                ", cLabel='" + cLabel + '\'' +
                ", eLabel='" + eLabel + '\'' +
                ", parentId=" + parentId +
                ", path='" + path + '\'' +
                ", disable='" + disable + '\'' +
                ", flagTag='" + flagTag + '\'' +
                '}';
    }
}
