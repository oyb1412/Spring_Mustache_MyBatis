package kr.co.myproject.Util;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.myproject.controller.enums.Role;
import kr.co.myproject.entity.User;
// 세큐리티가 사용하는 사용자 정보 객체
// 폼 로그인 성공 시, SecurityContext에 이 객체가 저장됨
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user)
    {
        this.user = user;
    }

    public User getUser()
    {
        return this.user;
    }

    //유저의 권한을 세큐리티가 인식할 수 있는 형태로 변환
    //ROLE_ 접두어가 필수
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        return Collections.singleton(() -> "ROLE_" + role);
    }

    // 폼 로그인 시, 세큐리티가 이 값을 기반으로 DB에서 조회
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 폼 로그인 시, 세큐리티가 이 값을 기반으로 DB에서 조회
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
}
