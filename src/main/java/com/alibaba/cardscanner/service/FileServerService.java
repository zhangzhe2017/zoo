package com.alibaba.cardscanner.service;


import com.alibaba.cardscanner.infrastructure.dto.FileServerResultDTO;

/**
 * Created by rmy on 16/6/29.
 */
public interface FileServerService {
    /**
     * 上传file server2
     * @param fileName 带后缀的文件名
     * @param content byte[]形式文件内容
     * @return
     */
    FileServerResultDTO uploadFileServer(String fileName, byte[] content);
}
