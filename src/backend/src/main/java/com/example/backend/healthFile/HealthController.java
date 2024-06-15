package com.example.backend.healthFile;
import com.example.backend.CommonResult;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthRecord")
public class HealthController {

    private final HealthRecordService healthRecordService;
    private final UserService userService;

    @Autowired
    public HealthController(HealthRecordService healthRecordService, UserService userService) {
        this.healthRecordService = healthRecordService;
        this.userService = userService;
    }

    @GetMapping("/getHealthRecord")
    public CommonResult<?> getHealthRecord(@RequestHeader("Authorization") String accessToken) {
        try {
            String token = accessToken.substring(7); // Assuming "Bearer " prefix
            User user = userService.getUserInfoByToken(token);
            if (user != null) {
                HealthRecord healthRecord = healthRecordService.getHealthRecordByUid(user.getUid());
                if (healthRecord != null) {
                    return CommonResult.success(healthRecord);
                } else {
                    return CommonResult.error(404, "未找到健康档案");
                }
            } else {
                return CommonResult.error(401, "未授权");
            }
        } catch (Exception e) {
            return CommonResult.error(401, "身份识别出现错误");
        }
    }

    @GetMapping("/getRecord")
    public CommonResult<?> getHealthRecordByUsername(@RequestHeader("Authorization") String accessToken, @RequestParam String username) {
        try {
            String token = accessToken.substring(7); // Assuming "Bearer " prefix
            User user = userService.getUserInfoByToken(token);
            if (user != null) {
                User requestedUser = userService.findUserByUsername(username);
                if (requestedUser != null) {
                    HealthRecord healthRecord = healthRecordService.getHealthRecordByUid(requestedUser.getUid());
                    if (healthRecord != null) {
                        return CommonResult.success(healthRecord);
                    } else {
                        return CommonResult.error(404, "未找到健康档案");
                    }
                } else {
                    return CommonResult.error(404, "未找到用户");
                }
            } else {
                return CommonResult.error(401, "未授权");
            }
        } catch (Exception e) {
            return CommonResult.error(401, "身份识别出现错误");
        }
    }

    @PostMapping("/updateRecord")
    public CommonResult<?> updateHealthRecord(@RequestHeader("Authorization") String accessToken, @RequestBody HealthRecord healthRecord) {
        try {
            String token = accessToken.substring(7); // Assuming "Bearer " prefix
            User user = userService.getUserInfoByToken(token);
            if (user != null) {
                boolean updated = healthRecordService.updateHealthRecord(healthRecord);
                if (updated) {
                    return CommonResult.success("健康档案更新成功");
                } else {
                    return CommonResult.error(500, "更新健康档案失败");
                }
            } else {
                return CommonResult.error(401, "未授权");
            }
        } catch (Exception e) {
            return CommonResult.error(401, "身份识别出现错误");
        }
    }
}
