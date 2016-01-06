package com.project.speed.handler;

import com.project.speed.request.Request;
import com.project.speed.rule.OptionRule;



public class OptionHandler extends Handler {

	@Override
	public boolean onHandle(Request req) {
		
		if (Request.CD.equals(req.getType())){//cd <path>
	
			OptionRule.setBasePath(req.getArgs().get(0));
			
			return true;
		}else if(Request.PKG.equals(req.getType())){//pkg <package>
			OptionRule.setBasePackage(req.getArgs().get(0));
			return true;
		}
		return false;
	}

}
