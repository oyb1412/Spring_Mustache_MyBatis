package kr.co.myproject.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import kr.co.myproject.Util.CustomOAuth2UserDetails;
import kr.co.myproject.controller.enums.FindPasswordQuestion;
import kr.co.myproject.controller.enums.Role;
import kr.co.myproject.entity.User;
import kr.co.myproject.repisitory.UserRepository;
import kr.co.myproject.security.OAuthAttributes;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 로직
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // 구글 or 네이버 구분
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, naver
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 유저 정보 파싱
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(provider, attributes);
        String providerId = oAuthAttributes.getProviderId();

        // 유저 존재 여부 확인
        Optional<User> userOpt = userRepository.findByProviderAndProviderId(provider, providerId);

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get(); // 기존 유저 로그인
        } else {
            user = createTempUser(oAuthAttributes); // 임시 유저 생성
            userRepository.save(user);
        }

        OAuth2User defaultOAuth2User = new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            attributes,
            "sub" // 또는 "email", provider에 따라
        );

        return new CustomOAuth2UserDetails(defaultOAuth2User, user);
    }

    private User createTempUser(OAuthAttributes attr) {
        return User.builder()
                .provider(attr.getProvider())
                .password("")
                .providerId(attr.getProviderId())
                .username(attr.getName())
                .nickname(attr.getName())
                .findPasswordQuestion(FindPasswordQuestion.BIRTH_PLACE)
                .findPasswordQuestionAnswer("")
                .role(Role.MEMBER)
                .build();
    }
}

