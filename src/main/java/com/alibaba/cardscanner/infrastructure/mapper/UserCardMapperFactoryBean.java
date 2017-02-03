package com.alibaba.cardscanner.infrastructure.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rmy on 16/6/26.
 */
@Component
public class UserCardMapperFactoryBean extends MapperFactoryBean<UserCardMapper> {

    public UserCardMapperFactoryBean(){
        setMapperInterface(UserCardMapper.class);
    }

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public Class<UserCardMapper> getObjectType(){
        return UserCardMapper.class;
    }
}
