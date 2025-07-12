package kr.co.myproject.service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.myproject.Mapper.*;
import org.springframework.stereotype.Service;

import kr.co.myproject.enums.BoardCategory;
import kr.co.myproject.dto.Comment.CommentDto;
import kr.co.myproject.dto.Post.PopularPostListDto;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.dto.Post.PostModifyDto;
import kr.co.myproject.dto.Post.PostRegisterDto;
import kr.co.myproject.dto.Post.SidebarNoticeDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.entity.Board;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final BoardMapper boardMapper;
    private final LikeMapper likeMapper;
    private final FileUploadService fileUploadService;
    private final CommentMapper commentMapper;

    @Transactional
    public Map<String, Object> register(PostRegisterDto dto, SessionUser user)
    {
        User realUser = userMapper.findById(user.getId());
        Board board = boardMapper.findById(dto.getBoardId());

        String filePath = null;
        Post post = dto.buildPostEntity(realUser, board);

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            
            String filename = fileUploadService.saveFile(dto.getFile());

            if(filename == null)
            {
                return Map.of("success", false, "message", "파일 저장 실패");
            }
 
            filePath = "/files/" + filename;
            postMapper.updateFile(dto.getFile().getOriginalFilename(), filePath, post.getId());
        }
        else
        {
            postMapper.updateFile(null, null, post.getId());
        }


        //board가 notice면, post의 isnotice 설정해야함.
        if(board.getCategory() == BoardCategory.NOTICE)
        {
            postMapper.updateNotice(true, post.getId());
        }

        postMapper.update(post);
        return Map.of("success", true, "message", "게시글 작성에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> modify(PostModifyDto dto)
    {
        Post post = postMapper.findById(dto.getId());
        
        //1.기존 content이미지 삭제
        if (post.getContent() != null) {
            fileUploadService.deleteImagesFromHtml(post.getContent(), "C:/JavaProject");
        }

        //2.기존 첨부파일 삭제 (존재한다면)
        if (post.getFilePath() != null && !post.getFilePath().isEmpty()) {
            File oldFile = new File(post.getFilePath());
            if (oldFile.exists()) oldFile.delete();
        }

        //3.새 첨부파일 저장(존재한다면)
        String filePath = null;
        String originalName = null;
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            String filename = fileUploadService.saveFile(dto.getFile());
            if (filename == null) {
                return Map.of("success", false, "message", "파일 저장 실패");
            }
            filePath = "/files/" + filename;
            originalName = dto.getFile().getOriginalFilename();
        }

        postMapper.update(dto.getTitle(), dto.getContent(), filePath, originalName, post.getId());

        return Map.of("success", true, "message", "게시글 수정에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> delete(Long id)
    {
        Post post = postMapper.findById(id);
        Board board = boardMapper.findById(post.getBoardId());

        //1.기존 content이미지 삭제
        if (post.getContent() != null) {
            fileUploadService.deleteImagesFromHtml(post.getContent(), "C:/JavaProject");
        }
        postMapper.delete(post.getId());
        return Map.of("success", true, "message", "게시글 삭제에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> upLikeCount(Long id, SessionUser sessionUser)
    {
        Post post = postMapper.findById(id);
        User user = userMapper.findById(sessionUser.getId());

        userMapper.upExp(id);

        //내 글엔 좋싫 불가
        if(post.getUserId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신이 작성한 글엔 할 수 없습니다");
        }

        //한번 누른 글엔 좋싫 불가
        if(likeMapper.count(user.getId(), id) != 0)
        {
            return Map.of("success", false, "message", "이미 좋아요나 싫어요를 누른 글입니다");
        }

        //user entity에 좋싫 기록 남기기
        likeMapper.insert(user.getId(), id);

        //Like Up
        postMapper.upLikeCount(id);

        return Map.of("success", true, "message", "좋아요를 눌렀습니다");
    }

    @Transactional
    public Map<String, Object> downLikeCount(Long id, SessionUser sessionUser)
    {
        Post post = postMapper.findById(id);
        User user = userMapper.findById(sessionUser.getId());

        userMapper.downExp(sessionUser.getId());

        //내 글엔 좋싫 불가
        if(post.getUserId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신이 작성한 글엔 할 수 없습니다");
        }

        //한번 누른 글엔 좋싫 불가
        if(likeMapper.count(user.getId(), id) != 0)
        {
            return Map.of("success", false, "message", "이미 좋아요나 싫어요를 누른 글입니다");
        }

        //user entity에 좋싫 기록 남기기

        //Like Up
        postMapper.downLikeCount(id);

        return Map.of("success", true, "message", "싫어요를 눌렀습니다");
    }

    @Transactional
    public Long upViewCount(Long id)
    {
        Post post = postMapper.findById(id);

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 핫 여부 갱신 조건 확인
        if (Duration.between(post.getCheckDate(), now).toHours() >= 1) {
        if (post.getRecentViewCount() >= 10) {
            postMapper.updateHot(true, id);
        } else {
            postMapper.updateHot(false, id);
        }

        // 초기화
        postMapper.setRecentViewCount(id, 0);
        postMapper.setCheckDate(now, id);
    }

        postMapper.upViewCount(id);
        return post.getId();
    }

    @Transactional
    public void updatePostState()
    {
        List<Post> posts = postMapper.findAll();

        for (Post post : posts) {
            if (post.isNew() && post.getCreatedDate().isBefore(LocalDateTime.now().minusHours(24))) {
                postMapper.updateNotice(false, post.getId());
            }
        }
    }

    public List<SidebarNoticeDto> findSidebarNoticeList()
    {
        List<Post> posts = postMapper.findTop3ByBoardIdOrderByCreatedDateDesc(7L);
        List<SidebarNoticeDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            dtos.add(new SidebarNoticeDto(post.getId(), post.getTitle(), post.getCreatedDate()));
        }

        return dtos;
    }

    public List<PopularPostListDto> findPopularTop5Post()
    {
        List<Post> posts = postMapper.findTop5ByOrderByViewCountDesc();
        List<PopularPostListDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            Board board =  boardMapper.findById(post.getBoardId());
            dtos.add(new PopularPostListDto(post.getId(), post.getTitle(), board.getName(), post.getViewCount()));
        }

        return dtos;
    }

    public List<PostDto> findPopularPosts()
    {
        List<Post> posts = postMapper.findTop10ByOrderByViewCountDesc();

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> findAllByBoardId(Long boardId)
    {
        List<Post> posts = postMapper.findByBoardId(boardId);

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> findAll()
    {
        List<Post> posts = postMapper.findAll();
        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> search(String keyword)
    {
        List<Post> posts = postMapper.findByTitleContainingOrContentContaining(keyword);

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public PostDto findById(Long id)
    {
        Post post = postMapper.findById(id);

        return new PostDto(post);
    }

    public List<CommentDto> findComments(Long id)
    {
        Post post = postMapper.findById(id);
        List<Comment> comments = commentMapper.findByPostId(id);
        List<CommentDto> commentDto = new ArrayList<>();

        for (Comment item : comments) {
            commentDto.add(new CommentDto(item, true));
        }
        return commentDto;
    }
}
