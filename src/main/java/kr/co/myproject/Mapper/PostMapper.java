package kr.co.myproject.Mapper;

import kr.co.myproject.entity.Post;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT COUNT(*) FROM post WHERE board_id =#{boardId} AND created_date >= #{start} AND created_date < #{end}")
    public int countTodayPosts(@Param("boardId") Long boardId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("SELECT * FROM post WHERE id=#{id}")
    public Post findById(@Param("id") Long id);

    @Select("SELECT * FROM post")
    public List<Post> findAll();

    @Select("SELECT * FROM post WHERE board_id =#{boardId}")
    public List<Post> findByBoardId(@Param("boardId") Long boardId);

    @Select("SELECT * FROM post WHERE user_id =#{userId}")
    public List<Post> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM post ORDER BY view_count DESC LIMIT 5")
    public List<Post> findTop5ByOrderByViewCountDesc();

    @Select("SELECT * FROM post ORDER BY view_count DESC LIMIT 10")
    public List<Post> findTop10ByOrderByViewCountDesc();

    @Select("SELECT * FROM post WHERE board_id=#{boardId} ORDER BY created_date DESC LIMIT 3")
    public List<Post> findTop3ByBoardIdOrderByCreatedDateDesc(@Param("boardId") Long boardId);

    @Select("SELECT * FROM post  WHERE board_id =#{boardId} ORDER BY created_date DESC LIMIT 5")
    public List<Post> findTop5ByBoardId(@Param("boardId") Long boardId);

    @Select("SELECT * FROM post WHERE title LIKE CONCAT('%', #{keyword}, '%') OR  content LIKE CONCAT('%', #{keyword}, '%')")
    public List<Post> findByTitleContainingOrContentContaining(@Param("keyword") String keyword);

    @Select("SELECT * FROM post WHERE title LIKE CONCAT('%', #{title}, '%')")
    public List<Post> findByTitleContaining(@Param("title") String title);

    @Select("SELECT * FROM post WHERE content LIKE CONCAT('%', #{content}, '%')")
    public List<Post> findByContentContaining(@Param("content") String content);    //findByContentContaining

    @Insert("INSERT INTO post(user_id, board_id, title, content) VALUES (#{post.userId}, #{post.boardId}, #{post.title}, #{post.content})")
    public int insert(@Param("post") Post post);

    @Update("UPDATE post SET title = #{title}, content =#{content}, filePath =#{filePath}, originalName =#{originalName} WHERE id =#{id}")
    public int update(@Param("title") String title, @Param("content") String content, @Param("filePath") String filePath, @Param("originalName") String originalName, @Param("id") Long id);

    @Update("UPDATE post SET title = #{post.title}, content =#{post.content} WHERE id =#{post.id}")
    public int update(@Param("post") Post post);

    @Update("UPDATE post SET file_name =#{fileName}, file_path =#{filePath} WHERE id =#{id}")
    public int updateFile(@Param("fileName")  String fileName, @Param("filePath") String filePath, @Param("id") Long id);

    @Update("UPDATE post SET is_notice =#{trigger} WHERE id=#{id}")
    public int updateNotice(@Param("trigger") Boolean trigger, @Param("id") Long id);

    @Update("UPDATE post SET is_hot =#{trigger} WHERE id=#{id}")
    public int updateHot(@Param("trigger") Boolean trigger, @Param("id") Long id);

    @Update("UPDATE post SET is_new =#{trigger} WHERE id=#{id}")
    public int updateNew(@Param("trigger") Boolean trigger, @Param("id") Long id);

    @Update("UPDATE post SET board_id =#{boardId} WHERE id=#{id}")
    public int updateBoard(@Param("boardId") Long boardId, @Param("id") Long id);

    @Update("UPDATE post SET user_id =#{userId} WHERE id=#{id}")
    public int updateUser(@Param("userId") Long userId, @Param("id") Long id);

    @Update("UPDATE post SET like_count = like_count + 1 WHERE id=#{id}")
    public int upLikeCount(@Param("id") Long id);

    @Update("UPDATE post SET like_count = like_count - 1 WHERE id=#{id}")
    public int downLikeCount(@Param("id") Long id);

    @Update("UPDATE post SET recent_view_count = #{number} WHERE id=#{id}")
    public int setRecentViewCount(@Param("id") Long id, @Param("number") int number );

    @Update("UPDATE post SET view_count = view_count + 1 WHERE id=#{id}")
    public int upViewCount(@Param("id") Long id);

    @Update("UPDATE post SET check_date =#{checkDate} WHERE id=#{id}")
    public int setCheckDate(@Param("checkDate")LocalDateTime checkDate, @Param("id") Long id);

    @Delete("DELETE FROM post WHERE id=#{id}")
    public int delete(@Param("id") Long id);
}
