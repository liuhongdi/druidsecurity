package com.druidsecurity.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
 public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //指定访问druid的role
        String druidRule = "hasAnyRole('ADMIN','DEV')";

        //http.csrf().disable();
        //针对druid的ui,关闭csrf,否则会无法登录
        http.csrf().ignoringAntMatchers("/druid/**");

        //login和logout
        http.formLogin()
                .defaultSuccessUrl("/goods/session")
                .failureUrl("/login-error.html")
                .permitAll()
                .and()
                .logout();

        //匹配的页面，符合限制才可访问
        http.authorizeRequests()
                //.antMatchers("/actuator/**").hasIpAddress("127.0.0.1")
                .antMatchers("/druid/**").access(druidRule)
                .antMatchers("/goods/**").hasAnyRole("ADMIN","DEV","USER");

        //剩下的页面，允许访问
        http.authorizeRequests().anyRequest().permitAll();

    }

    @Autowired
    public  void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //添加两个账号用来做测试
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("lhdadmin")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("ADMIN","USER")
                .and()
                .withUser("lhduser")
                .password(new BCryptPasswordEncoder().encode("123456"))
                .roles("USER");
    }
 }