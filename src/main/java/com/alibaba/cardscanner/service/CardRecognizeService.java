package com.alibaba.cardscanner.service;

import com.alibaba.cardscanner.infrastructure.dto.ResultDTO;
import com.alibaba.cardscanner.infrastructure.model.UserCard;

import java.util.Map;

/**
 * Created by rmy on 16/6/29.
 */
public interface CardRecognizeService {
    ResultDTO<Map<String, Object>> recognizeCard(String fileUri);

    ResultDTO<UserCard> recognizeCardFromDD(String picUrl);
}
