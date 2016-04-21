package com.project.speed.handler.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;
import com.project.speed.util.SQLScriptUtil;
import com.project.speed.util.SQLScriptUtil.TableField;


//uiplugin <component Name> <uiframe component Name>
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
			
			try {
				File file = new File(path + ".ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/plugin.ts", path + ".ts");
					String text = FileUtil.readText(path + ".ts", "utf-8");
					text = text.replace("template", NamingRule.lastName(pluginName));
					text = text.replace("#FRAME#", NamingRule.lastName(frameName));
					FileUtil.setText(path + ".ts", text, "utf-8");
				}
				
				file = new File(path + "Entry.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/pluginEntry.ts", path + "Entry.ts");
					String text = FileUtil.readText(path + "Entry.ts", "utf-8");
					text = text.replace("template", NamingRule.lastName(pluginName));
					text = text.replace("#FRAME#", NamingRule.lastName(frameName));
					FileUtil.setText(path + "Entry.ts", text, "utf-8");
				}
				
				file = new File(path + ".jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/plugin.jsp", path + ".jsp");
					String text = FileUtil.readText(path + ".jsp", "utf-8");
					text = text.replace("template", NamingRule.lastName(pluginName));
					text = text.replace("#FRAME#", NamingRule.lastName(frameName));
					FileUtil.setText(path + ".jsp", text, "utf-8");
				}
				
				file = new File(path + "Entry.jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/pluginEntry.jsp", path + "Entry.jsp");
					String text = FileUtil.readText(path + "Entry.jsp", "utf-8");
					text = text.replace("template", NamingRule.lastName(pluginName));
					text = text.replace("#FRAME#", NamingRule.lastName(frameName));
					FileUtil.setText(path + "Entry.jsp", text, "utf-8");
				}
				
				String text = FileUtil.readText(framePath + "Entry.jsp", "utf-8");
				Matcher matcher = pluginPattern.matcher(text);
				if (matcher.find()){
					text = text.substring(0, matcher.end()) + 
					"\r\n\t\t\t<%@include file=\""+
					pluginName.toLowerCase().replace(".", "/") + "/" + NamingRule.lastName(pluginName) + "Entry.jsp" +
					"\"%>\r\n" +
					text.substring(matcher.end());
					FileUtil.setText(framePath + "Entry.jsp", text, "utf-8");
				}
				
				text = FileUtil.readText(framePath + ".jsp", "utf-8");
				matcher = pluginPattern.matcher(text);
				if (matcher.find()){
					text = text.substring(0, matcher.end()) + 
					"\r\n\t\t\t<%@include file=\""+
					pluginName.toLowerCase().replace(".", "/") + "/" + NamingRule.lastName(pluginName) + ".jsp" +
					"\"%>\r\n" +
					text.substring(matcher.end());
					FileUtil.setText(framePath + ".jsp", text, "utf-8");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
