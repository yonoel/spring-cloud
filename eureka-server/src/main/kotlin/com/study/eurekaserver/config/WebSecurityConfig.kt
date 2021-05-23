package com.study.eurekaserver.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 * .
 * @author yonoel 2021/05/12
 */
@EnableWebSecurity
class WebSecurityConfig:WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable();
        http.authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
    }
}