package com.ruoyi.prepare.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.mapper.CompetenceMapper;
import com.ruoyi.prepare.mapper.PositionMapper;
import com.ruoyi.prepare.mapper.RatingMapper;
import com.ruoyi.prepare.service.CompetenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class CompetenceServiceImpl implements CompetenceService {

    @Autowired
    private CompetenceMapper competenceMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public List<Competence> selectCompetenceList(Competence competence) {
        return competenceMapper.selectCompetenceList(competence);
    }



    @Override
    public List<Competence> selectCompetenceListByPosition(Position position) {
        return competenceMapper.selectCompetenceListByPosition(position);
    }

    @Override
    public List<Competence> getCompetenceTree() {
        List<Competence> competenceList = competenceMapper.selectCompetenceList(new Competence());
        List<Competence> treeList = new ArrayList<>();
        for (Competence competence: competenceList) {
            if (competence.getParentId() == 0){
                getChildTree(competenceList, competence);
                treeList.add(competence);
            }
        }
        return treeList;
    }

    private Competence getChildTree(List<Competence> sourceList, Competence rootNode) {
        for (Competence node: sourceList) {
            if (node.getParentId() == rootNode.getCompetenceId()){
                List<Competence> childList = rootNode.getChildren();
                if (childList == null) childList = new ArrayList<>();
                childList.add(getChildTree(sourceList,node));
                rootNode.setChildren(childList);
            }
        }
        return rootNode;
    }

    @Override
    @Transactional
    public String importCompetence(List<Competence> competenceList, String operatorName) {
        if (StringUtils.isNull(competenceList) || competenceList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Competence item: competenceList) {
            try {
                if (StringUtils.isNotNull(item.getCompetenceId())) {
                    // 更新操作，先判断父技能的id是否合法
                    if (item.getCompetenceId() == item.getParentId()) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、技能 " + item.getCompetenceId() + " 的父技能不能为自身");
                    }
                    Competence parentCompetence = new Competence();
                    parentCompetence.setCompetenceId(item.getParentId());
                    if (competenceMapper.selectCompetenceList(parentCompetence).isEmpty()) {
                        // 父技能id不合法
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、技能 " + item.getCompetenceId() + " 的父技能不存在");
                    } else {
                        updateCompetence(item);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、技能 " + item.getcLabel() + " 更新成功");
                    }
                } else {
                    item.setCreateBy(operatorName);
                    insertCompetence(item);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、技能 " + item.getcLabel() + " 插入成功");
                }
            } catch (Exception e){
                failureNum++;
                String msg = "<br/>" + failureNum + "、技能 " + item.getcLabel() + " 导入失败：";
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

    @Override
    @Transactional
    public int insertCompetence(Competence competence) {
        if(competenceMapper.insertCompetence(competence) == 0)
            return 0;
        return competenceMapper.updateCompetence(setPath(competence));
    }

    @Override
    @Transactional
    public int updateCompetence(Competence competence) {
        if (competence.getCompetenceId() == competence.getParentId())
            throw new CustomException("父技能不能为自己");
        competence.setUpdateBy(SecurityUtils.getUsername());
        String path = competence.getPath();
        // 查找该技能的子孙技能
        Competence query = new Competence();
        query.setPath(path + "-%");
        List<Competence> descendantNodes = competenceMapper.selectCompetenceList(query);
        // 设定路径，更新技能
        Competence competenceWithNewPath = setPath(competence);
        competenceMapper.updateCompetence(competenceWithNewPath);
        // 更新完技能后，还需要更新他子孙的路径，因为他的路径可能已经更改
        for (Competence item : descendantNodes) {
            item.setPath(item.getPath().replace(path,competenceWithNewPath.getPath()));
            competenceMapper.updateCompetence(item);
        }
        return 1;
    }

    /**
     * create by: Leon
     * description: 删除技能及其所有的子技能
     * create time: 7/19/2021 5:37 PM
      * @Param: null
     * @return
     */
    @Override
    @Transactional
    public void deleteCompetenceByIds(Long[] competenceIds) {
        Competence competence = new Competence();
        for (Long competenceId : competenceIds)
        {
            competence.setCompetenceId(competenceId);
            String path = competenceMapper.selectCompetenceList(competence).get(0).getPath();
            positionMapper.deletePositionCompetenceByPath(path);//删除岗位技能关联
            ratingMapper.deleteRatingByPath(path);//删除技能评级关联
            competenceMapper.deleteCompetenceByPath(path);//最后删除技能
        }
    }

    public Competence setPath(Competence competence){
        /*
         * 如果新添加的技能有父技能，那么该技能的路径为父技能路径 + 自己的competenceId
         * 如果没有父技能，那么该技能的路径为自己的competenceId
         */
        if(competence.getParentId() != 0){
            Competence parentCompetence = new Competence();
            parentCompetence.setCompetenceId(competence.getParentId());
            List<Competence> parentCompetenceList = competenceMapper.selectCompetenceList(parentCompetence);
            parentCompetence = parentCompetenceList.get(0); //指定了父技能的id，那么自然只有一条记录
            competence.setPath(parentCompetence.getPath() + "-" + competence.getCompetenceId());
        } else{
            competence.setPath(competence.getCompetenceId().toString());
        }
        return competence;
    }


}
