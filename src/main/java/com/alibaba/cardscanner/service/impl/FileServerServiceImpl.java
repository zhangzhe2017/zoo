package com.alibaba.cardscanner.service.impl;

import com.alibaba.cardscanner.infrastructure.dto.FileServerResultDTO;
import com.alibaba.cardscanner.infrastructure.http.HttpProtocolHandler;
import com.alibaba.cardscanner.infrastructure.http.model.HttpRequest;
import com.alibaba.cardscanner.infrastructure.http.model.MultiPartParam;
import com.alibaba.cardscanner.service.FileServerService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by rmy on 16/6/29.
 */
@Service
@EnableAutoConfiguration
public class FileServerServiceImpl implements FileServerService {
    @Value("${fileserver.gateway}")
    private String gateway;

    @Override
    public FileServerResultDTO uploadFileServer(String fileName, byte[] content) {
        if(StringUtils.isBlank(gateway)){
            //todo add log
            throw new RuntimeException("get file server gateway uri failed.");
        }

        MultiPartParam<String> fileNamePart = new MultiPartParam<>("Filename", fileName);
        MultiPartParam<String> namePart = new MultiPartParam<>("name", getName(fileName));
        MultiPartParam<String> scenePart = new MultiPartParam<>("scene", "productImageRule");
        MultiPartParam<byte[]> filePart = new MultiPartParam<>("file", content, ContentType.APPLICATION_OCTET_STREAM, fileName);

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl(gateway);
        httpRequest.setMultiPartParams(Arrays.asList(fileNamePart, namePart, scenePart, filePart));

        String uploadResult = HttpProtocolHandler.multiPartPost(httpRequest);
        return StringUtils.isNotBlank(uploadResult) ? JSONObject.parseObject(uploadResult, FileServerResultDTO.class) : null;
    }

    private String getName(String fileName) {
        Long now = new Date().getTime();
        return String.valueOf(now) + "_" + fileName;
    }
}
