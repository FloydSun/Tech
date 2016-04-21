package com.project.speed.handler.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;
import com.project.speed.util.SQLScriptUtil;
import com.project.speed.util.SQLScriptUtil.TableField;


//uiframe <component Name>
public class UIFrameHandler extends Handler {

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.UIFRAME.equals(req.getType())){
			if (req.getArgs().size() >= 1){
				if (!NamingRule.validatePackage(req.getArgs().get(0))){
					System.out.println("class name 非法");
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
			String componentName = req.getArgs().get(0);
			String path = NamingRule.getUIFramePath(componentName);
			
			try {
				File file = new File(path + ".ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframe.ts", path + ".ts");
					String text = FileUtil.readText(path + ".ts", "utf-8");
					text = text.replace("template", NamingRule.lastName(componentName));
					FileUtil.setText(path + ".ts", text, "utf-8");
				}
				file = new File(path + "def.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframedef.ts", path + "def.ts");
					String text = FileUtil.readText(path + "def.ts", "utf-8");
					text = text.replace("template", NamingRule.lastName(componentName));
					FileUtil.setText(path + "def.ts", text, "utf-8");
				}
				file = new File(path + "Entry.ts");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframeEntry.ts", path + "Entry.ts");
					String text = FileUtil.readText(path + "Entry.ts", "utf-8");
					text = text.replace("template", NamingRule.lastName(componentName));
					FileUtil.setText(path + "Entry.ts", text, "utf-8");
				}
				
				file = new File(path + ".jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframe.jsp", path + ".jsp");
					String text = FileUtil.readText(path + ".jsp", "utf-8");
					text = text.replace("template", NamingRule.lastName(componentName));
					FileUtil.setText(path + ".jsp", text, "utf-8");
				}
				
				file = new File(path + "Entry.jsp");
				if (!file.exists()) {
					FileUtil.copyResFile("/res/uiframeEntry.jsp", path + "Entry.jsp");
					String text = FileUtil.readText(path + "Entry.jsp", "utf-8");
					text = text.replace("template", NamingRule.lastName(componentName));
					FileUtil.setText(path + "Entry.jsp", text, "utf-8");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
