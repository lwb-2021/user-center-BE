package com.github.lwb_2021.user_center;

import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.core.JsonEncoding;
import com.github.lwb_2021.user_center.model.Account;
import com.github.lwb_2021.user_center.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserCenterApplicationTests {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Test
    void contextLoads() {
        Account[] accounts = accountService.list().toArray(new Account[0]);
        System.out.println(Arrays.toString(accounts));
        assert accounts.length > 0;
        for (Account account : accounts) {
            System.err.println("-------------------"+account);
        }
    }

}
