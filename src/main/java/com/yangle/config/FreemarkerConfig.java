package com.yangle.config;

import com.yangle.tag.GlobalTag;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yangle on 2017/10/12.
 */
@Component
public class FreemarkerConfig {
    @Autowired
    private Configuration configuration;
    @Autowired
    private GlobalTag globalTag;
@PostConstruct
    public void setSharedVariable(){
    configuration.setSharedVariable("global",globalTag);
}
}
