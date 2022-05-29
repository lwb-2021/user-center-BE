package com.github.lwb_2021.user_center.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lwb_2021.user_center.mapper.AccountMapper;
import com.github.lwb_2021.user_center.model.Account;
import com.github.lwb_2021.user_center.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
