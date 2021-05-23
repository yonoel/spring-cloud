package com.example.gatewaydemo;


/**
 * 模拟config service扫描message表.
 *
 * @author yonoel 2021/05/18
 */

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ReleaseMessageScanner implements InitializingBean {
    @Autowired
    private NotificationControllerV2 notificationControllerV2;
    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(()->{
            for (;;){
                final String poll = NotificationControllerV2.queue.poll();
                if (poll != null) {
                    final ReleaseMessage releaseMessage = new ReleaseMessage(poll);
                    notificationControllerV2.handleMessage(releaseMessage);
                }
            }
        }).start();
    }
}
