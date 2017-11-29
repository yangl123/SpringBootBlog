package com.yangle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * Created by yangle on 2017/10/11.
 */
public class StartUpListener implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartUpListener.class);
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.info("======================系统启动之后，可以在这里执行一些事情。。。。。。===================");
    }
}
