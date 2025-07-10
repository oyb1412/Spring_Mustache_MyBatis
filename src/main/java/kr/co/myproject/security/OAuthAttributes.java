package kr.co.myproject.security;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private String provider;      // google, naver
    private String providerId;    // 고유 id
    private String name;          // 이름
    private String email;         // 이메일
    private String picture;       // 프로필 사진

    @Builder
    public OAuthAttributes(String provider, String providerId, String name, String email, String picture) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String provider, Map<String, Object> attributes) {
        if (provider.equals("google")) {
            return ofGoogle(attributes);
        }
        
        return null;
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .provider("google")
                .providerId((String) attributes.get("sub")) 
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .build();
    }
}

