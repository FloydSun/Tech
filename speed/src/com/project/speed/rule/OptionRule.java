package com.project.speed.rule;

public class OptionRule {
	
	static String basePath = "";
	static String basePackage = "";
	public static String getBasePath() {
		return basePath;
	}
	public static void setBasePath(String basePath) {
		if (basePath.charAt(basePath.length() - 1) == '\\' || basePath.charAt(basePath.length() - 1) == '/'){
			OptionRule.basePath = basePath.substring(0, basePath.length() - 1);
		}else{
			OptionRule.basePath = basePath;
		}
	}
	public static String getBasePackage() {
		return basePackage;
	}
	public static void setBasePackage(String basePackage) {
		OptionRule.basePackage = basePackage;
	}
	
	
}
