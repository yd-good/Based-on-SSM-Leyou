/*
信息:
*/
package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CodecUtils;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String KEY_PREFIX = "send.verify.code:";

    /**
     * 实现数据校验
     *
     * @param data
     * @param type
     * @return
     */
    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type) {
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnums.USER_DATA_TYPE_ERROR);
        }
        return userMapper.selectCount(record) == 0;
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    public void sendCode(String phone) {
        String key = KEY_PREFIX + phone;
        String code = null;
        code = NumberUtils.generateCode(6);
        Map map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("code", code);
        amqpTemplate.convertAndSend("ly.send.exchange", "send.verify.code", map);
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        System.out.println(code);
    }

    /**
     * 用户校验和注册
     *
     * @param user
     * @param code
     */
    public Boolean register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        String codeCache = redisTemplate.opsForValue().get(key);
        if (code == codeCache) {
            return false;
        }
        user.setId(null);
        user.setCreated(new Date());
        String codePassword = CodecUtils.passwordBcryptEncode(user.getUsername().trim(), user.getPassword().trim());
        user.setPassword(codePassword);
        boolean result = userMapper.insertSelective(user) == 1;
        if (result) {
            try {
                this.redisTemplate.delete(key);
            } catch (Exception e) {
                logger.error("删除缓存验证码失败，code:{}", code, e);
            }
        }
        return result;
    }

    /**
     * 用户验证
     * @param username
     * @param password
     * @return
     */
    public User queryUser(String username, String password) {
        //1.查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);

        //2.校验用户名
        if (user == null){
            return null;
        }
        //3. 校验密码
        boolean result = CodecUtils.passwordConfirm(username + password,user.getPassword());
        if (!result){
            return null;
        }

        //4.用户名密码都正确
        return user;
    }
}




