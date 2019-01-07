package cn.tedu.store.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.tedu.store.controller.ex.FileEmptyExctption;
import cn.tedu.store.controller.ex.FileSizeOutOfLimitException;
import cn.tedu.store.controller.ex.FileTypeNotSupportException;
import cn.tedu.store.controller.ex.FileUploadException;
import cn.tedu.store.controller.ex.RequestException;
import cn.tedu.store.service.ex.DuplicateKeyException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.util.ResponseResult;

/**
 * 当前项目中所有控制器类的基类
 * 
 * @author UID-ECD
 *
 */
public abstract class BaseController {
	// 正确响应时的代号
	public static final Integer SUCCESS = 200;

	@ExceptionHandler({ ServiceException.class, RequestException.class })
	@ResponseBody
	public ResponseResult<Void> handlerException(Exception e) {

		if (e instanceof DuplicateKeyException) {
			// 400-违反了unique约束的异常
			return new ResponseResult<>(400, e);
		} else if (e instanceof UserNotFoundException) {
			// 401-用户数据不存在
			return new ResponseResult<>(401, e);
		} else if (e instanceof PasswordNotMatchException) {
			// 402-密码错误
			return new ResponseResult<>(402, e);

		} else if (e instanceof InsertException) {
			// 500-插入数据时发生未知错误
			return new ResponseResult<>(500, e);
		} else if (e instanceof UpdateException) {
			// 501-修改密码时发生未知错误
			return new ResponseResult<>(501, e);
		} else if (e instanceof FileEmptyExctption) {
			// 600-上传头像时发生上传文件为空异常
			return new ResponseResult<>(600, e);
		} else if (e instanceof FileSizeOutOfLimitException) {
			// 601-上传头像时发生上传文件大小限制异常
			return new ResponseResult<>(601, e);
		} else if (e instanceof FileTypeNotSupportException) {
			// 602-上传头像时发生上传文件类型限制异常
			return new ResponseResult<>(602, e);
		} else if (e instanceof FileUploadException) {
			// 610-上传头像时发生未知错误
			return new ResponseResult<>(610, e);
		}
		return null;

	}

	protected Integer getUidFormSession(HttpSession session) {

		return Integer.valueOf(session.getAttribute("uid").toString());
	}

}
