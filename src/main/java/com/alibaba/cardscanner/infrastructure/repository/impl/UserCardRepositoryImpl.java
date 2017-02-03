package com.alibaba.cardscanner.infrastructure.repository.impl;

import com.alibaba.cardscanner.infrastructure.dto.PageDTO;
import com.alibaba.cardscanner.infrastructure.enums.YesNoEnum;
import com.alibaba.cardscanner.infrastructure.mapper.UserCardMapper;
import com.alibaba.cardscanner.infrastructure.model.UserCard;
import com.alibaba.cardscanner.infrastructure.model.UserCardExample;
import com.alibaba.cardscanner.infrastructure.repository.UserCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rmy on 16/6/26.
 */
@Repository
public class UserCardRepositoryImpl implements UserCardRepository{
    @Autowired
    private UserCardMapper userCardMapper;

    @Override
    public Long saveUserCard(UserCard userCard, String userId) {
        userCard.beforeInsert(userId);
        return userCardMapper.insert(userCard);
    }

    @Override
    public boolean deleteUserCard(Long id, String userId) {
        UserCard userCard = new UserCard();
        userCard.beforeDelete(userId);
        UserCardExample example = new UserCardExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andCreatorEqualTo(userId)
                .andIsDeletedEqualTo(YesNoEnum.No.getVal());

        return userCardMapper.updateByExampleSelective(userCard, example) > 0;
    }

    @Override
    public boolean updateUserCard(Long id, UserCard userCard, String userId) {
        userCard.beforeUpdate(userId);
        UserCardExample example = new UserCardExample();
        example.createCriteria()
                .andIdEqualTo(id)
                .andCreatorEqualTo(userId)
                .andIsDeletedEqualTo(YesNoEnum.No.getVal());

        return userCardMapper.updateByExampleSelective(userCard, example) > 0;
    }

    @Override
    public UserCard findById(Long id){
        UserCardExample example = new UserCardExample();
        example.createCriteria().andIdEqualTo(id)
        .andIsDeletedEqualTo(YesNoEnum.No.getVal());
        List<UserCard> userCards = userCardMapper.selectByExample(example);

        return userCards != null ? userCards.get(0) : null;
    }

    @Override
    public List<UserCard> queryUserIdOrderByCreate(String userId, PageDTO pageDTO) {
        UserCardExample example = new UserCardExample();
        UserCardExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId)
                .andIsDeletedEqualTo(YesNoEnum.No.getVal());

        if(pageDTO != null){
            if(pageDTO.getPageSize() != 0){
                example.setLimitStart(0);
                example.setLimitEnd(pageDTO.getPageSize());
            }
            if(pageDTO.getEndDate() != null){
                criteria.andGmtCreateLessThanOrEqualTo(pageDTO.getEndDate());
            }
        }

        example.setOrderByClause("gmt_create desc");
        return userCardMapper.selectByExample(example);
    }

}
