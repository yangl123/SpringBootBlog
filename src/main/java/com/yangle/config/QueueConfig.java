package com.yangle.config;

import com.yangle.util.QueueNames;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


/**
 * Created by yangle on 2017/9/24.
 */
@Configuration
public class QueueConfig {
    //日志队列
    @Bean
    public Queue logQueue(){
        return new ActiveMQQueue(QueueNames.LOG_QUEUE);
    }
}
