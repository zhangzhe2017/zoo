package com.alibaba.cardscanner.service.impl;

import com.alibaba.cardscanner.infrastructure.dto.PageDTO;
import com.alibaba.cardscanner.infrastructure.model.UserCard;
import com.alibaba.cardscanner.infrastructure.repository.UserCardRepository;
import com.alibaba.cardscanner.service.UserCardService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by rmy on 16/6/29.
 */
@Service
public class UserCardServiceImpl implements UserCardService {
    @Autowired
    private UserCardRepository userCardRepository;

    public Long saveOrUpdateUserCard(UserCard userCard, String dingdingId) {
        userCard.setUserId(dingdingId);
        if(userCard.getId() != null){
            boolean success = userCardRepository.updateUserCard(userCard.getId(), userCard, dingdingId);
            return success ? 1L : 0L;
        }else {
            if(StringUtils.isBlank(userCard.getCardInfo())
                    || StringUtils.isBlank(userCard.getFileServerUri())
                    || StringUtils.isBlank(userCard.getFileName())){
                throw new RuntimeException("param illegal");
            }
            return userCardRepository.saveUserCard(userCard, dingdingId);
        }
    }

    @Override
    public boolean deleteUserCard(Long id, String dingdingId) {
        return userCardRepository.deleteUserCard(id, dingdingId);
    }

    @Override
    public List<UserCard> queryUserCards(int pageSize, Date endDate, String dingdingId) {
        PageDTO pageDTO = new PageDTO();
        if(pageSize != 0){
            pageDTO.setPageSize(pageSize);
        }
        if(endDate != null){
            pageDTO.setEndDate(endDate);
        }else {
            pageDTO.setEndDate(new Date());
        }

        return userCardRepository.queryUserIdOrderByCreate(dingdingId, pageDTO);
    }

    @Override
    public UserCard findUserCardById(Long id) {
        return userCardRepository.findById(id);
    }
}
