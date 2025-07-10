package kr.co.myproject.security;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.myproject.Util.BannedUserException;
import kr.co.myproject.Util.CustomOAuth2UserDetails;
import kr.co.myproject.Util.CustomUserDetails;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration // 이 클래스가 스프링 설정 클래스임을 나타냄
@EnableWebSecurity // 스프링 세큐리티 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2SuccessHandler OAuth2SuccessHandler;
	// 스프링 세큐리티의 핵심 필터 체인 설정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

		http
            .csrf(csrf -> csrf.disable()) // CSRF(사이트 위조 공격 방지) 기능 비활성화
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // X-Frame-Options 비활성화(h2콘솔 사용 가능화)
            .authorizeHttpRequests(auth -> auth // 인증 및 인가 관련 처리 설정
                .requestMatchers("/", "/css/**", "/js/**", "/files/**", "/userLogin-page", "/userLogin", "/userRegister-page", "/api/user/register", "/findPassword-page","/resetPassword-page", "/api/user/findPassword", "/search").permitAll() // 여기에 등록된 리소스는 인증(로그인) 없이 접근 허용
				.requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated() // 나머지 경로는 모두 인증(로그인) 필요
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트 할 경로
				.logoutUrl("/userLogout") // 로그아웃 POST 처리 요청 경로
            )
            .formLogin(login -> login
				.loginPage("/userLogin-page") // 로그인 페이지 경로
				.loginProcessingUrl("/userLogin") // 로그인 POST 처리 요청 경로
				.usernameParameter("username") // 세큐리티가 인식할 수 있는 name값
				.passwordParameter("password") // 세큐리티가 인식할 수 있는 name값
				.successHandler(authenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler())
			).oauth2Login(oauth -> oauth
				.loginPage("/userLogin-page")
				.userInfoEndpoint(user -> user
					.userService(customOAuth2UserService))
				.successHandler(oAuth2LoginSuccessHandler())
			);	

	// 설정된 http객체를 반환
    return http.build();
}
	
	@Bean
	public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
    return new SimpleUrlAuthenticationSuccessHandler() {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {

            	// CustomOAuth2User에서 User 꺼냄
            	CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails) authentication.getPrincipal();

            	// 세션에 등록
            	SessionUser sessionUser = new SessionUser(oAuth2User.getUser());
            	request.getSession().setAttribute("user", sessionUser);

            	super.onAuthenticationSuccess(request, response, authentication);
        	}
    	};
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler()
	{
		return new SimpleUrlAuthenticationSuccessHandler()
		{
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request,
												HttpServletResponse response,
												Authentication authentication) throws IOException, ServletException
				{
					CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

					SessionUser sessionUser = new SessionUser(userDetails.getUser());

					request.getSession().setAttribute("user", sessionUser);
					super.onAuthenticationSuccess(request, response, authentication);
				}
		};
	}

	@Bean
public AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler() {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request,
                                            HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {

            String errorMessage;

            if (exception instanceof InternalAuthenticationServiceException) {
    			Throwable cause = exception.getCause();

    			if (cause instanceof UsernameNotFoundException) {
        			errorMessage = "존재하지 않는 계정입니다.";
    			} else if (cause instanceof BannedUserException) {
        			errorMessage = cause.getMessage();
    			} else {
        			errorMessage = "로그인 실패 (내부 오류)";
    			}

			} else if (exception instanceof BadCredentialsException) {
    			errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
			} else {
    			errorMessage = "알 수 없는 로그인 오류";
			}

			String message = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

            // 에러 메시지를 쿼리 파라미터로 넘겨주기
            String redirectUrl = "/userLogin-page?error=true&message=" + message;
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    };
}

	

	// 폼 로그인 시, 스프링이 DB에 저장된 비밀번호와 입력 비밀번호를 체크하기 위한 인코더
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
