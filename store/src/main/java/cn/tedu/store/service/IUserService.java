package cn.tedu.store.service;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.ex.DuplicateKeyException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;

/**
 * 处理业务的接口
 * 
 * @author UID-ECD
 *
 */
public interface IUserService {

	/**
	 * 用户注册
	 * 
	 * @param 用户注册信息
	 * @return 成功注册的用户信息
	 * @throws DuplicateKeyException
	 *             用户名被占用异常
	 * @throws InsertException
	 *             插入数据异常
	 */
	User reg(User user) throws DuplicateKeyException, InsertException;

	/**
	 * 用户登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return 成功登录的用户信息
	 * @throws UserNotFoundException
	 *             用户不存在的异常
	 * @throws PasswordNotMatchException
	 *             密码不匹配的异常
	 */
	User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException;

	/**
	 * 修改密码
	 * 
	 * @param uid
	 *            id
	 * @param oldPassword
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @throws UserNotFoundException
	 *             用户不存在的异常
	 * @throws PasswordNotMatchException
	 *             密码不匹配的异常
	 * @throws UpdateException
	 *             修改过程中发生未知错误
	 */
	void changePassword(Integer uid, String oldPassword, String newPassword)
			throws UserNotFoundException, PasswordNotMatchException, UpdateException;

	/**
	 * 修改用户资料
	 * 
	 * @param user
	 *            用户资料
	 * @throws UserNotFoundException
	 * @throws UpdateException
	 */
	void changeInfo(User user) throws UserNotFoundException, UpdateException;

	/**
	 * 根据id查询用户资料
	 * 
	 * @param id
	 *            用户id
	 * @return 用户资料
	 */
	User getById(Integer id);
    
	/**
	 * 根据用户的id上传用户头像
	 * @param uid
	 * @param avater
	 * @throws UserNotFoundException
	 * @throws UpdateException
	 */
	void changeAvatar(Integer uid,String avatar) throws UserNotFoundException,UpdateException;

}
