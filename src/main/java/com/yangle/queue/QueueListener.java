package com.yangle.queue;

import com.yangle.util.QueueNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by yangle on 2017/9/24.
 */
@Component
public class QueueListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueListener.class);

//   日志队列监听器
    @JmsListener(destination = QueueNames.LOG_QUEUE)
    public void receivedQueue(String msg) {
        LOGGER.info("Has received from " + QueueNames.LOG_QUEUE + ", msg: " + msg);
    }

}
