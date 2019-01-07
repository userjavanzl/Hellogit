package cn.tedu.store.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import cn.tedu.store.entity.User;

/**
 * 持久层接口 处理用户数据的持久层
 * 
 * @author UID-ECD
 *
 */

public interface UserMapper {
	/**
	 * 插入用户数据
	 * 
	 * @param 用户数据
	 * @return 受影响的行数
	 */

	Integer addnew(User user);

	/**
	 * 根据用户名查询用户数据
	 * 
	 * @param username
	 *            用户名
	 * @return 匹配的用户数据， 如果没有匹配的数据，则返回null
	 */

	User findByUsername(String username);

	/**
	 * 根据登录时的session绑定的id去修改用户的密码
	 * @param uid 用户id
	 * @param password 密码
	 * @param modifiedUser 修改人
	 * @param modifiedTime 修改时间
	 * @return 受影响的行数
	 */
	Integer updatePassword(@Param("uid") Integer uid, @Param("password") String password,
			@Param("modifiedUser") String modifiedUser, @Param("modifiedTime") Date modifiedTime);

	/**
	 * 根据用户id查询用户密码和盐值
	 * 
	 * @param id  用户id
	 * @return 匹配的用户数据，
	 */
	User findById(Integer id);
	
	
	/**
	 *修改用户资料
	 * @param user
	 * @return 返回匹配的用户信息，若没查到 返回null，
	 */
	Integer  updateInfo(User user);
    
	/**
	 * 上传用户头像
	 * @param uid
	 * @param avatar
	 * @param modifiedUser
	 * @param modifiedTime
	 * @return 受影响的行数
	 */
	Integer updateAvatar(@Param("uid") Integer uid,
			@Param("avatar") String avatar,
			@Param("modifiedUser") String modifiedUser,
			@Param("modifiedTime")  Date modifiedTime);
}
