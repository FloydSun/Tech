package com.demo.activiti.controller.servlet.activiti;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.activiti.service.account.TestService;

@Controller
@RequestMapping(value = "activiti")
public class ActivitiServlet {
	
	@Autowired
	TestService accountService;
	
	@RequestMapping(value = "show.do")
	public @ResponseBody byte[] getShow(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		accountService.resetpassword("adsfdsf", "fasdfasd", "fdfweeasdf");
		
		return "OK".getBytes("utf-8");
	}
	

}