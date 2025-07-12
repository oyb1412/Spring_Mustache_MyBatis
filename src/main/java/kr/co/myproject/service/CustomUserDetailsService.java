package kr.co.myproject.service;


import kr.co.myproject.Mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.myproject.Util.BannedUserException;
import kr.co.myproject.Util.CustomUserDetails;
import kr.co.myproject.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService { // 세큐리티가 폼 로그인시 유저 정보 조회를 위해 호출하는 인터페이스
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 로그인 시 입력된 username으로 유저를 DB에서 조회
        // 없으면 예외처리
        User user = userMapper.findByUsername(username);

        if(user == null)
        {
            throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
        }

        if (user.isBan()) {
            throw new BannedUserException("정지된 계정입니다.");
        }

        // 조회된 객체를 세큐리티가 사용할 수 있도록 래핑해서 전달
        return new CustomUserDetails(user); 
    }
}
