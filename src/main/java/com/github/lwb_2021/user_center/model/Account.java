package com.github.lwb_2021.user_center.model;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("accounts")
public class Account {
    private Long id;
    private String email;
    private String username;
    private String password;

    private int level = 0;

    private int role = 0;
    private int state = 0;

    @TableLogic
    private int deleted;

    public Account(){}
    public Account(Long id, String email, String username, String password){
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    public boolean canLogin(String username, String password){
        return this.username.equals(username) && this.password.equals(password);
    }
}
