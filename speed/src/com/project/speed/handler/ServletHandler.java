package com.project.speed.handler;

import java.io.File;
import java.io.IOException;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;

//servlet <class name> <request map>
public class ServletHandler extends Handler {

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.SERVLET.equals(req.getType())){
			if (req.getArgs().size() >= 2){
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
		if (validateRequest(req)) {

			String path = OptionRule.getBasePath() + "/" + req.getArgs().get(0).replace(".", "/") + ".java";
			
			try {
				File file = new File(path);
				if (!file.exists()) {
					FileUtil.copyResFile("/res/servlet.java", path);
				}
				
				String text = FileUtil.readText(path, "utf-8");
				text = CodeUtil.setRequestName(text, req.getArgs().get(1));
				text = CodeUtil.setClassName(text, NamingRule.getClassName(req.getArgs().get(0)));
				text = CodeUtil.setPackageName(text, NamingRule.getPackageName(req.getArgs().get(0)));
				FileUtil.setText(path, text, "utf-8");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}

}
