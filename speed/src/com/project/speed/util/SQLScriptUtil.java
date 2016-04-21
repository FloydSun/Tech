package com.project.speed.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLScriptUtil {
	
	public static class TableField{
		String name;
		String type;
		public TableField(String name, String type) {
			super();
			this.name = name;
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
	
	//\\((.*)\\)
	static String CREATE_TABLE = "create\\s+table\\s+%s\\s*";
	static String BEGIN_TABLE = "create\\s+table\\s+%s\\s*\\(\\s+";
	static Pattern KH = Pattern.compile("\\((.*)\\)", Pattern.CASE_INSENSITIVE);
	static String FIELD_END = ",\\s+";
	private static Pattern getCreateTablePatten(String tableName){
		return Pattern.compile(String.format(CREATE_TABLE, tableName), Pattern.CASE_INSENSITIVE);
	}
	private static Pattern getBeginTablePatten(String tableName){
		return Pattern.compile(String.format(BEGIN_TABLE, tableName), Pattern.CASE_INSENSITIVE);
	}
	
	private static TableField getField(String rawField){
		rawField = rawField.trim();
		String[] fds = rawField.split("\\s+");
		if (fds.length > 2){
			return new TableField(fds[0], fds[1]);
		}
		return null;
	}
	
	public static List<TableField> getTableFields(String sqlscript, String tableName){
		List<TableField> ret = new ArrayList<TableField>();
		sqlscript = sqlscript.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("dbo.", "");
		Pattern tableDecl = getCreateTablePatten(tableName);
		Matcher matcher = tableDecl.matcher(sqlscript);
		if (matcher.find()){
			Pattern beginTableDecl = getBeginTablePatten(tableName);
			String decl = sqlscript.substring(matcher.start(), matcher.end() - 1);
			matcher = beginTableDecl.matcher(decl);
			if (matcher.find()){
				decl = decl.substring(matcher.start());
				decl = decl.replaceAll("\\((.*)\\)", "").replaceAll("\\s", " ");
				String[] rawFields = decl.split("\n");
				for (int i = 0; i < rawFields.length; ++i){
					TableField tf = getField(rawFields[i]);
					if (null != tf){
						ret.add(tf);
					}
					if (rawFields[i].lastIndexOf(",") < 0){
						break;
					}
				}
			}
			
		}
		return ret;
	}
	
}
