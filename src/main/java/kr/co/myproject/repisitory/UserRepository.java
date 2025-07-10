package kr.co.myproject.repisitory;


import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import kr.co.myproject.entity.User;

// User 엔티티에 대한 DB 접근 로직을 담당하는 인터페이스 (JPA가 자동으로 다룸)
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByNickname(String nickname);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

}
