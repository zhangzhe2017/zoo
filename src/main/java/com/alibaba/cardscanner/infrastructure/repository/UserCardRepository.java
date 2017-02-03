package com.alibaba.cardscanner.infrastructure.repository;

import com.alibaba.cardscanner.infrastructure.dto.PageDTO;
import com.alibaba.cardscanner.infrastructure.model.UserCard;

import java.util.List;

/**
 * Created by rmy on 16/6/26.
 */
public interface UserCardRepository {
    Long saveUserCard(UserCard userCard, String userId);

    boolean deleteUserCard(Long id, String userId);

    boolean updateUserCard(Long id, UserCard userCard, String userId);

    UserCard findById(Long id);

    /**
     * 取userId的所有名片信息
     * 按照gmt_create 倒序排序
     * @param userId
     * @param pageDTO
     * @return
     */
    List<UserCard> queryUserIdOrderByCreate(String userId, PageDTO pageDTO);
}
