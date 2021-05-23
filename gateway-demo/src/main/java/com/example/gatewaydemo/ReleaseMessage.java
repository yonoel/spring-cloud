package com.example.gatewaydemo;


import lombok.Getter;

/**
 * .
 *
 * @author yonoel 2021/05/18
 */
@Getter
public class ReleaseMessage {

    private String message;

    public ReleaseMessage(String message) {
        this.message = message;
    }
}
