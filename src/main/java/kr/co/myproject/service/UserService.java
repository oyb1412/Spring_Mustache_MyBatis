package kr.co.myproject.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.co.myproject.Mapper.CommentMapper;
import kr.co.myproject.Mapper.PostMapper;
import kr.co.myproject.Mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
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
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
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
        userMapper.insert(dto.buildUserEntity(encodedPassword));

        return Map.of("success", true, "message", "회원가입 성공");
    }

    @Transactional
    public Map<String, Object> modify(UserModifyDto dto, SessionUser sessionUser)
    {
        User user = userMapper.findById(sessionUser.getId());
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
        User userCheck = userMapper.findByNickname(dto.getNewNickname());

        if(userCheck != null)
        {
            return Map.of("success", false, "message", "이미 존재하는 닉네임입니다");
        }

        //비밀번호, 닉네임 업데이트
        String newPassword = passwordEncoder.encode(dto.getNewPassword());
        userMapper.update(newPassword, dto.getNewNickname(), sessionUser.getId());

        return Map.of("success", true, "message", "유저 정보 변경에 성공했습니다");
    }

    @Transactional
    public Map<String, Object> withdrawal(Long id, HttpSession session)
    {
        User user = userMapper.findById(id);
        List<Post> posts = postMapper.findByUserId(user.getId());
        List<Comment> comments = commentMapper.findByUserId(user.getId());

        for (Post post : posts) {
            postMapper.delete(post.getId());
        }

        for (Comment comment : comments) {
            commentMapper.delete(comment.getId());
        }

        userMapper.delete(user.getId());

        session.invalidate();

        return Map.of("success", true, "message", "탈퇴되었습니다");
    }

    @Transactional
    public Long upExp(Long postId, String type)
    {
        Post post = postMapper.findById(postId);
        User user = userMapper.findById(post.getId());

        userMapper.upExp(user.getId());

        return user.getId();
    }

    @Transactional
    public Long downExp(Long postId, String type)
    {
        Post post = postMapper.findById(postId);
        User user = userMapper.findById(post.getId());

        userMapper.downExp(user.getId());

        return user.getId();
    }

    @Transactional
    public Map<String, Object> ban(Long id, SessionUser sessionUser)
    {
        if(id.equals(sessionUser.getId()))
        {
            return Map.of("success", false, "message", "자기 자신은 밴 할수 없습니다");
        }

        User user = userMapper.findById(id);
        userMapper.ban(true, user.getId());
        return Map.of("success", true, "message", "해당 유저를 밴 처리했습니다");
    }

    @Transactional
    public Map<String, Object> unban(Long id, SessionUser sessionUser)
    {
        if(id.equals(sessionUser.getId()))
        {
            return Map.of("success", false, "message", "자기 자신은 밴 할수 없습니다");
        }

        User user = userMapper.findById(id);

        userMapper.ban(false, user.getId());
        return Map.of("success", true, "message", "해당 유저를 밴 해제 처리했습니다");
    }

    @Transactional
    public Map<String, Object> findPassword(UserFindPasswordDto dto,
                                            HttpSession session)
    {
        User user = userMapper.findByUsername(dto.getUsername());

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
        User user = userMapper.findByUsername(username);

        if(user == null)
        {
            return Map.of("success", false, "message", "유저 정보가 올바르지 않습니다");
        }

        //newp, confirmp 체크
        if(!dto.getNewPassword().equals(dto.getConfirmPassword()))
        {
            return Map.of("success", false, "message", "확인용 비밀번호가 올바르지 않습니다");
        }

        userMapper.update(user);

        session.removeAttribute("resetUsername");

        return Map.of("success", true, "message", "비밀번호를 재설정 했습니다");
    }

    @Transactional
    public Map<String, Object> imageUpload(UserProfileImageEdtiDto dto)
    {
        User user = userMapper.findById(dto.getId());

        if (user == null) {
            return Map.of("success", false, "message", "유저를 찾을 수 없습니다");
        }

        userMapper.updateProfileImageBase64(dto.getProfileImageBase64(), user.getId());

        return Map.of("success", true, "message", "이미지 업로드에 성공했습니다");
    }

    public List<UserManagementDto> findByManagement()
    {
        List<UserManagementDto> userDto = new ArrayList<>();
        List<User> users = userMapper.findAll();

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
        User user = userMapper.findByUsername(username);

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public UserDto findById(Long id)
    {
        User user = userMapper.findById(id);

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public UserDto findByNickname(String nickname)
    {
        User user = userMapper.findByNickname(nickname);

        if(user == null)
            return null;

        return new UserDto(user);
    }

    public List<UserDto> findAll()
    {
        List<User> users = userMapper.findAll();

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
