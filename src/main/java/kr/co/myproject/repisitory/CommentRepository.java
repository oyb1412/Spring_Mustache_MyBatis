package kr.co.myproject.repisitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser(User user);
}
