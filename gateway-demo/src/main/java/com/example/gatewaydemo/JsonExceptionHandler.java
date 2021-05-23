package com.example.gatewaydemo;


import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * .
 *
 * @author yonoel 2021/05/17
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {
    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;
        final Throwable error = super.getError(request);
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            code = 404;
        }
        return response(code, this.buildMessage(request, error));
    }


    private String buildMessage(ServerRequest request, Throwable ex) {
        final StringBuilder builder = new StringBuilder("Failed to handler request[");
        builder.append(request.methodName())
                .append(" ")
                .append(request.uri())
                .append("]");
        if (ex != null) {
            builder.append(": ").append(ex.getMessage());
        }
        return builder.toString();
    }

    private Map<String, Object> response(int code, String buildMessage) {
        final HashMap<String, Object> map = new HashMap<>(3);
        map.put("code", code);
        map.put("message", buildMessage);
        map.put("data", null);
        return map;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int code = (int) errorAttributes.get("code");
        return HttpStatus.valueOf(code);
    }


}
