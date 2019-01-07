package cn.tedu.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.DuplicateKeyException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;

/**
 * 
 * @author tomocus
 *
 */
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;

	// 注册
	@Override
	public User reg(User user) throws DuplicateKeyException, InsertException {
		// 1.先根据用户尝试注册的用户名查询用户信息
		User data = findByUsername(user.getUsername());
		// 2.判断查询到的数据是否为null
		if (data == null) {
			// 3.查询不到 用户信息为null,允许注册 则
			// 先执行密码加密
			// 【补充非用户提交的数据】
			user.setIsDelete(0);
			// 4项日志
			Date now = new Date();
			user.setCreateUser(user.getUsername());
			user.setCreateTime(now);
			user.setModifiedUser(user.getUsername());
			user.setModifiedTime(now);
			// ---------------------
			// 【处理密码加密】
			// 加密-1:获取随机的UUID作为盐值
			String salt = UUID.randomUUID().toString().toUpperCase();
			// 加密-2:获取用户提交的原始密码
			String srcPassword = user.getPassword();
			// 加密-3:基于原始密码和盐值执行加密，获取通过加密之后的密码。
			String md5Password = getMd5Password(srcPassword, salt);
			// 加密-4:将加密之后的密码封装到用户信息中
			user.setPassword(md5Password);
			user.setSalt(salt);
			// 然后返回注册的用户对象
			addnew(user);
			return user;
		} else {
			// 4.查询到用户信息，用户信息非null,抛出用户名被占用异常(DuplicateKeyException)
			throw new DuplicateKeyException("注册失败!您尝试使用的用户名(" + user.getUsername() + ")已经被占用!");
		}

	}

	/**
	 * 获取密码使用md5加密之后的密码的方法
	 * 
	 * @param srcPassword
	 *            未加密的密码
	 * @param salt
	 *            UUID形式的salt
	 * @return 加密之后的密码
	 */
	private String getMd5Password(String srcPassword, String salt) {
		// 将盐值拼接 原密码 再拼接 盐值
		String str = salt + srcPassword + salt;
		// 将循环执行10摘要运算
		for (int i = 0; i < 10; i++) {
			str = DigestUtils.md5DigestAsHex(str.getBytes()).toUpperCase();

		}
		// 再返回摘要结果
		return str;
	}

	// 登录
	@Override
	public User login(String username, String password) throws UserNotFoundException, PasswordNotMatchException {
		// 根据用户尝试登录的用户名查询用户信息
		User data = findByUsername(username);
		// 判断查询到的数据是否为null
		if (data == null) {
			// 是 数据为null 登录失败 抛出用户不存在异常:UserNotFoundException
			throw new UserNotFoundException("登录失败!您尝试登录的用户名(" + username + ")不存在!");
		}
		// 否 数据不为null 根据用户名找到数据，取出盐值，
		String salt = data.getSalt();
		// 对参数password执行加密
		password = getMd5Password(password, salt);
		// 判断密码是否匹配
		if (data.getPassword().equals(password)) {
			System.out.println(data.getIsDelete());
			// 匹配 密码正确,则判断用户是否被删除
			if (data.getIsDelete() == 1) {
				// 是 :已被删除 则抛出抛出用户不存在异常:UserNotFoundException
				throw new UserNotFoundException("登录失败!您尝试登录的用户名(" + username + ")已经被删除!");
			}
			// 否: 没被删除，则登录成功，将第一步查询到的用户数据中的盐值和密码设置为null
			data.setSalt(null);
			data.setPassword(null);
			// 返回第一步查询的用户数据,
			return data;

		} else {
			// 不匹配 ，登录失败 抛出密码错误异常 PasswordNotMatchException
			throw new PasswordNotMatchException("登录失败!您尝试输入的密码错误!");
		}

	}

	// 【修改密码】

	@Override
	public void changePassword(Integer uid, String oldPassword, String newPassword) {
		// 根据uid查询用户数据
		User data = findById(uid);
		// 判断查询结果是否为null
		if (data == null) {
			throw new UserNotFoundException("修改密码失败!您尝试访问的用户不存在!");
		}
		// 判断查询结果中的isDelete是否为1
		if (data.getIsDelete() == 1) {
			// 是：抛出异常：UserNotFoundException
			throw new UserNotFoundException("修改密码失败!您尝试访问的用户已经被删除!");
		}
		// 取出查询结果中的盐值
		String salt = data.getSalt();
		// 对参数oldPassword执行MD5加密
		String md5OldPassword = getMd5Password(oldPassword, salt);
		// 将加密结果与查询结果中的password对比是否匹配
		if (data.getPassword().equals(md5OldPassword)) {
			// 是：原密码正确，对参数newPassword执行MD5加密
			String md5NewPassword = getMd5Password(newPassword, salt);
			// 获取当前时间
			Date modifiedTime = new Date();
			// 更新密码
			updatePassword(uid, md5NewPassword, data.getUsername(), modifiedTime);

		} else {
			// 否：原密码错误，抛出异常：PasswordNotMatchException
			throw new PasswordNotMatchException("您输入的原密码错误!");
		}

	}

	// 【修改用户资料】
	@Override
	public void changeInfo(User user) throws UserNotFoundException, UpdateException {
		// 根据user.getId()查询用户数据
		User data = findById(user.getId());
		// 判断数据是否为null
		if (data == null) {
			// 是：抛出：UserNotFoundException
			throw new UserNotFoundException("修改用户资料失败!您尝试修改的用户不存在!");
		}
		// 判断is_delete是否为1
		if (data.getIsDelete() == 1) {
			// 是：抛出：UserNotFoundException
			throw new UserNotFoundException("修改用户资料失败!您尝试修改的用户已经被删除!");

		}
		// 向参数对象中封装：
		// - modified_user > data.getUsername()
		user.setModifiedUser(data.getUsername());
		// - modified_time > new Date()
		user.setModifiedTime(new Date());

		// 执行修改：gender,phone,email,
		// modified_user,modified_time
		updateInfo(user);

	}

	// 【上传用户头像】
	@Override
	public void changeAvatar(Integer uid, String avatar) throws UpdateException, UserNotFoundException {
		// 1.根据用户登录时绑定的参数uid查询用户信息
		User data = findById(uid);
		// 2.判断用户数据是否存在
		if (data == null) {
			// 是:用户数据为null 抛出UserNotFoundException
			throw new UserNotFoundException("修改头像失败!您尝试访问的用户不存在!");
		}
		// 否: 用户数据存在，判断用户数据是否删除
		if (data.getIsDelete() == 1) {
			// 是:抛出UserNotFoundException
			throw new UserNotFoundException("修改头像失败!您尝试访问的用户已经被删除!");

		}
		// 否:执行上传头像

		updateAvatar(uid, avatar, data.getUsername(), new Date());

	}

	/**
	 * 不希望将某些加密信息直接显示给用户
	 */
	@Override
	public User getById(Integer id) {
		User data = findById(id);
		data.setPassword(null);
		data.setSalt(null);
		data.setIsDelete(null);
		return data;
	}

	// 【注册 登录】
	/**
	 * 插入用户数据
	 * 
	 * @param 用户数据
	 * @exception InsertException
	 */

	private void addnew(User user) {

		Integer rows = userMapper.addnew(user);
		if (rows != 1) {
			throw new InsertException("注册失败!添加用户信息时出现未知错误!");
		}

	}

	/**
	 * 根据用户名查询用户数据
	 * 
	 * @param username
	 *            用户名
	 * @return 匹配的用户数据， 如果没有匹配的数据，则返回null
	 */

	private User findByUsername(String username) {

		return userMapper.findByUsername(username);
	}

	// 【修改密码】

	/**
	 * 根据登录时的session绑定的id去修改用户的密码
	 * 
	 * @param uid
	 *            用户id
	 * @param password
	 *            密码
	 * @param modifiedUser
	 *            修改人
	 * @param modifiedTime
	 *            修改时间
	 * @return 受影响的行数
	 */
	private void updatePassword(Integer uid, String password, String modifiedUser, Date modifiedTime) {

		Integer rows = userMapper.updatePassword(uid, password, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException("修改密码时发生未知错误！");
		}

	}

	/**
	 * 根据用户id查询用户密码和盐值
	 * 
	 * @param id
	 *            用户id
	 * @return 匹配的用户数据，
	 */
	private User findById(Integer id) {

		return userMapper.findById(id);
	}

	// 【修改用户资料】
	/**
	 * 修改用户资料
	 * 
	 * @param user
	 * @return 返回匹配的用户信息，若没查到 返回null，
	 */
	private void updateInfo(User user) {

		Integer rows = userMapper.updateInfo(user);
		if (rows != 1) {
			throw new UpdateException("修改资料时发生未知错误！");
		}
	}

	/**
	 * 上传用户头像
	 * 
	 * @param uid
	 * @param avatar
	 * @param modifiedUser
	 * @param modifiedTime
	 * 
	 */
	private void updateAvatar(Integer uid, String avatar, String modifiedUser, Date modifiedTime) {

		Integer rows = userMapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException("修改资料时发生未知错误！");
		}
	}

}
