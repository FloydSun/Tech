package com.project.speed.handler.server;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;


//frame <component Name> <transaction> [<entityName> <tableName> <db name>]
public class FrameHandler extends Handler {

	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.FRAME.equals(req.getType())){
			if (req.getArgs().size() >= 2){
				
				if (!NamingRule.validateClass(req.getArgs().get(0))){
					System.out.println("component Name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(1))){
					System.out.println("transaction 非法");
					bRet = false;
				}
				
				if (req.getArgs().size() > 2 ){
					if (req.getArgs().size() >= 5){
						if (!NamingRule.validateClass(req.getArgs().get(2))){
							System.out.println("entityName 非法");
							bRet = false;
						}
						
						if (!NamingRule.validateClass(req.getArgs().get(3))){
							System.out.println("tableName 非法");
							bRet = false;
						}
						
						if (!NamingRule.validateClass(req.getArgs().get(4))){
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
			
			RequestServer.post(new Request(Request.SERVLET, new String[]{req.getArgs().get(0),
					req.getArgs().get(0)
			}));
			
			RequestServer.post(new Request(Request.SERVICE, new String[]{req.getArgs().get(0),
					req.getArgs().get(1),
					"-s",
					req.getArgs().get(0)
			}));
			
			if (req.getArgs().size() > 2){
				RequestServer.post(new Request(Request.DAO, new String[]{req.getArgs().get(2),
						req.getArgs().get(4),
						req.getArgs().get(1),
						req.getArgs().get(0)
				}));
				
				RequestServer.post(new Request(Request.ENTITY, new String[]{
						"-f",
						req.getArgs().get(2),
						req.getArgs().get(3)
				}));
			}
			
			return true;
		}
		return false;
	}

}
