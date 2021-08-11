package com.ruoyi.prepare.service;

import com.ruoyi.prepare.domain.Competence;
import com.ruoyi.prepare.domain.Position;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 7/12/2021 10:08 AM
 * @description：
 * @modified By：
 * @version:
 */
public interface CompetenceService {
    List<Competence> selectCompetenceList(Competence competence);

    List<Competence> selectCompetenceListByPosition(Position position);

    List<Competence> getCompetenceTree();

    String importCompetence(List<Competence> competenceList, String operatorName);

    int insertCompetence(Competence competence);

    int updateCompetence(Competence competence);

    void deleteCompetenceByIds(Long[] competenceIds);
}
