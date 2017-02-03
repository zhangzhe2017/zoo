package com.alibaba.cardscanner.service;

import com.alibaba.cardscanner.infrastructure.dto.PageDTO;
import com.alibaba.cardscanner.infrastructure.model.UserCard;

import java.util.Date;
import java.util.List;

/**
 * Created by rmy on 16/6/26.
 */
public interface UserCardService {
    Long saveOrUpdateUserCard(UserCard userCard, String dingdingId);

    boolean deleteUserCard(Long id, String dingdingId);

    List<UserCard> queryUserCards(int pageSize, Date endDate, String dingdingId);

    UserCard findUserCardById(Long id);

}
