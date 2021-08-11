package com.ruoyi.prepare.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.prepare.domain.Position;
import com.ruoyi.prepare.domain.Rating;
import com.ruoyi.prepare.mapper.RatingMapper;
import com.ruoyi.prepare.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：nx014924
 * @date ：Created in 8/2/2021 9:45 AM
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public List<Rating> selectRatingByPosition(Position position) {
        return ratingMapper.selectRatingByPosition(position);
    }

    @Override
    public int updateRating(Rating rating) {
        rating.setUpdateBy(SecurityUtils.getEeId());
        int result = ratingMapper.updateRating(rating);
        if (result == 0) {
            rating.setCreateBy(SecurityUtils.getEeId());
            return ratingMapper.insertRating(rating);
        }
        return result;
    }


}
