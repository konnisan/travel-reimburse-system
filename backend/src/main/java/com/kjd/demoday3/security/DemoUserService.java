package com.kjd.demoday3.security;

import com.kjd.demoday3.vo.UserInfoVO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DemoUserService {
    private final Map<String, AuthUser> users = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    public DemoUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        addUser("1", "admin", "admin123", "System Admin",
                Arrays.asList("ADMIN"),
                Arrays.asList("reimburse:list", "reimburse:view", "reimburse:create", "reimburse:edit",
                        "reimburse:save", "reimburse:submit", "reimburse:invalid", "user:manage"));
        addUser("2", "finance", "finance123", "Finance User",
                Arrays.asList("FINANCE"),
                Arrays.asList("reimburse:list", "reimburse:view", "reimburse:edit",
                        "reimburse:save", "reimburse:submit", "reimburse:invalid"));
        addUser("3", "employee", "employee123", "Employee",
                Arrays.asList("EMPLOYEE"),
                Arrays.asList("reimburse:list", "reimburse:view", "reimburse:create", "reimburse:edit", "reimburse:save"));
    }

    public AuthUser findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return users.get(username);
    }

    public AuthUser authenticate(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        AuthUser user = findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    private void addUser(String id, String username, String rawPassword, String displayName,
                         List<String> roles, List<String> permissions) {
        UserInfoVO info = new UserInfoVO();
        info.setId(id);
        info.setUsername(username);
        info.setDisplayName(displayName);
        info.setRoles(roles);
        info.setPermissions(permissions);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList()));
        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        users.put(username, new AuthUser(info, passwordEncoder.encode(rawPassword), authorities));
    }
}
