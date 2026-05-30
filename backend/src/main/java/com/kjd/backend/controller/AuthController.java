package com.kjd.backend.controller;

import com.kjd.backend.common.Result;
import com.kjd.backend.dto.LoginDTO;
import com.kjd.backend.security.AuthUser;
import com.kjd.backend.security.DemoUserService;
import com.kjd.backend.security.JwtService;
import com.kjd.backend.service.RedisSupportService;
import com.kjd.backend.vo.LoginVO;
import com.kjd.backend.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final DemoUserService userService;
    private final JwtService jwtService;
    private final RedisSupportService redisSupportService;

    public AuthController(DemoUserService userService, JwtService jwtService, RedisSupportService redisSupportService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.redisSupportService = redisSupportService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto, HttpServletRequest request) {
        String username = dto == null ? null : dto.getUsername();
        String password = dto == null ? null : dto.getPassword();
        String clientIp = getClientIp(request);
        if (redisSupportService.isLoginLocked(username, clientIp)) {
            return Result.fail("登录失败次数过多，请" + redisSupportService.getLoginLockSeconds() / 60 + "分钟后再试");
        }
        AuthUser user = userService.authenticate(username, password);
        if (user == null) {
            Integer remaining = redisSupportService.recordLoginFailure(username, clientIp);
            if (remaining != null && remaining <= 0) {
                return Result.fail("登录失败次数过多，请" + redisSupportService.getLoginLockSeconds() / 60 + "分钟后再试");
            }
            if (remaining != null) {
                return Result.fail("用户名或密码错误，还可尝试" + remaining + "次");
            }
            return Result.fail("用户名或密码错误");
        }
        redisSupportService.clearLoginFailures(username, clientIp);

        LoginVO vo = new LoginVO();
        vo.setAccessToken(jwtService.createToken(user.getUsername()));
        vo.setTokenType("Bearer");
        vo.setExpiresIn(jwtService.getExpiresInSeconds());
        vo.setUser(user.getUserInfo());
        return Result.success(vo);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success(null);
    }

    @GetMapping("/me")
    public Result<UserInfoVO> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUser)) {
            return Result.fail("Login expired");
        }
        AuthUser user = (AuthUser) authentication.getPrincipal();
        return Result.success(user.getUserInfo());
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}
