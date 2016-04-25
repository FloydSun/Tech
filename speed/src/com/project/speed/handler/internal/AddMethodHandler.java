package com.project.speed.handler.internal;

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


//addMethod classFilePath method body
public class AddMethodHandler extends Handler {

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.ADDMETHOD.equals(req.getType())){
			if (req.getArgs().size() == 2){

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
			
			try {
				String path = req.getArgs().get(0);
				String text = FileUtil.readText(path, "utf-8");
				int index = text.lastIndexOf("}");
				if (index > 0){
					text = text.substring(0, index) + req.getArgs().get(1) + "\r\n}";
				}
				FileUtil.setText(path, text, "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

}
