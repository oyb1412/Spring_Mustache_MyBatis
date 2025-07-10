package kr.co.myproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.co.myproject.dto.Comment.CommentDeleteDto;
import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.dto.Comment.CommentRegisterDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import kr.co.myproject.repisitory.CommentRepository;
import kr.co.myproject.repisitory.PostRepository;
import kr.co.myproject.repisitory.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Map<String, Object> register(CommentRegisterDto dto, SessionUser user)
    {
        User realUser = userRepository.findById(user.getId()).orElseThrow();
        Comment comment;

        //포스트에 댓글 달기

            Post post = postRepository.findById(dto.getPostId()).orElseThrow();

            //일반 댓글
            if(dto.getParentCommentId() == null)
            {
                comment = dto.buildCommentEntity(post, realUser, null);
            }
            //답글
            else
            {
                Comment parentComment = commentRepository.findById(dto.getParentCommentId()).orElseThrow();

                //답글에 부모 등록
                comment = dto.buildCommentEntity(post, realUser, parentComment);

                //원글에 자식 등록
               parentComment.SetChildComment(comment);
            }

        realUser.setComment(comment);

        commentRepository.save(comment);

        return Map.of("success", true, "message", "댓글 작성에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> delete(CommentDeleteDto dto, SessionUser sessionUser)
    {
        Comment comment = commentRepository.findById(dto.getId()).orElseThrow();
        User user = userRepository.findById(sessionUser.getId()).orElseThrow();

        if(!comment.getUser().getId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신의 댓글만 삭제할 수 있습니다");
        }

        if(dto.getType().equals("post"))
        {
            //post쪽 comment list에서 수동 제거
            Post post = postRepository.findById(dto.getPostId()).orElseThrow();
            post.getComments().removeIf(c -> c.getId().equals(comment.getId()));
        }

        commentRepository.delete(comment);
        return Map.of("success", true, "message", "댓글 삭제에 성공했습니다");
    }

    public List<CommentDto> findAll()
    {
        List<Comment> comment = commentRepository.findAll();
        List<CommentDto> commentDto = new ArrayList<>();
        for (Comment item : comment) {
            commentDto.add(new CommentDto(item, true));
        }
       
        return commentDto;
    }

    public CommentDto findById(Long id)
    {
        Comment comment = commentRepository.findById(id).orElseThrow();

        return new CommentDto(comment, true);
    }


    
}
