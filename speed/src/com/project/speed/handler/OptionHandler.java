package com.project.speed.handler;

import com.project.speed.request.Request;
import com.project.speed.rule.OptionRule;



public class OptionHandler extends Handler {

	@Override
	public boolean onHandle(Request req) {
		if (Request.LS.equals(req.getType())){//cd <path>
			System.out.println("base path : " + OptionRule.getBasePath());
			return true;
		}else if (Request.CD.equals(req.getType())){//cd <path>
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
