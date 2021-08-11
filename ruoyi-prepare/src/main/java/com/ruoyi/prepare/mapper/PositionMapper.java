package com.ruoyi.prepare.mapper;

import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.PositionEmployee;
import com.ruoyi.prepare.domain.vo.PositionEmployeeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public interface PositionMapper {
    List<Position> selectPositionList(Position position);

    List<Position> selectPositionByIds(Set<String> positionIds);

    int checkIsOwner(@Param("positionId")Long positionId, @Param("eeId")String eeId);

    List<Position> selectPositionListByEeId(String eeId);

    int insertPosition(Position position);

    int insertPositionCompetence(@Param("positionId")Long positionId, @Param("competenceId")Long competenceId);

    int deletePositionCompetence(Position position);

    int deletePositionCompetenceByPath(String path);

    int updatePosition(Position position);

    void deletePositionByPath(String path);

    int deletePositionEmployee(PositionEmployee positionEmployee);

    int insertPositionEmployee(PositionEmployee positionEmployee);

    List<PositionEmployeeVo> listPositionEmployee(Long positionId);

    int updatePositionEmployee(PositionEmployee positionEmployee);
}
