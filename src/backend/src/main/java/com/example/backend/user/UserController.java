package com.example.backend.user;
import com.example.backend.CommonResult;
import com.example.backend.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userService.addUser(user);
        if(user.getRole()==1){
            User user2 = userService.findUserByUsername(user.getUsername());
            user.setUid(user2.getUid());
            userService.addFile(user);
            return "用户添加成功1";
        }
        return "用户添加成功2";
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User loginUser) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.loginUser(loginUser.getUsername(), loginUser.getPassword());
        if (success) {
            String token = JwtUtil.generateToken(loginUser.getUsername());
            User user = userService.findUserByUsername(loginUser.getUsername());
            int role = user.getRole();
            result.put("success", true);
            result.put("message", role);
            result.put("token", token);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @GetMapping("/menu")
    public CommonResult<?> getUserMenu(@RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User userInfo = userService.getUserInfoByToken(token);
            int role = userInfo.getRole();
            List<Map<String,String>> menulist =  userService.getMenuList(role);
//            String
            return CommonResult.success(menulist);
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            return CommonResult.error(400,"身份识别出现错误");
        }
    }
    @GetMapping("/list")
    public CommonResult<?> getUserList(@RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User userInfo = userService.getUserInfoByToken(token);
            int role = userInfo.getRole();
            if(role==2){
                List<User> userList = userService.getUserList();
                return CommonResult.success(userList);
            }
            else{
                return CommonResult.error(401,"身份认证失败");
            }
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            return CommonResult.error(400,"身份识别出现错误");
        }
    }

    @GetMapping("/info")
    public CommonResult<?> getUserInfo(@RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User userInfo = userService.getUserInfoByToken(token);
//            return ResponseEntity.ok(userInfo);
            return CommonResult.success(userInfo);
        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            return CommonResult.error(400,"身份识别出现错误");
        }
    }

    @PostMapping("/update")
    public CommonResult<?> update(@RequestHeader("Authorization") String ACCESS_TOKEN, @RequestBody User user) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User userInfo = userService.getUserInfoByToken(token);
            user.setUsername(userInfo.getUsername());
            userService.update(user);
            return CommonResult.success(user);
        } catch (Exception e) {
            return CommonResult.error(400,"身份识别出现错误");
        }
    }
}