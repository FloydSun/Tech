package com.project.speed.handler;

import java.io.File;

import com.project.speed.request.Request;
import com.project.speed.rule.OptionRule;



public class OptionHandler extends Handler {

	@Override
	public boolean onHandle(Request req) {
		if (Request.LS.equals(req.getType())){//cd <path>
			File file = new File(OptionRule.getBasePath());  
	        File[] subFile = file.listFiles();  
	  
	        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
	        	 String fileName = subFile[iFileLength].getName();  
	            if (!subFile[iFileLength].isDirectory()) {  
	                System.out.println(fileName );
	            } else{
	            	System.out.println(fileName + "/");
	            }
	        }  
			System.out.println(OptionRule.getBasePath());
			return true;
		}else if (Request.PWD.equals(req.getType())){//cd <path>
			System.out.println(OptionRule.getBasePath());
			return true;
		}else if (Request.SWD.equals(req.getType())){//cd <path>
			if (req.getArgs().isEmpty()){
				System.out.println("参数过少 ");
			}else{
				OptionRule.setBasePath(req.getArgs().get(0));
			}
			return true;
		}else if(Request.PKG.equals(req.getType())){//pkg <package>
			if (req.getArgs().isEmpty()){
				System.out.println("base package : " + OptionRule.getBasePackage());
			}else{
				if (!OptionRule.setBasePackage(req.getArgs().get(0))){
					System.out.println(req.getArgs().get(0) + " 非法");
				}
			}
			return true;
		}else if(!Request.HELP.equals(req.getType())){
			if (null == OptionRule.getBasePackage()){
				System.out.println("请通过 " + Request.PKG + " 命令设置包名称");
				return true;
			}
		}
		return false;
	}

}
