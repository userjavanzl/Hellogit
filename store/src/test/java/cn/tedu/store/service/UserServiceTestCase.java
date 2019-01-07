package cn.tedu.store.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTestCase {

	@Autowired
	private IUserService userService;

	@Test
	public void reg() {

		try {
			User user = new User();
			user.setUsername("springmvc3");
			user.setPassword("123");
			user.setGender(1);
			user.setPhone("535665488");
			user.setEmail("java@tedu.cn");
			user.setSalt("hello,MD5");

			User data = userService.reg(user);
			System.out.println(data);
		} catch (ServiceException e) {
			System.out.println(e.getClass());
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void login() {
		try {
			User data = userService.login("springmvc2", "12834");
			System.out.println(data);
		} catch (ServiceException e) {
			System.out.println("错误类型:" + e.getClass());
			System.out.println("错误描述:" + e.getMessage());
		}
	}

	@Test
	public void changePassword() {

		try {

			userService.changePassword(6, "12834", "123");// 123

		} catch (ServiceException e) {
			System.out.println("错误类型:" + e.getClass());
			System.out.println("错误描述:" + e.getMessage());
		}
	}

	@Test
	public void changeInfo() {

		try {
			User user = new User();
			user.setId(6);
			user.setGender(0);
			user.setPhone("111111");
			user.setEmail("124@qq.com");
			userService.changeInfo(user);
		} catch (ServiceException e) {
			System.out.println("错误类型:" + e.getClass());
			System.out.println("错误描述:" + e.getMessage());
		}
	}
	
	@Test 
	public void changeAvatar(){
		
		try {
			Integer uid=3;
			String avatar="upload/12455425475.gif";
			userService.changeAvatar(uid, avatar);
			
		} catch (ServiceException e) {
			
			System.out.println("错误类型:" + e.getClass());
			System.out.println("错误描述:" + e.getMessage());
			
		}
	}

}
