package com.ruoyi.prepare.service;


import com.ruoyi.common.core.domain.entity.Employee;

import java.util.List;

/**
 * @ClassName EmployeeService
 * @Description: TODO
 * @Author RangBai WU
 * @Date 2021/2/22
 **/
public interface EmployeeService {

    List<Employee> getEmployeeList(Employee employee);

    Employee getEmployeeByNwsId(String nwsId);

    int updateEmployee(Employee employee);

    int insertEmployee(Employee employee);

    String importEmployee(List<Employee> employeeList, String operatorName);
}
