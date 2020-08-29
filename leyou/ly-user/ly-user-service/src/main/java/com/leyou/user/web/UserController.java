/*
信息:
*/
package com.leyou.user.web;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 数据校验
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable(name = "data")String data,@PathVariable(name = "type")Integer type) {
        return ResponseEntity.ok(userService.checkData(data,type));
    }

    /**
     * 验证码发送校验
     * @param phone
     * @return
     */
    @PostMapping("send")
    public ResponseEntity<Void> sendCode(@RequestParam(name = "phone")String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 注册用户信息到数据库
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam(name = "code")String code){
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        User user = this.userService.queryUser(username,password);
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

}
