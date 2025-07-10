package kr.co.myproject.repisitory;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);

    Page<Post> findByContentContaining(String keyword, Pageable pageable);

    Page<Post> findByViewCount(String keyword, Pageable pageable);

    List<Post> findAllByBoardId(Long boardId);

    List<Post> findTop3ByBoardIdOrderByCreatedDateDesc(Long boardId);

    List<Post> findTop5ByOrderByViewCountDesc();

    List<Post> findTop10ByOrderByViewCountDesc();

    List<Post> findByUser(User user);

    List<Post> findByTitleContainingOrContentContaining(String keyword1, String keyword2);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.board.id = :boardId AND p.createdDate >= :todayStart AND p.createdDate < :tomorrowStart")
    int countTodayPosts(@Param("boardId") Long boardId,
                    @Param("todayStart") LocalDateTime todayStart,
                    @Param("tomorrowStart") LocalDateTime tomorrowStart);
}
