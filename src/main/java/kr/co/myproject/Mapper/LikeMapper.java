package kr.co.myproject.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Select("SELECT COUNT(*) FROM liked_post_ids WHERE user_id =#{userId} AND post_id =#{postId}")
    public int count(@Param("userId") Long userId, @Param("postId") Long postId);

    @Insert("INSERT INTO liked_post_ids(user_id, post_id) VALUES (#{userId}, #{postId})")
    public void insert(@Param("userId") Long userId, @Param("postId") Long postId);
}
