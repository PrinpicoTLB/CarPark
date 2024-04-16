package com.yxq.carpark.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yxq.carpark.entity.User;



@Mapper
public interface UserDao extends BaseDao<User>{

	//����û�
	public void save(User user);
	//ͨ��id���û�
	public User findUserById(int id);
	//ͨ���û������û�
	public User findUserByUserName(String  username);
	//ͨ��username��cardid����û���ֱ�����ͣ����ʱ��
	public void updateByaddDepotCard(@Param("username")String username, @Param("cardid")int cardid);
	//ͨ��ͣ����id��ѯ�û�
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
