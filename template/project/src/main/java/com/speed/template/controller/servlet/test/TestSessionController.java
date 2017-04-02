package com.speed.template.controller.servlet.test;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value = "/test")
public class TestSessionController {


	@RequestMapping(value = "/session.do")
	public @ResponseBody byte[] testSet(
			HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(true);
		session.setAttribute("test", "speedSessionTest");
		Enumeration<String> e = session.getAttributeNames();
		String key = "";
		while (e.hasMoreElements()){
			String k1 = e.nextElement();
			key += k1;
			key += " : " + session.getAttribute(k1); 
		}
		
//		Map mp = SessionRepositoryUtil.getSessions();
		RedisOperationsSessionRepository rosp;
		return key.getBytes("utf-8");
	}
	
	@RequestMapping(value = "/sessionGet.do")
	public @ResponseBody byte[] testGet(
			HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		
		return ((String)session.getAttribute("test") + request.getLocalPort()).getBytes("utf-8");
	}
	
	@RequestMapping(value = "/login.do")
	public @ResponseBody byte[] testLogin(
			HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		
		return ((String)session.getAttribute("test") + request.getLocalPort()).getBytes("utf-8");
	}
	
	@RequestMapping(value = "/logout.do")
	public @ResponseBody byte[] testLogout(
			HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		String ret = ((String)session.getAttribute("test") + request.getLocalPort());
		session.invalidate();
		return (ret).getBytes("utf-8");
	}
}
