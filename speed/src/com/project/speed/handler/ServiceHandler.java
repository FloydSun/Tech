package com.project.speed.handler;

import java.io.File;
import java.io.IOException;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;


//service <class name> <transaction> [<servlet name>]
public class ServiceHandler extends Handler {

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.SERVICE.equals(req.getType())){
			if (req.getArgs().size() >= 2){
				if (!NamingRule.validatePackage(req.getArgs().get(0))){
					System.out.println("class name 非法");
					bRet = false;
				}
				
				if (!NamingRule.validateClass(req.getArgs().get(1))){
					System.out.println("transaction 非法");
					bRet = false;
				}
				
				if (req.getArgs().size() > 2 && !NamingRule.validatePackage(req.getArgs().get(2))){
					System.out.println("servlet name 非法");
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
			String interfacePath = OptionRule.getBasePath() + "/" + req.getArgs().get(0).replace(".", "/") + ".java";
			String classPath = OptionRule.getBasePath() + "/" + req.getArgs().get(0).replace(".", "/") + "Impl.java";

			try {
				File interfacefile = new File(interfacePath);
				if (!interfacefile.exists()) {
					FileUtil.copyResFile("/res/service.java", interfacePath);
				}
				
				File classfile = new File(classPath);
				if (!classfile.exists()) {
					FileUtil.copyResFile("/res/serviceImpl.java", classPath);
				}
				
				
				String className = NamingRule.getClassName(req.getArgs().get(0));
				String pkgName = NamingRule.getPackageName(req.getArgs().get(0));
				
				String text = FileUtil.readText(interfacePath, "utf-8");
				text = CodeUtil.setInterfaceName(text, className);
				text = CodeUtil.setPackageName(text, pkgName);
				FileUtil.setText(interfacePath, text, "utf-8");
				
				text = FileUtil.readText(classPath, "utf-8");
				text = CodeUtil.addImport(text, req.getArgs().get(0));
				text = CodeUtil.setPackageName(text, pkgName);
				text = CodeUtil.setImplementsName(text, className);
				text = CodeUtil.setTransactionName(text, req.getArgs().get(1));
				text = CodeUtil.setClassName(text, className + "Impl");
				FileUtil.setText(classPath, text, "utf-8");
				
				
				if (req.getArgs().size() > 2){
					String servletPath = OptionRule.getBasePath() + "/" + req.getArgs().get(2).replace(".", "/") + ".java";
					text = FileUtil.readText(servletPath, "utf-8");
					text = CodeUtil.addImport(text, req.getArgs().get(0));
					text = CodeUtil.addMember(text, "\t@Autowired\r\n\t" + className + " " + className.substring(0, 1).toLowerCase() + className.substring(1) + ";");
					FileUtil.setText(servletPath, text, "utf-8");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true;
		}
		return false;
	}

}
