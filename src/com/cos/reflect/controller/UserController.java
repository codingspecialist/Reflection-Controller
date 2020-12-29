package com.cos.reflect.controller;

import com.cos.reflect.anno.Controller;
import com.cos.reflect.anno.RequestMapping;
import com.cos.reflect.controller.dto.JoinDto;
import com.cos.reflect.controller.dto.LoginDto;

//@Controller
public class UserController {
	
	@RequestMapping(uri = "/user/join")
	public String join(JoinDto dto) {
		System.out.println("join 함수 요청됨");
		System.out.println(dto);
		return "/";
	}
	
	@RequestMapping(uri = "/user/login")
	public String login(LoginDto dto) {
		System.out.println("login 함수 요청됨");
		System.out.println(dto);
		return "/";
	}
	
	@RequestMapping(uri = "/user")
	public String user() {
		System.out.println("user 함수 요청됨");
		return "/";
	}
}
