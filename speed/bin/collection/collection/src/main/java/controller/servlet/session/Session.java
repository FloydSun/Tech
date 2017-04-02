package com.bank.debt;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.speed.frame.common.AjaxRedirect;
import com.speed.frame.common.ControllerTools;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/session")
public class Session {
	@RequestMapping(value = "/timeout")
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		String redirPage = request.getParameter("redirect");  
        if(ControllerTools.isAjaxRequest(request)){
        	AjaxRedirect ar = new AjaxRedirect(redirPage);
        	PrintWriter pw = response.getWriter();
			pw.print(JSONObject.fromObject(ar));
			pw.close();
        }else{  
        	response.sendRedirect(redirPage);  
        }
    }  
}
