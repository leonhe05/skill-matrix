package com.ruoyi.prepare.service;

import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.PositionEmployee;
import com.ruoyi.prepare.domain.vo.PositionEmployeeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public interface PositionService {
    List<Position> selectPositionList(Position position);

    List<Position> getPositionTree();

    List<Position> getPositionTree(String eeId);

    int checkIsOwner(Long positionId, String eeId);

    int insertPosition(Position position);

    int insertPositionEmployee(PositionEmployee positionEmployee);

    List<PositionEmployeeVo> listPositionEmployee(Long positionId);

    int updatePositionEmployee(PositionEmployee positionEmployee);

    int deletePositionEmployee(PositionEmployee positionEmployee);

    int updatePosition(Position position);

    void deletePositionByIds(Long[] positionIds);
}