package com.ruoyi.prepare.service;

import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.Rating;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 8/2/2021 9:45 AM
 * @description：
 * @modified By：
 * @version:
 */
public interface RatingService {
    List<Rating> selectRatingByPosition(Position position);

    int updateRating(Rating rating);
}
