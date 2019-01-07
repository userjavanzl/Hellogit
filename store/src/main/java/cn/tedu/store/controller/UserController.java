package cn.tedu.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.tedu.store.controller.ex.FileEmptyExctption;
import cn.tedu.store.controller.ex.FileSizeOutOfLimitException;
import cn.tedu.store.controller.ex.FileTypeNotSupportException;
import cn.tedu.store.controller.ex.FileUploadException;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.util.ResponseResult;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	/*
	 * 上传文件夹的名称
	 */
	private static final String UPLOAD_DIR_NAME = "upload";
	private static final long FILE_MAX_SIZE = 5 * 1024 * 1024;
	private static final List<String> FILE_CONTENT_TYPES = new ArrayList<String>();
	/**
	 * 初始化允许上传的文件类型的集合
	 */
	static {
		FILE_CONTENT_TYPES.add("image/jpeg");
		FILE_CONTENT_TYPES.add("image/png");
		FILE_CONTENT_TYPES.add("image/gif");
	}

	@PostMapping("/reg.do")
	public ResponseResult<Void> hanldeReg(User user) {
		userService.reg(user);
		return new ResponseResult<>(SUCCESS);
	}

	@PostMapping("/login.do")
	public ResponseResult<User> handleLogin(@RequestParam("username") String username,
			@RequestParam("password") String password, HttpSession session) {
		// 执行登录
		User user = userService.login(username, password);
		// 将相关信息存入到session中
		session.setAttribute("uid", user.getId());
		session.setAttribute("username", user.getUsername());
		// 返回
		return new ResponseResult<User>(SUCCESS,user);
	}

	@PostMapping("/password.do")
	public ResponseResult<Void> changePassword(HttpSession session, @RequestParam("old_password") String oldPassword,
			@RequestParam("new_password") String newPassword) {
		// 获取当前用户登录的用户id
		Integer uid = getUidFormSession(session);
		// 执行修改密码
		userService.changePassword(uid, oldPassword, newPassword);
		// 返回
		return new ResponseResult<>(SUCCESS);
	}

	@RequestMapping("/info.do")
	public ResponseResult<User> getInfo(HttpSession session) {
		// 获取当前用户登录的用户id
		Integer uid = getUidFormSession(session);
		// 通过uid查询用户资料并显示到界面上
		User data = userService.getById(uid);
		// 返回
		return new ResponseResult<User>(SUCCESS, data);
	}

	@PostMapping("/change_info.do")

	public ResponseResult<Void> changeInfo(User user, HttpSession session) {
		// 获取当前用户登录的用户id
		Integer uid = getUidFormSession(session);
		// 将id封装到参数user中，因为user是用户提交的数据不包含id
		user.setId(uid);
		// 执行修改用户资料
		userService.changeInfo(user);

		// 返回
		return new ResponseResult<>(SUCCESS);
	}

	@PostMapping("/upload.do")
	public ResponseResult<String> handleUpload(@RequestParam("file") MultipartFile file, HttpSession session) {

		// TODO 检查是否存在上传文件
		if (file.isEmpty()) {
			// 抛出异常，文件不允许为空
			throw new FileEmptyExctption("上传失败!您没有选择上传文件或者选择上传的文件为空!");
		}
		// TODO 检查文件大小
		if (file.getSize() > FILE_MAX_SIZE) {
			// 抛出异常 ，文件大小超出限制
			throw new FileSizeOutOfLimitException("上传失败!您选择上传的文件大小超出了最大限制!");
		}
		// TODO 检查 文件类型
		if (!FILE_CONTENT_TYPES.contains(file.getContentType())) {
			// 抛出异常，文件类型限制
			throw new FileTypeNotSupportException("上传失败!您选择上传的文件类型不支持上传!");
		}

		// 确定上传文件夹的路径
		String parentPath = session.getServletContext().getRealPath(UPLOAD_DIR_NAME);
		File parent = new File(parentPath);
		if (!parent.exists()) {
			// 若不存在，就创建
			parent.mkdirs();
		}
		// 确定文件名

		// 获取原始文件名
		String originalFileName = file.getOriginalFilename();
		// 获取扩展名
		String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
		System.out.println("originalFileName=" + originalFileName + "------suffix=" + suffix);
		String fileName = System.currentTimeMillis() + "" + (new Random().nextInt(900000) + 100000) + suffix;
		// 保存到的目标文件，即上传的文件保存在服务器的哪个位置的路径
		File dest = new File(parent, fileName);
		// 保存文件
		try {
			file.transferTo(dest);
			System.out.println("上传成功!");
		} catch (IllegalStateException e) {
			// 抛出异常,上传失败
			throw new FileUploadException("抛出异常,上传失败");
		} catch (IOException e) {
			// 抛出异常, 上传失败
			throw new FileUploadException("抛出异常,上传失败");
		}
		// 获取当前用户登录的用户id
		Integer uid = getUidFormSession(session);

		// 更新头像数据
		System.err.println("头像路径:" + parent + "/" + fileName);
		userService.changeAvatar(uid, "/" + UPLOAD_DIR_NAME + "/" + fileName);
		// 返回
		ResponseResult<String> rr = new ResponseResult<>();
		rr.setState(SUCCESS);
		rr.setData("/"+UPLOAD_DIR_NAME + "/" + fileName);
		return rr;
	}

}
