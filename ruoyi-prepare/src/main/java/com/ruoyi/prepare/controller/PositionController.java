package com.ruoyi.prepare.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.PositionEmployee;
import com.ruoyi.prepare.domain.vo.PositionEmployeeVo;
import com.ruoyi.prepare.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 9:35 AM
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/talent/position")
public class PositionController extends BaseController {

    @Autowired
    private PositionService positionService;

    @PreAuthorize("@ss.hasPermi('talent:position:list')")
    @GetMapping("/list")
    public TableDataInfo list(Position position)
    {
        startPage();
        List<Position> list = positionService.selectPositionList(position);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:position:list')")
    @GetMapping("/listTree")
    public AjaxResult listTree()
    {
        List<Position> list = positionService.getPositionTree();
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:position:list')")
    @GetMapping("/listMyTree")
    public AjaxResult listMyTree()
    {
        List<Position> list = positionService.getPositionTree(SecurityUtils.getEeId());
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:position:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Position position)
    {
        position.setCreateBy(SecurityUtils.getUsername());
        return toAjax(positionService.insertPosition(position));
    }

    /**
     * 修改技能
     */
    @PreAuthorize("@ss.hasPermi('talent:position:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Position position)
    {
        position.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(positionService.updatePosition(position));
    }

    /**
     * 删除技能
     */
    @PreAuthorize("@ss.hasPermi('talent:position:remove')")
    @DeleteMapping("/{positionIds}")
    public AjaxResult remove(@PathVariable Long[] positionIds)
    {
        positionService.deletePositionByIds(positionIds);
        return success();
    }

    /**
     * 添加岗位员工
     */
    @PutMapping("/assign/insertPositionEmployee")
    public AjaxResult insertPositionEmployee(@RequestBody PositionEmployee positionEmployee)
    {

        return toAjax(positionService.insertPositionEmployee(positionEmployee));
    }

    @GetMapping("/assign/listPositionEmployee")
    public TableDataInfo listPositionEmployee(Long positionId)
    {
        startPage();
        List<PositionEmployeeVo> positionEmployeeVoList = positionService.listPositionEmployee(positionId);
        return getDataTable(positionEmployeeVoList);
    }

    @PostMapping("/assign/updatePositionEmployee")
    public AjaxResult updatePositionEmployee(@RequestBody PositionEmployee positionEmployee)
    {
        return toAjax(positionService.updatePositionEmployee(positionEmployee));
    }

    @PostMapping("/assign/deletePositionEmployee")
    public AjaxResult deletePositionEmployee(@RequestBody PositionEmployee positionEmployee)
    {
        return toAjax(positionService.deletePositionEmployee(positionEmployee));
    }

}

