package com.example.gatewaydemo;


/**
 * 模拟apollo的实时推送.
 *
 * @author yonoel 2021/05/18
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import com.ctrip.framework.apollo.core.dto.ApolloConfigNotification;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import org.bouncycastle.asn1.ocsp.ResponseData;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;


@Component
@RestController
public class NotificationControllerV2 implements ReleaseMessageListener {
    // 模拟配置更新，向数据库插入新数据
    public static Queue<String> queue = new LinkedBlockingDeque<>();

    @RequestMapping(method = {
            RequestMethod.GET}, path = {"/addmsg"})
    public String addMsg() {
        queue.add("xxx");
        return "success";
    }

    @RequestMapping(method = {RequestMethod.GET}, path = {"getConfig"})
    public DeferredResult<ResponseEntity<List<ApolloConfigNotification>>> getConfig() {
        final DeferredResultWrapper deferredResultWrapper = new DeferredResultWrapper();
        final List<ApolloConfigNotification> notifications = getApolloConfigNotifications();
        if (!notifications.isEmpty()) {
            deferredResultWrapper.setResult(notifications);
        } else {
            deferredResultWrapper.onTimeout(() -> {
                System.out.println("time out");
            });
            deferredResultWrapper.onCompletion(() -> {
                System.out.println("completion");
            });
            multimap.put("xxxx", deferredResultWrapper);
        }
        return deferredResultWrapper.getResult();
    }

    private List<ApolloConfigNotification> getApolloConfigNotifications() {
        final String poll = queue.poll();
        if (poll != null) {
            return Lists.newArrayList(new ApolloConfigNotification("application", 1));
        }
        return Collections.emptyList();
    }

    private final Multimap<String, DeferredResultWrapper> multimap = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    @Override
    public void handleMessage(ReleaseMessage message) {
        final ArrayList<DeferredResultWrapper> deferredResultWrappers = Lists.newArrayList(multimap.get("xxxx"));
        for (DeferredResultWrapper wrapper : deferredResultWrappers) {
            final List<ApolloConfigNotification> list = new ArrayList<>();
            list.add(new ApolloConfigNotification("application", 1));
            wrapper.setResult(list);
        }

    }
}
