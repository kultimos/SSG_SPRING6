package com.kul.a05;

import com.alibaba.druid.pool.DruidDataSource;

import com.kul.a05.component.Bean2;
import org.mybatis.spring.SqlSessionFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;


@Configuration
@ComponentScan("com.kul.a05.component")
public class Config {

    @Bean
    public Bean1 bean1(){
        return new Bean1();
    }


    public Bean2 bean2() {
        return new Bean2();
    }


    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://10.117.57.248:3306");
        dataSource.setUsername("root");
        dataSource.setPassword("FHGKxz0ovdC");
        return dataSource;
    }

}
