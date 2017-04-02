package com.project.speed.handler.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.project.speed.handler.Handler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;

//template <project name> <package name>
public class ProjectHandler extends Handler {

	private static final String srcPath = "/project/src/main/java/com/speed/template/";
	private static final String javaPath = "/project/src/main/java";

	
	private boolean validateRequest(Request req){
		boolean bRet = true;
		if (Request.PROJECT.equals(req.getType())){
			if (req.getArgs().size() >= 2){
				if (!NamingRule.validatePackage(req.getArgs().get(1))){
					System.out.println("package name 非法");
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
	
	private void modifySourceFile(String outPath, String pkgName, String projName) throws IOException{
		String pkgPath = pkgName.replace(".", "/");
		List<String> fileList = new ArrayList<String>();
		fileList.add("controller/servlet/session/Session.java");
		fileList.add("controller/servlet/test/TestSessionController.java");
		fileList.add("controller/webservice/test/TestWS.java");
		fileList.add("service/security/AuthenticationProviderImpl.java");
		fileList.add("service/security/GrantedAuthorityImpl.java");
		fileList.add("service/security/UserDetailsServiceImpl.java");
		for(String src : fileList){
			String text = FileUtil.readText(outPath + srcPath + src, "utf-8");		
			text = CodeUtil.setPackageName(text, pkgName + "." + FileUtil.getDir(src).replace("/", "."));
			FileUtil.setText(outPath + srcPath + src, text, "utf-8");
			FileUtil.cut(outPath + srcPath + src, outPath + javaPath + "/" + src);
		}
		
		FileUtil.deleteDir(new File(outPath + javaPath + "/com"));
		
		for(String src : fileList){
			FileUtil.cut(outPath + javaPath + "/" + src, 
					outPath + javaPath + "/" + pkgPath + "/" + src);
		}
	}
	
	@Override
	public boolean onHandle(Request req) {
		if (validateRequest(req)){
			String projName = req.getArgs().get(0);
			String pkgName = req.getArgs().get(1);
			String zipPath = OptionRule.getBasePath() + "/tmp.zip";
			String outPath = OptionRule.getBasePath() + "/" + projName;
			try {
				FileUtil.copyResFile("/res/template.zip", zipPath);
				FileUtil.unZip(zipPath, outPath);
				modifySourceFile(outPath, pkgName, projName);	
				modifyConfigFile(outPath, pkgName, projName);
				FileUtil.deleteFile(zipPath);
			} catch (IOException e) {
				e.printStackTrace();
			}		
			
			try {
				FileUtil.cutFolder(outPath + "/project/", outPath + "/" + projName + "/");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			RequestServer.post(new Request(Request.SWD, new String[]{outPath + javaPath.replace("project", projName)}));
			RequestServer.post(new Request(Request.PKG, new String[]{req.getArgs().get(1)}));
			RequestServer.post(new Request(Request.PWD, new String[]{}));
			return true;
		}
		return false;
	}

	private void modifyConfigFile(String outPath, String pkgName,
			String projName) throws IOException {
		List<String> fileList = new ArrayList<String>();
		fileList.add("project/.settings/org.eclipse.wst.common.component");
		fileList.add("project/.project");
		fileList.add("project/pom.xml");
		fileList.add("project/src/main/webapp/WEB-INF/applicationContext-services.xml");
		fileList.add("project/src/main/webapp/WEB-INF/applicationContext-servlet.xml");
		fileList.add("project/src/main/webapp/WEB-INF/cxf-webservices.xml");
		fileList.add("project/src/main/webapp/WEB-INF/spring-security.xml");
		fileList.add("project/src/main/webapp/WEB-INF/spring-session.xml");
		fileList.add("project/src/main/webapp/WEB-INF/web.xml");
		fileList.add("project/src/main/webapp/WEB-INF/log4j.xml");
		for(String src : fileList){
			String text = FileUtil.readText(outPath + "/" + src, "utf-8");		
			text = text.replace("com.speed.template", pkgName).replace("speedTemplate", projName);
			FileUtil.setText(outPath + "/" + src, text, "utf-8");
		}
		
		fileList.clear();
		fileList.add("frame/.settings/org.eclipse.wst.common.component");
		fileList.add("frame/.project");
		fileList.add("frame/pom.xml");
		fileList.add("project/pom.xml");
		for(String src : fileList){
			String text = FileUtil.readText(outPath + "/" + src, "utf-8");		
			text = text.replace("frame.template", "com.speed.frame").replace("frameTemplate", "frame");
			FileUtil.setText(outPath + "/" + src, text, "utf-8");
		}
	}
}
