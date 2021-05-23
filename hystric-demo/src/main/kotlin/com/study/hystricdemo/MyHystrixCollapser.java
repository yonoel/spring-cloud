package com.study.hystricdemo;


import java.util.Collection;
import java.util.List;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;

/**
 * .
 *
 * @author yonoel 2021/05/13
 */
public class MyHystrixCollapser extends HystrixCollapser<List<String>,String,String> {
    private final String name;

    public MyHystrixCollapser(String name) {
        this.name = name;
    }

    @Override
    public String getRequestArgument() {
        return name;
    }

    @Override
    protected HystrixCommand<List<String>> createCommand(Collection<CollapsedRequest<String, String>> collapsedRequests) {
        return new BatchCommand(collapsedRequests);
    }

    @Override
    protected void mapResponseToRequests(List<String> batchResponse, Collection<CollapsedRequest<String, String>> collapsedRequests) {
        int count = 0;
        for (CollapsedRequest<String, String> collapsedRequest : collapsedRequests) {
            collapsedRequest.setResponse(batchResponse.get(count++));
        }
    }
}
