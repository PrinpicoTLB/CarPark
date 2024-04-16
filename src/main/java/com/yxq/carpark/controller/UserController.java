package com.yxq.carpark.controller;

import javax.servlet.http.HttpSession;

import com.yxq.carpark.vo.UserRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yxq.carpark.entity.Depotcard;
import com.yxq.carpark.entity.ParkInfo;
import com.yxq.carpark.entity.User;
import com.yxq.carpark.service.DepotcardService;
import com.yxq.carpark.service.ParkinfoService;
import com.yxq.carpark.service.UserService;
import com.yxq.carpark.utils.Msg;


@Controller
@Slf4j
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private DepotcardService depotcardService;
	@Autowired
	private ParkinfoService parkinfoService;

	@RequestMapping("/index/user/phone")
	@ResponseBody
	public Msg verifyPhone(@RequestParam String tel){
		User user = userService.findByphone(tel);
		if(user==null)
		{
			return Msg.fail().add("va_msg", "�û���������");
		}
		return Msg.success();
	}

	@RequestMapping("/index/user/register")
	@ResponseBody
	public Msg register(@RequestBody UserRegisterVO userRegisterVO) {
		System.out.println("��ʼע��: " + userRegisterVO.toString());
		return userService.register(userRegisterVO) > 0 ? Msg.success() : Msg.fail();
	}


	//ajaxУ��username�Ƿ����
		@ResponseBody
		@RequestMapping("/index/user/checkUsername")
		public Msg checkUsername(@RequestParam("username")String username){
			System.out.println("username:"+username);
			User user=userService.findUserByUsername(username);
			if(user==null)
			{
				return Msg.fail().add("va_msg", "�û���������");
			}
			return Msg.success();
		}
		
		//���user
		@ResponseBody
		@RequestMapping("/index/user/addUser")
		public Msg addUser(User user){
			user.setSex("��");
			user.setName(user.getUsername());
			userService.save(user);
			user=userService.findUserByUsername(user.getUsername());
			if(user==null)
			{
				return Msg.fail().add("va_msg", "���ʧ�ܣ�");
			}
				return Msg.success().add("va_msg", "��ӳɹ���");
		}
		
		//����user
		@ResponseBody
		@RequestMapping("/index/user/findUserById")
		public Msg findUserById(@RequestParam("uid")Integer uid,HttpSession httpSession)
		{
			User user=userService.findUserById(uid.intValue());
			if(user==null)
			{
				return Msg.fail().add("va_msg", "����ʧ�ܣ�");
			}else
			{
				User currentUser=(User) httpSession.getAttribute("user");
				return Msg.success().add("user",user).add("role", currentUser.getRole());
			}
			
		}
		
		//�༭user
		@ResponseBody
		@RequestMapping("/index/user/editUser")
		public Msg editUser(User user){
			int uid=user.getId();
			User temUser=userService.findUserById(uid);
			if(user.getRole()==0)
			{
				user.setRole(temUser.getRole());
			}
			user.setPassword(temUser.getPassword());
			user.setCardid(temUser.getCardid());
			try {
						userService.update(user);
			} catch (Exception e) {
				return Msg.fail().add("va_msg", "�޸�ʧ�ܣ�");
			}
				return Msg.success().add("va_msg", "�޸ĳɹ���");
		}
		
		//ɾ��user
		@ResponseBody
		@RequestMapping("/index/user/deleteUser")
		@Transactional
		public Msg deleteUser(@RequestParam("uid") Integer uid, HttpSession session)
		{
			User user=userService.findUserById(uid);
			User user1 = (User) session.getAttribute("user");
			log.info("删除用户:" + user.toString() + ",删除人:" + user1.toString());
			if(user.getRole() == user1.getRole()) {
				return Msg.fail().add("va_msg","很抱歉，您无法删除同级用户!");
			}
			if(user!=null)
			{
				int cardid=user.getCardid();
				if(cardid!=0)
				{
					Depotcard depotcard=depotcardService.findByCardid(cardid);
					String cardnum=depotcard.getCardnum();
					ParkInfo parkInfo=parkinfoService.findParkinfoByCardnum(cardnum);
					//����ͣ������ɾ
					if(parkInfo!=null)
					{
						return Msg.fail().add("va_msg", "�г�����ͣ��������ɾ����");
					}else{
						depotcardService.deleteDepotCard(cardnum);
					}
				}
				userService.delUserById(uid.intValue());
				return Msg.success().add("va_msg", "ɾ���ɹ���");
			}else{
				return Msg.fail().add("va_msg", "ɾ��ʧ�ܣ�");
			}
		}
				
}
