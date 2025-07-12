package kr.co.myproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.myproject.Mapper.CommentMapper;
import kr.co.myproject.Mapper.PostMapper;
import kr.co.myproject.Mapper.UserMapper;
import org.springframework.stereotype.Service;

import kr.co.myproject.dto.Comment.CommentDeleteDto;
import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.dto.Comment.CommentRegisterDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Transactional
    public Map<String, Object> register(CommentRegisterDto dto, SessionUser user)
    {
        User realUser = userMapper.findById(user.getId());
        Comment comment;

        //포스트에 댓글 달기

            Post post = postMapper.findById(dto.getPostId());

            //일반 댓글
            if(dto.getParentCommentId() == null)
            {
                comment = dto.buildCommentEntity(post, realUser, null);
            }
            //답글
            else
            {
                Comment parentComment = commentMapper.findById(dto.getParentCommentId());

                //답글에 부모 등록
                comment = dto.buildCommentEntity(post, realUser, parentComment);
            }

        commentMapper.insert(comment);

        return Map.of("success", true, "message", "댓글 작성에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> delete(CommentDeleteDto dto, SessionUser sessionUser)
    {
        Comment comment = commentMapper.findById(dto.getId());
        User user = userMapper.findById(sessionUser.getId());
        User commentUser = userMapper.findById(comment.getUserId());
        if(!commentUser.getId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신의 댓글만 삭제할 수 있습니다");
        }

        if(dto.getType().equals("post"))
        {
            //post쪽 comment list에서 수동 제거
            Post post = postMapper.findById(dto.getPostId());
        }

        commentMapper.delete(comment.getId());
        return Map.of("success", true, "message", "댓글 삭제에 성공했습니다");
    }

    public List<CommentDto> findAll()
    {
        List<Comment> comment = commentMapper.findAll();
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment item : comment) {
            User user = userMapper.findById(item.getUserId());
            Comment parentComment = commentMapper.findById(item.getParentCommentId());
            commentDto.add(new CommentDto(item, parentComment, user.getNickname()));
        }
       
        return commentDto;
    }

    public CommentDto findById(Long id)
    {
        Comment comment = commentMapper.findById(id);
        return new CommentDto(comment, true);
    }


    
}
