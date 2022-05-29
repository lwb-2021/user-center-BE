package com.github.lwb_2021.user_center.controller;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lwb_2021.user_center.model.Account;
import com.github.lwb_2021.user_center.service.AccountService;
import com.github.lwb_2021.user_center.utils.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;
    private RedisTemplate<String, Account> redisTemplate;

    @Autowired
    public void setUserService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Account> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @RequestMapping("/login")
    public SaResult login(@RequestBody StringMap requestMap, @RequestParam(defaultValue = "false") boolean rememberMe){
        String username = requestMap.get("username");
        String password = requestMap.get("password");
        password = SaSecureUtil.sha256(password);

        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).or().eq("email", username);

        Account account = accountService.getOne(wrapper);
        if(account == null || !account.canLogin(username, password)){
            SpringMVCUtil.getResponse().setStatus(400);
            return SaResult.get(400, "用户名或密码错误", account);
        }

        StringMap map = new StringMap();
        map.put("id", String.valueOf(account.getId()));
        map.put("username", account.getUsername());
        map.put("level", String.valueOf(account.getLevel()));
        map.put("role", String.valueOf(account.getRole()));
        StpUtil.login(account.getId(), rememberMe);
        return SaResult.get(200, "登录成功", map);
    }
    @RequestMapping("/logout")
    public SaResult logout(){
        StpUtil.logout();
        return SaResult.ok("退出成功");
    }

    @RequestMapping("/verify")
    public SaResult verify(){
        long id = StpUtil.getLoginIdAsLong();
        Account account = this.accountService.getById(id);
        return SaResult.get(200, "操作成功", account);
    }
    @RequestMapping("/checkLogin")
    public SaResult checkLogin(){
        StpUtil.checkLogin();
        return SaResult.ok("已登录");
    }
    @RequestMapping("/register")
    public SaResult register(@RequestBody StringMap requestMap){
        long id = SnowflakeIdWorker.getInstance().nextId();
        String username = requestMap.get("username");
        String password = requestMap.get("password");
        password = SaSecureUtil.sha256(password);
        String email = requestMap.get("email");
        Account account = new Account(id, email, username, password);
        UUID verifyCode = UUID.randomUUID();
        redisTemplate.opsForValue().set(verifyCode.toString(), account, 30, TimeUnit.MINUTES);

        MailUtil.send(email, "验证码", "您的验证码是：" +
                verifyCode + "\n验证码将在30分钟后过期，请尽快验证", false);

        System.out.println(verifyCode);
        return SaResult.ok("注册成功，请继续邮箱验证");
    }
    @RequestMapping("/register/verify/{verifyCode}")
    public SaResult register_verify(@PathVariable String verifyCode){
        Account account = redisTemplate.opsForValue().get(verifyCode);
        if(account == null){
            SpringMVCUtil.getResponse().setStatus(400);
            return SaResult.get(400, "验证码错误或已过期", null);
        }
        accountService.save(account);
        return SaResult.get(200, "注册成功", account);
    }
}
class StringMap extends HashMap<String, String>{}