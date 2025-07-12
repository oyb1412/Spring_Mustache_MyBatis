package kr.co.myproject.Mapper;

import kr.co.myproject.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM 'user' WHERE id=#{id}")
    public User findById(@Param("id") Long id);

    @Select("SELECT * FROM 'user' WHERE nickname=#{nickname}")
    public User findByNickname(@Param("nickname") String nickname);

    @Select("SELECT * FROM 'user' WHERE username=#{username}")
    public User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM 'user'")
    public List<User> findAll();

    @Select("SELECT * FROM 'user' WHERE provider =#{provider} AND providerId = #{providerId}")
    public User findByProviderAndProviderId(@Param("provider") String provider, @Param("providerId") String providerId);

    @Insert("INSERT INTO 'user'(username, password, nickname, find_password_question, find_password_answer, provider, provider_id) VALUES (#{user.userName}, #{user.password}, #{user.nickName}, #{user.findPasswordQuestion}, #{user.findPasswordQuestionAnswer}, #{user.provider}, #{user.providerId})")
    public int insert(@Param("user") User user);

    @Update("UPDATE 'user' SET prifile_image_base64 =#{image} WHERE id=#{id}")
    public int updateProfileImageBase64(@Param("image") String image, @Param("id")  Long id);

    @Update("UPDATE 'user' SET password =#{user.password}, nickname=#{user.nickname} WHERE id=#{user.id}")
    public int update(@Param("user") User user);

    @Update("UPDATE 'user' SET password =#{password}, nickname=#{nickname} WHERE id=#{id}")
    public int update(@Param("password") String password,  @Param("nickname") String nickname, @Param("id") Long id);

    @Update("UPDATE 'user' SET password =#{password} WHERE id=#{id}")
    public int update(@Param("password") String password,   @Param("id") Long id);

    @Update("UPDATE 'user' SET exp = exp + 1 WHERE id =#{id}")
    public int upExp(@Param("id") Long id);

    @Update("UPDATE 'user' SET exp = exp - 1 WHERE id =#{id}")
    public int downExp(@Param("id") Long id);

    @Update("UPDATE 'user' SET ban = #{trigger} WHERE id=#{id}")
    public int ban(@Param("trigger") boolean trigger, @Param("id") Long id);

    @Delete("DELETE FROM 'user' WHERE id=#{id}")
    public int delete(@Param("id") Long id);


}
