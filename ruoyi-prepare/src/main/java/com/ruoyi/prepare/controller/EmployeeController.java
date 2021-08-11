package com.ruoyi.prepare.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.service.EmployeeService;
import com.ruoyi.common.core.domain.entity.Employee;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName EmployeeController
 * @Description: TODO
 * @Author Leon He
 * @Date 2021/2/17
 **/
@RestController
@RequestMapping(value = "/talent/employee")
public class EmployeeController extends BaseController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ISysRoleService sysRoleService;

    @GetMapping("/getEmployeeList")
    public TableDataInfo getEmployeeList(Employee employee) {
        startPage();
        List<Employee> dataList = employeeService.getEmployeeList(employee);
        return getDataTable(dataList);
    }

    /**
     * 修改账户
     */
    @PreAuthorize("@ss.hasPermi('talent:employee:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Employee employee)
    {
        return toAjax(employeeService.updateEmployee(employee));
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        return util.importTemplateExcel("用户模板");
    }


    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        List<Employee> employeeList = util.importExcel(file.getInputStream());
        String operatorName = SecurityUtils.getUsername();
        String message = employeeService.importEmployee(employeeList, operatorName);
        return AjaxResult.success(message);
    }

    @GetMapping("/export")
    public AjaxResult export(Employee employee)
    {
        List<Employee> list = employeeService.getEmployeeList(employee);
        ExcelUtil<Employee> util = new ExcelUtil<Employee>(Employee.class);
        return util.exportExcel(list, "技能数据");
    }

}
