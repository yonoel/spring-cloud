package com.example.gatewaydemo;


import java.util.List;

import com.ctrip.framework.apollo.core.dto.ApolloConfigNotification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * .
 *
 * @author yonoel 2021/05/18
 */
public class DeferredResultWrapper {
    private static final long time_out = 60 * 1000;
    private static final ResponseEntity<List<ApolloConfigNotification>> not_modify_response_list = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    private DeferredResult<ResponseEntity<List<ApolloConfigNotification>>> result;

    public DeferredResultWrapper() {
        this.result = new DeferredResult<>(time_out, not_modify_response_list);
    }

    public void onTimeout(Runnable runnable) {
        result.onTimeout(runnable);
    }

    public void onCompletion(Runnable runnable) {
        result.onCompletion(runnable);
    }

    public void setResult(List<ApolloConfigNotification> apolloConfigNotifications) {
        result.setResult(new ResponseEntity<>(apolloConfigNotifications, HttpStatus.OK));
    }

    public DeferredResult<ResponseEntity<List<ApolloConfigNotification>>> getResult() {
        return result;
    }
}
