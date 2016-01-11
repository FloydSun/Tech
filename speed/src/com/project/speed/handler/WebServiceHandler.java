package com.project.speed.handler;

import java.io.File;
import java.io.IOException;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;


//webservice <component Name> <request map>
public class WebServiceHandler extends Handler {

	private static final String WS_CONFIG =
		"<bean id=\"WEBSERVICE\" class=\"com.speed.template.controller.webservice.nacao.NacaoWebServiceImpl\" />\r\n\t"+
			"<jaxws:server id=\"WEBSERVICESERVER\" serviceBean=\"#WEBSERVICE\" address=\"ADDRESS\">"+
		"</jaxws:server>\r\n\t"; 
	
	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.WEBSERVICE.equals(req.getType())){
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
		if (validateRequest(req)){
			
			String totalClassName = NamingRule.getWebServiceName(req.getArgs().get(0));
			String classPath = NamingRule.getWebServicePath(req.getArgs().get(0)) + ".java";

			try {
				File interfacefile = new File(classPath);
				if (!interfacefile.exists()) {
					FileUtil.copyResFile("/res/webservice.java", classPath);
				}
							
				String className = NamingRule.getClassName(totalClassName);
				String pkgName = NamingRule.getPackageName(totalClassName);
				
				String text = FileUtil.readText(classPath, "utf-8");
				text = CodeUtil.setClassName(text, className);
				text = CodeUtil.setPackageName(text, pkgName);
				FileUtil.setText(classPath, text, "utf-8");
		
				String webConfigPath = FileUtil.getDir(OptionRule.getBasePath()) + "/webapp/WEB-INF/applicationContext-webservices.xml";
				text = FileUtil.readText(webConfigPath, "utf-8");
				String newService = WS_CONFIG.replace("WEBSERVICE", req.getArgs().get(0)).replace("WEBSERVICESERVER", req.getArgs().get(0) + "Server").replace("ADDRESS", req.getArgs().get(1));
				text = text.replace("<context:component-scan", newService + "<context:component-scan");
				FileUtil.setText(webConfigPath, text, "utf-8");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}

}
