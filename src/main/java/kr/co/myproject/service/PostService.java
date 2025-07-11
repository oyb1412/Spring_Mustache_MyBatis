package kr.co.myproject.service;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
import kr.co.myproject.repisitory.BoardRepository;
import kr.co.myproject.repisitory.PostRepository;
import kr.co.myproject.repisitory.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    @Transactional
    public Map<String, Object> register(PostRegisterDto dto, SessionUser user)
    {
        User realUser = userRepository.findById(user.getId()).orElseThrow();
        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow();

        String filePath = null;
        Post post = dto.buildPostEntity(realUser, board);

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            
            String filename = fileUploadService.saveFile(dto.getFile());

            if(filename == null)
            {
                return Map.of("success", false, "message", "파일 저장 실패");
            }
 
            filePath = "/files/" + filename;
            post.setFile(filePath, dto.getFile().getOriginalFilename());
        }
        else
        {
            post.setFile(null, null);
        }


        //board가 notice면, post의 isnotice 설정해야함.
        if(board.getCategory() == BoardCategory.NOTICE)
        {
            post.SetNotice();
        }

        board.setPost(post);
        realUser.setPost(post);
        postRepository.save(post);
        return Map.of("success", true, "message", "게시글 작성에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> modify(PostModifyDto dto)
    {
        Post post = postRepository.findById(dto.getId()).orElseThrow();
        
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
        
        post.modify(dto.getTitle(), dto.getContent(), filePath, originalName);

        return Map.of("success", true, "message", "게시글 수정에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> delete(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow();
        Board board = boardRepository.findById(post.getBoard().getId()).orElseThrow();

        //1.기존 content이미지 삭제
        if (post.getContent() != null) {
            fileUploadService.deleteImagesFromHtml(post.getContent(), "C:/JavaProject");
        }
        board.deletePost();
        postRepository.delete(post);

        return Map.of("success", true, "message", "게시글 삭제에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> upLikeCount(Long id, SessionUser sessionUser)
    {
        Post post = postRepository.findById(id).orElseThrow();
        User user = userRepository.findById(sessionUser.getId()).orElseThrow();

        userService.upExp(id, "post");

        //내 글엔 좋싫 불가
        if(post.getUser().getId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신이 작성한 글엔 할 수 없습니다");
        }

        //한번 누른 글엔 좋싫 불가
        if(user.getLikedPostIds().contains(id))
        {
            return Map.of("success", false, "message", "이미 좋아요나 싫어요를 누른 글입니다");
        }

        //user entity에 좋싫 기록 남기기
        user.setLikedPostId(id);

        //Like Up
        post.upLikeCount();

        return Map.of("success", true, "message", "좋아요를 눌렀습니다");
    }

    @Transactional
    public Map<String, Object> downLikeCount(Long id, SessionUser sessionUser)
    {
        Post post = postRepository.findById(id).orElseThrow();
        User user = userRepository.findById(sessionUser.getId()).orElseThrow();

        userService.downExp(id, "post");

        //내 글엔 좋싫 불가
        if(post.getUser().getId().equals(user.getId()))
        {
            return Map.of("success", false, "message", "자신이 작성한 글엔 할 수 없습니다");
        }

        //한번 누른 글엔 좋싫 불가
        if(user.getLikedPostIds().contains(id))
        {
            return Map.of("success", false, "message", "이미 좋아요나 싫어요를 누른 글입니다");
        }

        //user entity에 좋싫 기록 남기기
        user.setLikedPostId(id);

        //Like Up
        post.downLikeCount();

        return Map.of("success", true, "message", "싫어요를 눌렀습니다");
    }

    @Transactional
    public Long upViewCount(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow();

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // 핫 여부 갱신 조건 확인
        if (Duration.between(post.getCheckDate(), now).toHours() >= 1) {
        if (post.getRecentViewCount() >= 10) {
            post.setHot(true);
        } else {
            post.setHot(false);
        }

        // 초기화
        post.setRecentViewCount(0);
        post.setCheckDate(now);
    }

        post.upViewCount();

        return post.getId();
    }

    @Transactional
    public void updatePostState()
    {
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            if (post.isNew() && post.getCreatedDate().isBefore(LocalDateTime.now().minusHours(24))) {
                post.setNew(false);
                postRepository.save(post);
            }
        }
    }

    public List<SidebarNoticeDto> findSidebarNoticeList()
    {
        List<Post> posts = postRepository.findTop3ByBoardIdOrderByCreatedDateDesc(7L);
        List<SidebarNoticeDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            dtos.add(new SidebarNoticeDto(post.getId(), post.getTitle(), post.getCreatedDate()));
        }

        return dtos;
    }

    public List<PopularPostListDto> findPopularTop5Post()
    {
        List<Post> posts = postRepository.findTop5ByOrderByViewCountDesc();
        List<PopularPostListDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            dtos.add(new PopularPostListDto(post.getId(), post.getTitle(), post.getBoard().getName(), post.getViewCount()));
        }

        return dtos;
    }

    public List<PostDto> findPopularPosts()
    {
        List<Post> posts = postRepository.findTop10ByOrderByViewCountDesc();

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> findAllByBoardId(Long boardId)
    {
        List<Post> posts = postRepository.findAllByBoardId(boardId);

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> findAll()
    {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public List<PostDto> search(String keyword)
    {
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);

        return posts.stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
    }

    public Page<PostDto> findAll(Pageable aPageable, String searchType, String searchKeyword)
    {
        Page<Post> posts;
        switch (searchType) {
            case "title":
                posts = postRepository.findByTitleContaining(searchKeyword, aPageable);
                break;
            case "content":
                posts = postRepository.findByContentContaining(searchKeyword, aPageable);
                break;
            case "view":
                posts = postRepository.findByViewCount(searchKeyword, aPageable);
                break;
            default:
                posts = postRepository.findAll(aPageable);
                break;
        }

        return posts.map(PostDto::new); 
    }

   

    public PostDto findById(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow();

        return new PostDto(post);
    }

    public List<CommentDto> findComments(Long id)
    {
        Post post = postRepository.findById(id).orElseThrow();

        List<Comment> comments = post.getComments();
        List<CommentDto> commentDto = new ArrayList<>();

        for (Comment item : comments) {
            commentDto.add(new CommentDto(item, true));
        }
        return commentDto;
    }

    

}
