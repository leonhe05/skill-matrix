package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * @ClassName EmployeeVo
 * @Description: TODO
 * @Author
 * @Date
 **/

public class Employee extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "工号")
    private String eeId;
    @Excel(name = "英文名")
    private String engName;
    @Excel(name = "中文名")
    private String chiName;
    @Excel(name = "拼音")
    private String shortName;
    @Excel(name = "级别", cellType = Excel.ColumnType.NUMERIC)
    private int grade;
    @Excel(name = "性别")
    private String gender;
    @Excel(name = "入职时间")
    private String joinDate;
    @Excel(name = "部门")
    private String department;
    @Excel(name = "组别")
    private String section;
    @Excel(name = "子组别")
    private String subsection;
    @Excel(name = "小公司")
    private String miniCompany;
    @Excel(name = "职位")
    private String position;
    @Excel(name = "技能等级")
    private String skillGrade;
    @Excel(name = "是否在职")
    private String onJob;
    @Excel(name = "NWS账号")
    private String nwsId;
    @Excel(name = "允许登录")
    private String enableLogin;

    public Employee(String nwsId){
        this.nwsId = nwsId;
    }

    public Employee() {};

    public String getEeId() {
        return eeId;
    }

    public void setEeId(String eeId) {
        this.eeId = eeId;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getChiName() {
        return chiName;
    }

    public void setChiName(String chiName) {
        this.chiName = chiName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getMiniCompany() {
        return miniCompany;
    }

    public void setMiniCompany(String miniCompany) {
        this.miniCompany = miniCompany;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSkillGrade() {
        return skillGrade;
    }

    public void setSkillGrade(String skillGrade) {
        this.skillGrade = skillGrade;
    }

    public String getOnJob() {
        return onJob;
    }

    public void setOnJob(String onJob) {
        this.onJob = onJob;
    }

    public String getNwsId() {
        return nwsId;
    }

    public void setNwsId(String nwsId) {
        this.nwsId = nwsId;
    }

    public String getEnableLogin() {
        return enableLogin;
    }

    public void setEnableLogin(String enableLogin) {
        this.enableLogin = enableLogin;
    }

    @Override
    public String toString() {
        return "EmployeeVo{" +
                "eeId='" + eeId + '\'' +
                ", engName='" + engName + '\'' +
                ", chiName='" + chiName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", grade=" + grade +
                ", gender='" + gender + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", department='" + department + '\'' +
                ", section='" + section + '\'' +
                ", subsection='" + subsection + '\'' +
                ", miniCompany='" + miniCompany + '\'' +
                ", position='" + position + '\'' +
                ", skillGrade='" + skillGrade + '\'' +
                ", onJob='" + onJob + '\'' +
                ", nwsId='" + nwsId + '\'' +
                ", enableLogin='" + enableLogin + '\'' +
                '}';
    }
}
