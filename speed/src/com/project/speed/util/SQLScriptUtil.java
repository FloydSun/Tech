package com.project.speed.util;

import java.util.regex.Pattern;

public class SQLScriptUtil {
	static String CREATE_TABLE = "create\\s+table\\s+%s\\s*\\((.*)\\)";
	private static Pattern getCreateTablePatten(String tableName){
		return Pattern.compile(String.format(CREATE_TABLE, tableName), Pattern.CASE_INSENSITIVE);
	}
	
}
