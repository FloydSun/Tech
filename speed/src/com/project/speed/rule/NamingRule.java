package com.project.speed.rule;

import java.util.regex.Pattern;

public class NamingRule {
	static Pattern namePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");   
	static Pattern pkgPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*(.[a-zA-Z][a-zA-Z0-9_]*)*$");
	
	public static String getServletPath(String componentName){
		return OptionRule.getBasePath() + "/" + getServletName(componentName).replace(".", "/");
	}
	
	public static String getServletName(String componentName){
		return OptionRule.getBasePackage() + ".controller.servlet." + componentName.toLowerCase() + "." + componentName + "Controller";
	}
	
	public static String getServicePath(String componentName){
		return OptionRule.getBasePath() + "/" + getServiceName(componentName).replace(".", "/");
	}
	
	public static String getServiceName(String componentName){
		return OptionRule.getBasePackage() + ".service." + componentName.toLowerCase() + "." + componentName + "Service";
	}
	
	public static String getDaoPath(String componentName){
		return OptionRule.getBasePath() + "/" + getDaoName(componentName).replace(".", "/");
	}
	
	public static String getDaoName(String componentName){
		return OptionRule.getBasePackage() + ".model.dao." + componentName.toLowerCase() + "." + componentName + "Dao";
	}
	
	public static String getEntityPath(String componentName){
		return OptionRule.getBasePath() + "/" + getEntityName(componentName).replace(".", "/");
	}
	
	public static String getEntityName(String componentName){
		return OptionRule.getBasePackage() + ".model.entity." + componentName + "Entity";
	}

	public static String getClassName(String clsName){
		int index = clsName.lastIndexOf('.');
		if (index >= 0){
			return clsName.substring(index + 1);
		}
		return clsName;
	}
	
	public static String getPackageName(String clsName){
		int index = clsName.lastIndexOf('.');
		if (index >= 0){
			return clsName.substring(0, index);
		}
		return "";
	}
	
	public static boolean validatePackage(String name){
		return pkgPattern.matcher(name).find();
	}
	
	public static boolean validateClass(String name){
		return namePattern.matcher(name).find();
	}
}
