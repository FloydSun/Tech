package com.project.speed.handler;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import com.project.speed.request.Request;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;


//dao <component Name> <db name> <transaction> [<service name>]
public class DaoHandler extends Handler {

	static Pattern classPattern = Pattern.compile("(Dao)$");   
	
	@Override
	public boolean onHandle(Request req) {
		if (Request.DAO.equals(req.getType())){
		
			String totalClassName = NamingRule.getDaoName(req.getArgs().get(0));
			String interfacePath = NamingRule.getDaoPath(req.getArgs().get(0)) + ".java";
			String classPath = NamingRule.getDaoPath(req.getArgs().get(0)) + "Impl.java";

			try {
				File interfacefile = new File(interfacePath);
				if (!interfacefile.exists()) {
					FileUtil.copyResFile("/res/dao.java", interfacePath);
				}
				
				File classfile = new File(classPath);
				if (!classfile.exists()) {
					FileUtil.copyResFile("/res/daoImpl.java", classPath);
				}
				
				
				String className = NamingRule.getClassName(totalClassName);
				String pkgName = NamingRule.getPackageName(totalClassName);
				
				String text = FileUtil.readText(interfacePath, "utf-8");
				text = CodeUtil.setInterfaceName(text, className);
				text = CodeUtil.setPackageName(text, pkgName);
				FileUtil.setText(interfacePath, text, "utf-8");
				
				text = FileUtil.readText(classPath, "utf-8");
				text = CodeUtil.addImport(text, totalClassName);
				text = CodeUtil.setPackageName(text, pkgName);
				text = CodeUtil.setImplementsName(text, className);
				text = CodeUtil.setClassName(text, className + "Impl");
				text = CodeUtil.setTransactionName(text, req.getArgs().get(2));
				text = CodeUtil.setDBName(text, req.getArgs().get(1));
				FileUtil.setText(classPath, text, "utf-8");
				
				
				if (req.getArgs().size() > 3){
					String servicePath = NamingRule.getServicePath(req.getArgs().get(3)).replace(".", "/") + "Impl.java";
					text = FileUtil.readText(servicePath, "utf-8");
					text = CodeUtil.addImport(text, totalClassName);
					text = CodeUtil.addMember(text, "\t@Autowired\r\n\t" + className + " " + className.substring(0, 1).toLowerCase() + className.substring(1) + ";");
					FileUtil.setText(servicePath, text, "utf-8");
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
