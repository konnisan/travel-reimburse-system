package com.kjd.backend.controller;

import com.kjd.backend.common.Result;
import com.kjd.backend.dto.LoginDTO;
import com.kjd.backend.security.AuthUser;
import com.kjd.backend.security.DemoUserService;
import com.kjd.backend.security.JwtService;
import com.kjd.backend.vo.LoginVO;
import com.kjd.backend.vo.UserInfoVO;
import org.springframework.security.core.Authentication;
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

    public AuthController(DemoUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        String username = dto == null ? null : dto.getUsername();
        String password = dto == null ? null : dto.getPassword();
        AuthUser user = userService.authenticate(username, password);
        if (user == null) {
            return Result.fail("Username or password is incorrect");
        }

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
}
