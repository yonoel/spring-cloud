package com.study.hystricdemo;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * .
 *
 * @author yonoel 2021/05/13
 */
public class BatchCommand extends HystrixCommand<List<String>> {
    private final Collection<HystrixCollapser.CollapsedRequest<String, String>>  requests;
    public BatchCommand(Collection<HystrixCollapser.CollapsedRequest<String, String>> collapsedRequests) {
        super(Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey("ExampleGroup")
        )
        .andCommandKey(HystrixCommandKey.Factory.asKey("GetValueFoeKey"))
        );
        this.requests = collapsedRequests;
    }

    @Override
    protected List<String> run() throws Exception {
        System.out.println("真正执行请求。。。。。。。。。");
        return requests.stream().map(request->String.format("result %s",request.getArgument())).collect(Collectors.toList());
    }
}
