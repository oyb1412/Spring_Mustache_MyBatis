package kr.co.myproject.Mapper;

import kr.co.myproject.dto.Comment.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper {
    //findbyid
    @Select("SELECT id, content, parent_comment_id FROM comment WHERE id=#{id}")
    public CommentDto findById(Long id);
    //save
    //delete
    //findall
    //
}
