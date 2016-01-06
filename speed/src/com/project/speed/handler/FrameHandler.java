package com.project.speed.handler;

import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;


//frame <package name> <component Name> <transaction> [<entityName> <tableName> <db name>]
public class FrameHandler extends Handler {

	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.FRAME.equals(req.getType())){
			if (req.getArgs().size() >= 3){
				if (!NamingRule.validatePackage(req.getArgs().get(0))){
					System.out.println("package name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(1))){
					System.out.println("component Name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(2))){
					System.out.println("transaction 非法");
					bRet = false;
				}
				
				if (req.getArgs().size() > 3 ){
					if (req.getArgs().size() >= 6){
						if (!NamingRule.validateClass(req.getArgs().get(3))){
							System.out.println("entityName 非法");
							bRet = false;
						}
						
						if (!NamingRule.validateClass(req.getArgs().get(4))){
							System.out.println("tableName 非法");
							bRet = false;
						}
						
						if (!NamingRule.validateClass(req.getArgs().get(5))){
							System.out.println("db name 非法");
							bRet = false;
						}
					}else{
						System.out.println("可选参数过少");
						bRet = false;
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
	
			RequestServer.post(new Request(Request.SERVLET, new String[]{
					NamingRule.getServletName(req.getArgs().get(0), req.getArgs().get(1)),
					req.getArgs().get(1)
			}));
			
			RequestServer.post(new Request(Request.SERVICE, new String[]{
					NamingRule.getServiceName(req.getArgs().get(0), req.getArgs().get(1)),
					req.getArgs().get(2)
			}));
			
			if (req.getArgs().size() > 3){
				RequestServer.post(new Request(Request.DAO, new String[]{
						NamingRule.getDaoName(req.getArgs().get(0), req.getArgs().get(3)),
						req.getArgs().get(5),
						req.getArgs().get(2)
				}));
				
				RequestServer.post(new Request(Request.ENTITY, new String[]{
						"-i",
						NamingRule.getEntityName(req.getArgs().get(0), req.getArgs().get(3)),
						req.getArgs().get(4)
				}));
			}
			
			return true;
		}
		return false;
	}

}
