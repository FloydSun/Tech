package com.project.speed.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.NamingRule;
import com.project.speed.rule.OptionRule;
import com.project.speed.util.CodeUtil;
import com.project.speed.util.FileUtil;

//template <project name> <package name>
public class ProjectHandler extends Handler {

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
//		d:/test66/oop/src/main/java/AjaxRedirect.java
//		d:/test66/oop/src/main/java/CharacterEncodingFilter.java
//		d:/test66/oop/src/main/java/OnlineService.java
//		d:/test66/oop/src/main/java/RequestValidator.java
		pkgName += ".common";
		String pkgPath = pkgName.replace(".", "/");
		List<String> fileList = new ArrayList<String>();
		fileList.add("AjaxRedirect.java");
		fileList.add("CharacterEncodingFilter.java");
		fileList.add("OnlineService.java");
		fileList.add("RequestValidator.java");
		for(String src : fileList){
			String text = FileUtil.readText(outPath + "/project/src/main/java/" + src, "utf-8");		
			text = CodeUtil.setPackageName(text, pkgName);
			FileUtil.setText(outPath + "/project/src/main/java/" + src, text, "utf-8");
			FileUtil.cut(outPath + "/project/src/main/java/" + src, 
					outPath + "/project/src/main/java/" + pkgPath + "/" + src);
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
			RequestServer.post(new Request(Request.CD, new String[]{outPath + "/project/src/main/java"}));
			RequestServer.post(new Request(Request.PKG, new String[]{req.getArgs().get(1)}));
			RequestServer.post(new Request(Request.LS, new String[]{}));
			return true;
		}
		return false;
	}



	private void modifyConfigFile(String outPath, String pkgName,
			String projName) throws IOException {
		List<String> fileList = new ArrayList<String>();
		fileList.add("project/.classpath");
		fileList.add("project/src/main/resources/META-INF/persistence.xml");
		fileList.add("project/.settings/org.eclipse.wst.common.component");
		fileList.add("project/.project");
		fileList.add("project/pom.xml");
		fileList.add("project/src/main/webapp/META-INF/context.xml");
		fileList.add("project/src/main/webapp/WEB-INF/applicationContext-services.xml");
		fileList.add("project/src/main/webapp/WEB-INF/applicationContext-servlet.xml");
		fileList.add("project/src/main/webapp/WEB-INF/web.xml");
		for(String src : fileList){
			String text = FileUtil.readText(outPath + "/" + src, "utf-8");		
			text = text.replace("[packageName]", pkgName).replace("[projectName]", projName);
			FileUtil.setText(outPath + "/" + src, text, "utf-8");
		}
	}

}
