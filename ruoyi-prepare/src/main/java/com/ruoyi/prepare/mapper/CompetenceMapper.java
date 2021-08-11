package com.ruoyi.prepare.mapper;

import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public interface CompetenceMapper {
    List<Competence> selectCompetenceList(Competence competence);

    List<Competence> selectCompetenceListByPosition(Position position);

    int insertCompetence(Competence competence);

    int updateCompetence(Competence competence);

    void deleteCompetenceByPath(String path);
}
