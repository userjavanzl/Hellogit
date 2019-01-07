package cn.tedu.store.mapper;



import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTestCase {
	
	@Autowired
	private UserMapper  userMapper;
	
	@Test
	public void addnew(){
		User user = new User();
		user.setUsername("java");
		user.setPassword("1234");
		user.setGender(1);
		user.setPhone("535665488");
		user.setEmail("java@tedu.cn");
		user.setSalt("hello,MD5");
		user.setCreateUser("zhangsan");
		user.setIsDelete(0);
		Date now= new Date();
		user.setCreateTime(now);
		user.setModifiedUser("lisi");
		user.setModifiedTime(new Date());
		
		Integer rows=userMapper.addnew(user);
		System.out.println(rows);
		
	}
	
	@Test
	public void findByUsername(){
		
		User data=userMapper.findByUsername("java");
		System.out.println(data);
	}
	
	
	@Test
	public void findById(){
		
		User data=userMapper.findById(2);
		System.out.println(data);
	}
	
	@Test
	public void updatePassword(){
		
		Integer rows= userMapper.updatePassword(2, "123","lisi", new Date());
		 System.out.println(rows);
	    	
	}
	
	@Test
	public void  updateInfo(){
		
		User data=new User();
		data.setId(1);
		data.setGender(0);
		data.setPhone("1111111");
		data.setEmail("qq@.com");
		data.setModifiedUser("springmvc");
		data.setModifiedTime(new Date());
		Integer rows = userMapper.updateInfo(data);
		System.out.println("rows="+rows);
	}
	
	@Test
	public void  updateAvatar(){
		
		Integer uid =1;
		String avatar ="sdj";
		String modifiedUser="springmvc";
		Date modifiedTime=new Date();
		Integer rows = userMapper.updateAvatar(uid, avatar, modifiedUser, modifiedTime);
		System.out.println(rows);
	}

}
