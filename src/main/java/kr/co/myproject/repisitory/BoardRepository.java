package kr.co.myproject.repisitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.myproject.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findTop3ByOrderByTodayPostCountDesc();
}
