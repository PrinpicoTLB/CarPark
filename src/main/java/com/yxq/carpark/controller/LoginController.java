package com.yxq.carpark.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.yxq.carpark.entity.ParkSpace;
import com.yxq.carpark.entity.User;
import com.yxq.carpark.service.ParkspaceService;
import com.yxq.carpark.service.UserService;
import com.yxq.carpark.utils.Msg;


@Controller
@Slf4j
public class LoginController {
	@Autowired
	private UserService userService;

	@RequestMapping("/login/login")
	public String login(){
		return "login";
	}
	@RequestMapping("/login")
	public String login1(){
		return "login";
	}
	@RequestMapping("/register")
	public String register(){
		return "register";
	}

	@ResponseBody
	@RequestMapping("/login/index")
	public Msg loginIndex(User user,HttpSession httpSession){
		User user1=userService.findUserByUsername(user.getUsername());
		log.debug("user get: " + user1.toString());
		if(user1.getPassword().equals(user.getPassword()))
		{
			httpSession.setAttribute("user", user1);
			return Msg.success();

		}else{
			return Msg.fail().add("va_msg", "密码错误");
		}
	}

	@ResponseBody
	@RequestMapping("/login/checkUsernameExit")
	public Msg checkUsernameExit(@RequestParam("username")String username){
		System.out.println("username:"+username);
		User user=userService.findUserByUsername(username);
		if(user==null)
		{
			return Msg.fail().add("va_msg", "用户不存在");
		}
		return Msg.success();
	}
}
