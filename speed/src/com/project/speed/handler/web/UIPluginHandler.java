package com.project.speed.handler.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;
import com.project.speed.util.FileUtil;


//uiplugin <component Name> <uiframe component Name> [plugin file path]
public class UIPluginHandler extends Handler {
	
	static Pattern pluginPattern = Pattern.compile("id\\s*=\\s*\"plugin\"\\s*>");
	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.UIPLUGIN.equals(req.getType())){
			if (req.getArgs().size() >= 2){
				if (!NamingRule.validatePackage(req.getArgs().get(0))){
					System.out.println("component name 非法");
					bRet = false;
				}

				if (!NamingRule.validatePackage(req.getArgs().get(1))){
					System.out.println("uiframe component name 非法");
					bRet = false;
				}
				if (req.getArgs().size() >= 3){ 
					File file = new File(req.getArgs().get(2));
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
			String pluginName = req.getArgs().get(0);
			String frameName = req.getArgs().get(1);
			String framePath = NamingRule.getUIFramePath(frameName);
			String path = NamingRule.getUIPluginPath(frameName, pluginName);
			String lastPluginName = NamingRule.lastName(pluginName);
			String lastFrameName = NamingRule.lastName(frameName);
			try {
				File file = new File(path + ".ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/plugin.ts", path + ".ts");
					String text = FileUtil.readText(path + ".ts", "utf-8");
					text = text.replace("template", lastPluginName);
					text = text.replace("#FRAME#", lastFrameName);
					FileUtil.setText(path + ".ts", text, "utf-8");
				}
				
				file = new File(path + "Entry.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/pluginEntry.ts", path + "Entry.ts");
					String text = FileUtil.readText(path + "Entry.ts", "utf-8");
					text = text.replace("template", lastPluginName);
					text = text.replace("#FRAME#", lastFrameName);
					FileUtil.setText(path + "Entry.ts", text, "utf-8");
				}
				
				file = new File(path + ".jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/plugin.jsp", path + ".jsp");
					String text = FileUtil.readText(path + ".jsp", "utf-8");
					text = text.replace("template", lastPluginName);
					text = text.replace("#FRAME#", lastFrameName);
					FileUtil.setText(path + ".jsp", text, "utf-8");
				}
				
				file = new File(path + "Entry.jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/pluginEntry.jsp", path + "Entry.jsp");
					String text = FileUtil.readText(path + "Entry.jsp", "utf-8");
					text = text.replace("template", lastPluginName);
					text = text.replace("#FRAME#", lastFrameName);
					FileUtil.setText(path + "Entry.jsp", text, "utf-8");
				}
				
				String text = FileUtil.readText(framePath + "Entry.jsp", "utf-8");
				Matcher matcher = pluginPattern.matcher(text);
				if (matcher.find()){
					int end = text.indexOf("</td", matcher.end() - 1);
					text = text.substring(0, end) + 
					"\t<%@include file=\""+
					pluginName.toLowerCase().replace(".", "/") + "/" + NamingRule.lastName(pluginName) + "Entry.jsp" +
					"\"%>\r\n\t\t\t" +
					text.substring(end);
					FileUtil.setText(framePath + "Entry.jsp", text, "utf-8");
				}
				
				text = FileUtil.readText(framePath + ".jsp", "utf-8");
				matcher = pluginPattern.matcher(text);
				if (matcher.find()){
					int end = text.indexOf("</td", matcher.end() - 1);
					text = text.substring(0, end) + 
					"\t<%@include file=\""+
					pluginName.toLowerCase().replace(".", "/") + "/" + NamingRule.lastName(pluginName) + ".jsp" +
					"\"%>\r\n\t\t\t" +
					text.substring(end);
					FileUtil.setText(framePath + ".jsp", text, "utf-8");
				}
				
				List<String> args = new ArrayList<String>();
				String servletName = lastPluginName.substring(0, 1).toUpperCase() + lastPluginName.substring(1);
				args.add(frameName + "." + pluginName.replace(lastPluginName, servletName));
				args.add(servletName.toLowerCase());
				RequestServer.post(new Request(Request.SERVLET, args));
				args = new ArrayList<String>();
				args.add(frameName + "." + pluginName.replace(lastPluginName, servletName));
				args.add("transactionManager");
				args.add("-s");
				args.add(frameName + "." + pluginName.replace(lastPluginName, servletName));
				RequestServer.post(new Request(Request.SERVICE, args));
				
				if (req.getArgs().size() >= 3){ 
					args = new ArrayList<String>();
					path = NamingRule.getServletPath(frameName + "." + pluginName.replace(lastPluginName, servletName)) + ".java";
					String method = FileUtil.readText(req.getArgs().get(2), "utf-8");
					args.add(path);
					args.add(method
							.replace("#template#", servletName.toLowerCase())
							.replace("#template1#", servletName)
							.replace("#templateU#", servletName.toUpperCase())
							.replace("#templateF#", lastFrameName.substring(0, 1).toUpperCase() + lastFrameName.substring(1)));
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
