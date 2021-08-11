package com.ruoyi.prepare.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.PositionEmployee;
import com.ruoyi.prepare.domain.vo.PositionEmployeeVo;
import com.ruoyi.prepare.mapper.CompetenceMapper;
import com.ruoyi.prepare.mapper.PositionMapper;
import com.ruoyi.prepare.service.PositionService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private CompetenceMapper competenceMapper;

    @Override
    public List<Position> selectPositionList(Position position) {
        List<Position> positionList = positionMapper.selectPositionList(position);
        for (Position positionItem: positionList) {
            positionItem.setCompetences(competenceMapper.selectCompetenceListByPosition(positionItem));
        }
        return positionList;
    }

    /**
     * 获取岗位树
     */
    @Override
    public List<Position> getPositionTree() {
        List<Position> positionList = positionMapper.selectPositionList(new Position());
        List<Position> treeList = new ArrayList<>();
        for (Position position: positionList) {
            if (position.getParentId() == 0){
                getChildTree(positionList, position);
                treeList.add(position);
            }
        }
        return treeList;
    }

    private Position getChildTree(List<Position> sourceList, Position rootNode) {
        for (Position node: sourceList) {
            if (node.getParentId() == rootNode.getPositionId()){
                List<Position> childList = rootNode.getChildren();
                if (childList == null) childList = new ArrayList<>();
                childList.add(getChildTree(sourceList,node));
                rootNode.setChildren(childList);
            }
        }
        return rootNode;
    }

    /**
     * 根据eeId获取该员工的所有岗位，
     * 那么这个岗位树将剔除这个结点及其子节点
     */
    @Override
    public List<Position> getPositionTree(String eeId) {
        List<Position> myPositions = positionMapper.selectPositionListByEeId(eeId);
        if (myPositions.isEmpty()) return myPositions;

        Set<String> neededPositions = new HashSet();
        for (Position position: myPositions) {
            String path = position.getPath();
            neededPositions.addAll(Arrays.asList(path.split("-")));
        }
        /*
         * 岗位树的某个结点，如果是用户有这个岗位的话，那么disable为0，
         * 否则为1 (供前端的树决定某个结点是否可选，自己的岗位才可选，路径中间的结点不可选)
         */
        List<Position> sourceList = positionMapper.selectPositionByIds(neededPositions);
        for (Position sourcePosition: sourceList) {
            sourcePosition.setDisable("true");
            for (Position myPosition: myPositions) {
                if (sourcePosition.getPositionId() == myPosition.getPositionId()){
                    sourcePosition.setDisable("false");
                }
            }
        }


        List<Position> treeList = new ArrayList<>();
        for (Position position: sourceList) {
            if (position.getParentId() == 0){
                getChildTree(sourceList, position);
                treeList.add(position);
            }
        }
        return treeList;
    }

    @Override
    public int checkIsOwner(Long positionId, String eeId) {
        return positionMapper.checkIsOwner(positionId,eeId);
    }


    @Override
    @Transactional
    public int insertPosition(Position position) {
        if(positionMapper.insertPosition(position) == 0)
            return 0;
        for (Competence competence: position.getCompetences()) {
            positionMapper.insertPositionCompetence(position.getPositionId(), competence.getCompetenceId());
        }
        return positionMapper.updatePosition(setPath(position));
    }

    @Override
    public int insertPositionEmployee(PositionEmployee positionEmployee) {
        return positionMapper.insertPositionEmployee(positionEmployee);
    }

    @Override
    public List<PositionEmployeeVo> listPositionEmployee(Long positionId) {
        return positionMapper.listPositionEmployee(positionId);
    }

    @Override
    public int updatePositionEmployee(PositionEmployee positionEmployee) {
        return positionMapper.updatePositionEmployee(positionEmployee);
    }

    @Override
    public int deletePositionEmployee(PositionEmployee positionEmployee) {
        return positionMapper.deletePositionEmployee(positionEmployee);
    }

    @Override
    public int updatePosition(Position position) {
        positionMapper.deletePositionCompetence(position);
        for (Competence competence: position.getCompetences()) {
            positionMapper.insertPositionCompetence(position.getPositionId(), competence.getCompetenceId());
        }
        return positionMapper.updatePosition(setPath(position));
    }

    /**
     * create by: Leon
     * description: 删除岗位及其所有的子岗位
     * create time: 7/19/2021 5:37 PM
     * @Param: null
     * @return
     */
    @Override
    @Transactional
    public void deletePositionByIds(Long[] positionIds) {
        Position position = new Position();
        for (Long positionId : positionIds)
        {
            position.setPositionId(positionId);
            String path = positionMapper.selectPositionList(position).get(0).getPath();
            positionMapper.deletePositionByPath(path);
            // 删除岗位 - 技能的关联
            positionMapper.deletePositionCompetence(position);
        }
    }

    public Position setPath(Position position){
        /*
         * 如果新添加的岗位有父岗位，那么该岗位的路径为父岗位路径 + 自己的positionId
         * 如果没有父岗位，那么该岗位的路径为自己的positionId
         */
        if(position.getParentId() != 0){
            Position parentPosition = new Position();
            parentPosition.setPositionId(position.getParentId());
            List<Position> parentPositionList = positionMapper.selectPositionList(parentPosition);
            parentPosition = parentPositionList.get(0); //指定了父岗位的id，那么自然只有一条记录
            position.setPath(parentPosition.getPath() + "-" + position.getPositionId());
        } else{
            position.setPath(position.getPositionId().toString());
        }
        return position;
    }
}
