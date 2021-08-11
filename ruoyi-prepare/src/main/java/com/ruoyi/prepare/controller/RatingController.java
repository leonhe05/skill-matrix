package com.ruoyi.prepare.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.Rating;
import com.ruoyi.prepare.domain.vo.RatingVo;
import com.ruoyi.prepare.service.CompetenceService;
import com.ruoyi.prepare.service.PositionService;
import com.ruoyi.prepare.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 9:35 AM
 * @description：
 * @modified By：
 * @version:
 */
@RestController
@RequestMapping("/talent/rating")
public class RatingController extends BaseController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private CompetenceService competenceService;

    @GetMapping("/checkOwner")
    public AjaxResult checkOwner(String eeId, Long positionId)
    {
        return AjaxResult.success(positionService.checkIsOwner(positionId, eeId));
    }

    @GetMapping("/list")
    public AjaxResult list(Position position)
    {
        return AjaxResult.success(ratingService.selectRatingByPosition(position));
    }

    @PutMapping
    public AjaxResult edit(@Validated @RequestBody RatingVo ratingVo)
    {
        int result = positionService.checkIsOwner(ratingVo.getPositionId(), SecurityUtils.getEeId());
        if (result == 0)
            return AjaxResult.error("非岗位主管无法评分");
        return toAjax(ratingService.updateRating(ratingVo));
    }

    @GetMapping("/export")
    public AjaxResult export(Position position)
    {
        List<Rating> ratingList = ratingService.selectRatingByPosition(position);
        List<Competence> competenceList = competenceService.selectCompetenceListByPosition(position);

        List<LinkedHashMap<String,String>> employeeList = new ArrayList<>();
        LinkedHashMap<String,String> head = new LinkedHashMap<>();
        employeeList.add(head);
        head.put("name","员工 / 技能");
        for (Competence item: competenceList) {
            head.put("competence" + item.getCompetenceId(), item.getcLabel());
        }
        head.put("sum","总分");
        for (Rating item : ratingList) {
            LinkedHashMap<String,String> result = null;
            for (LinkedHashMap<String,String> employee: employeeList) {
                if (employee.get("eeId") != null && employee.get("eeId").equals(item.getEeId())){
                    result = employee;
                }
            }
            if (result == null) {
                result = new LinkedHashMap<>();
                employeeList.add(result);
                result.put("eeId",item.getEeId());
                result.put("chiName",item.getChiName());
                for (Competence competence: competenceList) {
                    result.put("competence" + competence.getCompetenceId(),"");
                }

            }
            if (StringUtils.isNotNull(item.getCompetenceId()))
                result.put("competence" + item.getCompetenceId(),String.valueOf(item.getRating()));
        }
        for (LinkedHashMap<String,String> item : employeeList) {
            if (item.get("name") != null) continue;
            item.remove("eeId");
            int sum = 0;
            for (Competence competence: competenceList) {
                sum += Integer.parseInt(
                        item.get("competence" + competence.getCompetenceId()).isEmpty()
                        ?
                        "0" : item.get("competence" + competence.getCompetenceId())
                );
            }
            item.put("sum",String.valueOf(sum == 0 ? "" : sum));
        }
        LinkedHashMap<String,String> temp = new LinkedHashMap<>();
        employeeList.add(temp);
        temp = new LinkedHashMap<>();
        temp.put("title","当前岗位:");
        temp.put("positionName",position.getcLabel());
        employeeList.add(temp);

        ExcelUtil util = new ExcelUtil(HashMap.class);
        return util.exportDynamicColumn(employeeList, "用户数据");
    }

}
