package com.project.speed.rule;

import java.util.regex.Pattern;

public class NamingRule {
	static Pattern namePattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");   
	static Pattern pkgPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*(.[a-zA-Z][a-zA-Z0-9_]*)*$");
	
	
	public static String getServletName(String packageName, String componentName){
		return packageName + ".controller.servlet." + componentName.toLowerCase() + "." + componentName + "Controller";
	}
	
	public static String getServiceName(String packageName, String componentName){
		return packageName + ".service." + componentName.toLowerCase() + "." + componentName + "Service";
	}
	
	public static String getDaoName(String packageName, String componentName){
		return packageName + ".model.dao." + componentName.toLowerCase() + "." + componentName + "Dao";
	}
	
	public static String getEntityName(String packageName, String componentName){
		return packageName + ".model.entity." + componentName.toLowerCase() + "." + componentName + "Entity";
	}
	
	public static String getDaoName(String entityName){
		String componentName = NamingRule.getClassName(entityName).replace("Entity", "");
		String pkg = getPackageName(getPackageName(getPackageName(getPackageName(entityName))));
		return getDaoName(pkg, componentName);
		//return pkg + ".dao." + clsName.replace("Entity", "Dao");
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
