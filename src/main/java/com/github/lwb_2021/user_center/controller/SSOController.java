package com.github.lwb_2021.user_center.controller;

import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.github.lwb_2021.user_center.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SSOController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping("/sso/auth")
    public Object ssoAuth() {
        return SaSsoHandle.ssoAuth();
    }


    @RequestMapping("/sso/checkTicket")
    public Object ssoCheckTicket() {
        return SaSsoHandle.ssoCheckTicket();
    }

    @RequestMapping("/sso/logout")
    public Object ssoLogout() {
        if(SpringMVCUtil.getRequest().getParameterMap().containsKey("longId"))
            return SaSsoHandle.ssoLogoutByClientHttp();
        return SaSsoHandle.ssoLogoutByUserVisit();
    }

    @Autowired
    public void configSSO(SaSsoConfig config) {
        config.setNotLoginView(() -> SaResult.get(401, "未登录", null));

    }
}
