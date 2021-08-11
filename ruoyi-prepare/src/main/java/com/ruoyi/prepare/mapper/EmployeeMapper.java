package com.ruoyi.prepare.mapper;


import com.ruoyi.common.core.domain.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName EmployeeMapper
 * @Description: TODO
 * @Author RangBai WU
 * @Date 2021/2/22
 **/
public interface EmployeeMapper {

    List<Employee> getEmployeeList(Employee employee);

    Employee getEmployeeByEeId(String eeId);

    int updateEmployee(Employee employee);

    int deleteEmployee(Employee employee);

    int insertEmployee(Employee employee);
}
