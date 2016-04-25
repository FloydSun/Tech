package com.project.speed.handler.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;
import com.project.speed.util.SQLScriptUtil;
import com.project.speed.util.SQLScriptUtil.TableField;


//uiframe <component Name> [method path file]
public class UIFrameHandler extends Handler {

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.UIFRAME.equals(req.getType())){
			if (req.getArgs().size() >= 1){
				if (!NamingRule.validatePackage(req.getArgs().get(0))){
					System.out.println("class name 非法");
					bRet = false;
				}
				
				if (req.getArgs().size() >= 2){ 
					File file = new File(req.getArgs().get(1));
					if (!file.exists()) {
						System.out.println("路径  非法");
					}
				}
				
			}else{
				System.out.println("参数过少");
				bRet = false;
			}
		}else{
			bRet = false;
		}
		return bRet;
	}
	
	@Override
	public boolean onHandle(Request req) {
		if (validateRequest(req)){		
			String componentName = req.getArgs().get(0);
			String path = NamingRule.getUIFramePath(componentName);
			String lastName = NamingRule.lastName(componentName);
			try {
				File file = new File(path + ".ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframe.ts", path + ".ts");
					String text = FileUtil.readText(path + ".ts", "utf-8");
					text = text.replace("template", lastName);
					FileUtil.setText(path + ".ts", text, "utf-8");
				}
				file = new File(path + "def.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframedef.ts", path + "def.ts");
					String text = FileUtil.readText(path + "def.ts", "utf-8");
					text = text.replace("template", lastName);
					FileUtil.setText(path + "def.ts", text, "utf-8");
				}
				file = new File(path + "Entry.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframeEntry.ts", path + "Entry.ts");
					String text = FileUtil.readText(path + "Entry.ts", "utf-8");
					text = text.replace("template", lastName);
					FileUtil.setText(path + "Entry.ts", text, "utf-8");
				}
				
				file = new File(path + ".jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframe.jsp", path + ".jsp");
					String text = FileUtil.readText(path + ".jsp", "utf-8");
					text = text.replace("template", lastName);
					FileUtil.setText(path + ".jsp", text, "utf-8");
				}
				
				file = new File(path + "Entry.jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframeEntry.jsp", path + "Entry.jsp");
					String text = FileUtil.readText(path + "Entry.jsp", "utf-8");
					text = text.replace("template", lastName);
					FileUtil.setText(path + "Entry.jsp", text, "utf-8");
				}
				
				List<String> args = new ArrayList<String>();
				String servletName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
				args.add(componentName.replace(lastName, servletName));
				args.add(lastName.toLowerCase());
				RequestServer.post(new Request(Request.SERVLET, args));
				if (req.getArgs().size() > 1){
					args = new ArrayList<String>();
					path = NamingRule.getServletPath(servletName) + ".java";
					String method = FileUtil.readText(req.getArgs().get(1), "utf-8");
					args.add(path);
					args.add(method.replace("#template#", lastName));
					RequestServer.post(new Request(Request.ADDMETHOD, args));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
