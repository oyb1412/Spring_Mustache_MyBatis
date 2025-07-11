package kr.co.myproject.Mapper;

import kr.co.myproject.dto.Board.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {
    @Select("SELECT id, name, description, todayPostCount, totalPostCount, category FROM board WHERE id=#{id}")
    public BoardDto findById(@Param("id") Long id);

    @Select("SELECT id, name, description, todayPostCount, totalPostCount, category FROM board")
    public List<BoardDto> findAll();

    //findTop3ByOrderByTodayPostCountDesc
    //countTodayPosts
}
