package com.ruoyi.prepare.controller;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.service.CompetenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 9:35 AM
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/talent/competence")
public class CompetenceController extends BaseController {

    @Autowired
    private CompetenceService competenceService;

    @PreAuthorize("@ss.hasPermi('talent:competence:list')")
    @GetMapping("/list")
    public TableDataInfo list(Competence competence)
    {
        startPage();
        List<Competence> list = competenceService.selectCompetenceList(competence);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:competence:list')")
    @GetMapping("/listByPosition")
    public AjaxResult listByPosition(Position position)
    {
        List<Competence> list = competenceService.selectCompetenceListByPosition(position);
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:competence:list')")
    @GetMapping("/listTree")
    public AjaxResult listTree()
    {
        List<Competence> list = competenceService.getCompetenceTree();
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('talent:competence:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Competence competence)
    {
        competence.setCreateBy(SecurityUtils.getUsername());
        return toAjax(competenceService.insertCompetence(competence));
    }

    /**
     * 修改技能
     */
    @PreAuthorize("@ss.hasPermi('talent:competence:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Competence competence)
    {
        return toAjax(competenceService.updateCompetence(competence));
    }

    /**
     * 删除技能
     */
    @PreAuthorize("@ss.hasPermi('talent:competence:remove')")
    @DeleteMapping("/{competenceIds}")
    public AjaxResult remove(@PathVariable Long[] competenceIds)
    {
        competenceService.deleteCompetenceByIds(competenceIds);
        return success();
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<Competence> util = new ExcelUtil<Competence>(Competence.class);
        return util.importTemplateExcel("技能模板");
    }


    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<Competence> util = new ExcelUtil<Competence>(Competence.class);
        List<Competence> competenceList = util.importExcel(file.getInputStream());
        String operatorName = SecurityUtils.getUsername();
        String message = competenceService.importCompetence(competenceList, operatorName);
        return AjaxResult.success(message);
    }

    @GetMapping("/export")
    public AjaxResult export(Competence competence)
    {
        List<Competence> list = competenceService.selectCompetenceList(competence);
        ExcelUtil<Competence> util = new ExcelUtil<Competence>(Competence.class);
        return util.exportExcel(list, "技能数据");
    }
}
