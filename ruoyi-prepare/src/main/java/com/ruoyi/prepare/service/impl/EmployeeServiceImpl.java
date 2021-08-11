package com.ruoyi.prepare.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.prepare.mapper.EmployeeMapper;
import com.ruoyi.prepare.service.EmployeeService;
import com.ruoyi.common.core.domain.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName EmployeeServiceImpl
 * @Description: TODO
 * @Author RangBai WU
 * @Date 2021/2/22
 **/
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public List<Employee> getEmployeeList(Employee employee) {
       List<Employee> employeeList =  employeeMapper.getEmployeeList(employee);
       return employeeList;
    }

    public Employee getEmployeeByNwsId(String nwsId) {
        List<Employee> employees =  employeeMapper.getEmployeeList(new Employee(nwsId));
        return employees.isEmpty() ? null : employees.get(0);
    }

    @Override
    @Transactional
    public int updateEmployee(Employee employee) {
        employee.setUpdateBy(SecurityUtils.getUsername());
        return employeeMapper.updateEmployee(employee);
    }

    @Override
    public int insertEmployee(Employee employee) {
        return employeeMapper.insertEmployee(employee);
    }

    @Override
    public String importEmployee(List<Employee> employeeList, String operatorName) {
        if (StringUtils.isNull(employeeList) || employeeList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Employee employee: employeeList) {
            try {
                if (!employee.getEnableLogin().equals("0") && !employee.getEnableLogin().equals("1")){
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、工号 " + employee.getEeId() + " 导入失败：允许登录只能为0或1";
                    failureMsg.append(msg);
                } else if (!employee.getGender().equals("F") && !employee.getGender().equals("M")){
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、工号 " + employee.getEeId() + " 导入失败：性别只能为F或M";
                    failureMsg.append(msg);
                } else if (StringUtils.isNotNull(employee.getEeId())){
                    Employee result = employeeMapper.getEmployeeByEeId(employee.getEeId());
                    if (StringUtils.isNotNull(result)){
                        updateEmployee(employee);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + employee.getEeId() + " 更新成功");
                    } else {
                        insertEmployee(employee);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、账号 " + employee.getChiName() + " 导入成功");
                    }
                }
            }catch (Exception e){
                failureNum++;
                String msg = "<br/>" + failureNum + "、工号 " + employee.getEeId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "数据导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }


}
