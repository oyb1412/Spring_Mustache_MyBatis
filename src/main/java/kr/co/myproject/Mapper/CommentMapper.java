package kr.co.myproject.Mapper;

import kr.co.myproject.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment(post_id, user_id, board_id, parent_comment_id, content) VALUES (#{comment.postId}, #{comment.userId}, #{comment.boardId}, #{comment.parentCommentId}, #{comment.content})")
    public int insert(Comment comment);

    @Select("SELECT * FROM comment WHERE id=#{id}")
    public Comment findById(@Param("id") Long id);

    @Select("SELECT * FROM comment WHERE user_id=#{userId}")
    public List<Comment> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM comment WHERE post_id=#{postId}")
    public List<Comment> findByPostId(@Param("postId") Long postId);

    @Select("SELECT * FROM comment WHERE board_id=#{boardId}")
    public List<Comment> findByBoardId(@Param("boardId") Long boardId);

    @Select("SELECT * FROM comment")
    public List<Comment> findAll();

    @Update("UPDATE comment SET parent_comment_id =#{parentComment.id} WHERE id = #{id}")
    public int updateParentComment(@Param("parentComment") Comment parentComment, @Param("id") Long id);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    public int delete(@Param("id") Long id);
}
