package kr.co.myproject.Mapper;

import kr.co.myproject.entity.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BoardMapper {


    @Select("SELECT * FROM board WHERE id=#{id}")
    public Board findById(@Param("id") Long id);

    @Select("SELECT * FROM board")
    public List<Board> findAll();

    @Select("SELECT * FROM board ORDER BY today_post_count DESC LIMIT 3")
    public List<Board> findTop3ByOrderByTodayPostCountDesc();

    @Update("UPDATE board SET today_post_count = today_post_count + 1 WHERE id=#{id} ")
    public int updateTodayPostCount(@Param("id") Long id);

    @Update("UPDATE board SET total_post_count = total_post_count + 1 WHERE id=#{id} ")
    public int updateTotalPostCount(@Param("id") Long id);

    @Update("UPDATE board SET today_post_count = #{todayPostCount} WHERE id =#{id}")
    public int updateTodayPostCount(@Param("id") Long id, @Param("todayPostCount") int todayPostCount);

    @Update("UPDATE board SET total_post_count = #{totalPostCount} WHERE id =#{id}")
    public int updateTotalPostCount(@Param("id") Long id, @Param("totalPostCount") int totalPostCount);
}
