package com.yxq.carpark.serviceImpl;

import java.util.List;

import com.yxq.carpark.vo.UserRegisterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yxq.carpark.dao.UserDao;
import com.yxq.carpark.entity.User;
import com.yxq.carpark.service.UserService;
import com.yxq.carpark.utils.Msg;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	public User findUserByUsername(String username) {
		User user=userDao.findUserByUserName(username);
		return user;
	}

	public void saveByaddDepotCard(String username,String name, int cardid) {
		userDao.updateByaddDepotCard(username,cardid);
	}

	public User findUserByCardid(int cardid) {
		return userDao.findUserByCardid(cardid);
	}

	public List<User> findAllUser(int page,int size) {
		return userDao.findAllUser(page,size);
	}

	public void deleteUserByCardid(int cardid) {
		userDao.deleteUserByCardid(cardid);
	}

	public void save(User user) {
		userDao.save(user);
	}

	public List<User> findUsersByRole(int role,int page,int size) {
		return userDao.findUsersByRole(role,page,size);
	}

	public List<User> findUsersByRoleMan(int role,int page,int size){return userDao.findUsersByRoleMan(role,page,size);};

	public User findUserById(int uid) {
		return userDao.findUserById(uid);
	}

	public void update(User user) {
		userDao.update(user);
	}

	public void delUserById(int uid) {
		userDao.delUserById(uid);
	}

	public int findAllUserCount(int role) {
		return userDao.findAllUserCount(role);
	}

	public int findAllUserCountMan(int role){return userDao.findAllUserCountMan(role);};

	public List<User> finAllUserByRole(int role) {
		return userDao.finAllUserByRole(role);
	}

	@Override
	public User findByphone(String tel) {
		User user = userDao.findUserByTel(tel);
		return user;
	}

	@Override
	public int register(UserRegisterVO userRegisterVO) {
		User user = new User();
		BeanUtils.copyProperties(userRegisterVO,user);
		user.setRole(3);
		int i = userDao.registerUser(user);
		System.out.println("注册结果: " + i);
		return i;
	}

}
