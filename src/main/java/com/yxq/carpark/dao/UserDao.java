package com.yxq.carpark.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yxq.carpark.entity.User;



@Mapper
public interface UserDao extends BaseDao<User>{

	public void save(User user);
	public User findUserById(int id);
	public User findUserByUserName(String  username);
	public void updateByaddDepotCard(@Param("username")String username, @Param("cardid")int cardid);
	public User findUserByCardid(@Param("cardid")int cardid);
	public List<User> findAllUser(@Param("page")int page,@Param("size")int size);
	public void deleteUserByCardid(@Param("cardid")int cardid);
	public List<User> findUsersByRole(@Param("role")int role,@Param("page")int page,@Param("size")int size);
	public List<User> findUsersByRoleMan(@Param("role")int role,@Param("page")int page,@Param("size")int size);
	public void update(User user);
	public void delUserById(int uid);
	public int findAllUserCount(@Param("role")int role);
	public int findAllUserCountMan(@Param("role")int role);
	public List<User> finAllUserByRole(int role);

	User findUserByTel(@Param("tel") String tel);

	int registerUser(User user);
}
