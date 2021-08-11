package com.ruoyi.prepare.mapper;

import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.Rating;

import java.util.List;

public interface RatingMapper {
    List<Rating> selectRatingByPosition(Position position);

    int updateRating(Rating rating);

    int insertRating(Rating rating);

    int deleteRatingByPath(String path);
}
