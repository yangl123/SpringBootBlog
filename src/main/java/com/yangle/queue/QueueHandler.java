package com.yangle.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * Created by yangle on 2017/9/24.
 */
@Component
public class QueueHandler {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Queue logQueue;
    public void sendLogQueue(String msg){
        this.jmsMessagingTemplate.convertAndSend(this.logQueue, msg);
    }
}
