package kr.co.myproject.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.co.myproject.dto.Post.PostDto;
import kr.co.myproject.dto.User.SessionUser;
import kr.co.myproject.dto.User.UserDto;
import kr.co.myproject.dto.User.UserFindPasswordDto;
import kr.co.myproject.dto.User.UserManagementDto;
import kr.co.myproject.dto.User.UserModifyDto;
import kr.co.myproject.dto.User.UserPageDto;
import kr.co.myproject.dto.User.UserProfileImageEdtiDto;
import kr.co.myproject.dto.User.UserRegisterDto;
import kr.co.myproject.dto.User.UserResetPasswordDto;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.entity.Post;
import kr.co.myproject.entity.User;
import kr.co.myproject.repisitory.CommentRepository;
import kr.co.myproject.repisitory.PostRepository;
import kr.co.myproject.repisitory.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;
    

    @Transactional
    public Map<String, Object> register(UserRegisterDto dto)
    {
        //유저 id기반 중복 체크
        if(existsByUsername(dto.getUsername()))
        {
            return Map.of("success", false, "message", "해당 아이디는 사용중입니다");
        }

        //유저 닉네임 기반 중복 체크
        if(existsByNickname(dto.getNickname()))
        {
            return Map.of("success", false, "message", "해당 닉네임은 사용중입니다");
        }

        //1차, 2차 비밀번호 체크
        if(!dto.getPassword().equals(dto.getCheckPassword()))
        {
            return Map.of("success", false, "message", "확인용 비밀번호가 올바르지 않습니다");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userRepository.save(dto.buildUserEntity(encodedPassword));

        return Map.of("success", true, "message", "회원가입 성공");
    }

    @Transactional
    public Map<String, Object> modify(UserModifyDto dto, SessionUser sessionUser)
    {
        User user = userRepository.findById(sessionUser.getId()).orElseThrow();
        //현재 비밀번호/새 비밀번호 일치 체크
        if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword()))
        {
            return Map.of("success", false, "message", "현재 비밀번호와 새 비밀번호가 같습니다");
        }

        //1차, 2차 비밀번호 체크
        if(!dto.getNewPassword().equals(dto.getConfirmPassword()))
        {
            return Map.of("success", false, "message", "재입력한 새 비밀번호가 다릅니다");
        }

        //닉네임 중복 체크
        User userCheck = userRepository.findByNickname(dto.getNewNickname());

        if(userCheck != null)
        {
            return Map.of("success", false, "message", "이미 존재하는 닉네임입니다");
        }

        //비밀번호, 닉네임 업데이트
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        user.modify(newPassword, dto.getNewNickname());

        return Map.of("success", true, "message", "유저 정보 변경에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> withdrawal(Long id, HttpSession session)
    {
        User user = userRepository.findById(id).orElseThrow();
        List<Post> posts = postRepository.findByUser(user);
        List<Comment> comments = commentRepository.findByUser(user);

        for (Post post : posts) {
            post.setUser(null); 
        }

        for (Comment comment : comments) {
            comment.setUser(null); 
        }

        userRepository.delete(user);

        session.invalidate();

        return Map.of("success", true, "message", "탈퇴되었습니다");
    }

    @Transactional
    public Long upExp(Long postId, String type)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(post.getUser().getId()).orElseThrow();

        user.upExp();

        return user.getId();
    }

    @Transactional
    public Long downExp(Long postId, String type)
    {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(post.getUser().getId()).orElseThrow();

        user.downExp();

        return user.getId();
    }

    @Transactional
    public Map<String, Object> ban(Long id, SessionUser sessionUser)
    {
        if(id.equals(sessionUser.getId()))
        {
            return Map.of("success", false, "message", "자기 자신은 밴 할수 없습니다");
        }

        User user = userRepository.findById(id).orElseThrow();
        user.ban(true);
        return Map.of("success", true, "message", "해당 유저를 밴 처리했습니다");
    }

    @Transactional
    public Map<String, Object> unban(Long id, SessionUser sessionUser)
    {
        if(id.equals(sessionUser.getId()))
        {
            return Map.of("success", false, "message", "자기 자신은 밴 할수 없습니다");
        }

        User user = userRepository.findById(id).orElseThrow();

        user.ban(false);
        return Map.of("success", true, "message", "해당 유저를 밴 해제 처리했습니다");
    }

    @Transactional
    public Map<String, Object> findPassword(UserFindPasswordDto dto,
                                            HttpSession session)
    {
        User user = userRepository.findByUsername(dto.getUsername());

        if(user == null)
        {
            return Map.of("success", false, "message", "아이디가 올바르지 않습니다");
        }

        if(!user.getFindPasswordQuestion().equals(dto.getQuestion()))
        {
            return Map.of("success", false, "message", "비밀번호 찾기 힌트가 올바르지 않습니다");
        }

        if(!user.getFindPasswordQuestionAnswer().equals(dto.getQuestionAnswer()))
        {
            return Map.of("success", false, "message", "비밀번호 찾기 힌트가 올바르지 않습니다");
        }

        session.setAttribute("resetUsername", dto.getUsername());
        return Map.of("success", true, "message", "유저 정보가 확인되었습니다");
    }

    @Transactional
    public Map<String, Object> resetPassword(UserResetPasswordDto dto,
                                             HttpSession session)
    {
        String username = (String) session.getAttribute("resetUsername");
        User user = userRepository.findByUsername(username);

        if(user == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        //newp, confirmp 체크
        if(!dto.getNewPassword().equals(dto.getConfirmPassword()))
        {
            return Map.of("success", false, "message", "확인용 비밀번호가 올바르지 않습니다");
        }

        user.ModifyPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

        session.removeAttribute("resetUsername");

        return Map.of("success", true, "message", "비밀번호를 재설정 했습니다");
    }

    @Transactional
    public Map<String, Object> imageUpload(UserProfileImageEdtiDto dto)
    {
        User user = userRepository.findById(dto.getId()).orElseThrow();

        if (user == null) {
            return Map.of("success", false, "message", "유저를 찾을 수 없습니다");
        }

        user.setProfileImageBase64(dto.getProfileImageBase64());

        return Map.of("success", true, "message", "이미지 업로드에 성공했습니다");
    }

    public List<UserManagementDto> findByManagement()
    {
        List<UserManagementDto> userDto = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for(User user : users){
            userDto.add(new UserManagementDto(user));
        }

        return userDto;
    }

    public List<UserPageDto> createUserPageDtos(UserDto user)
    {
        List<UserPageDto> userPageDto = new ArrayList<>();
        if(user != null)
        {
            for (PostDto post : user.getPosts()) {
                userPageDto.add(new UserPageDto(post.getId(), post.getTitle() , post.getCommentsDto()));
            }
        }

        return userPageDto;
    }

    public UserDto findByUsername(String username)
    {
        User user = userRepository.findByUsername(username);

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public UserDto findById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow();

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public UserDto findByNickname(String nickname)
    {
        User user = userRepository.findByNickname(nickname);

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public List<UserDto> findAll()
    {
        List<User> users = userRepository.findAll();

        return users.stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());
    }
       
    public boolean existsByUsername(String username)
    {
        return findByUsername(username) != null;
    }

    public boolean existsByNickname(String nickname)
    {
        return findByNickname(nickname) != null;
    }
}
