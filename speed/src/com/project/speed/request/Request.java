package com.project.speed.request;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

	public final static String FRAME = "frame";
	public final static String SERVLET = "servlet";
	public final static String SERVICE = "service";
	public final static String ENTITY = "entity";
	public final static String UIFRAME = "uiframe";
	public final static String UIPLUGIN = "uiplugin";
	public final static String DAO = "dao";
	public final static String EXIT = "exit";
	public final static String SWD = "swd";
	public final static String PKG = "pkg";
	public final static String FPKG = "fpkg";
	public final static String HELP = "help";
	public final static String PROJECT = "project";
	public final static String WEBSERVICE = "webservice";
	public final static String PWD = "pwd";
	public final static String LS = "ls";
	
	String type;
	List<String> args;
	
	public Request(String type, List<String> args) {
		super();
		this.type = type;
		this.args = args;
	}
	
	public Request(String type, String[] args) {
		super();
		this.type = type;
		this.args = new ArrayList<String>();
		for (int i = 0; i < args.length; ++i){
			this.args.add(args[i]);
		}
	}

	static Pattern quotationPattern = Pattern.compile("\\s+'.*'\\s*");
	static Pattern empthPattern = Pattern.compile("\\s+");
	
	public static Request parse(String req){
		List<String> args = new ArrayList<String>();
		Matcher matcher = quotationPattern.matcher(req);
		List<String> groups = new ArrayList<String>();
		if (matcher.find()){
			for (int i = 0, count = matcher.groupCount(); i <= count; ++i){
				String tmp = matcher.group(i).trim();
				groups.add(tmp.substring(1, tmp.length() - 1));
			}
			
			req = matcher.replaceAll(" __tag__ ");
		}
		matcher = empthPattern.matcher(req);
		if (matcher.find()){
			req = matcher.replaceAll(" ");
		}
		
		String[] ret = req.split(" ");		
		for (int i = 0; i < ret.length; ++i){
			if ("__tag__".equals(ret[i])){
				args.add(groups.remove(0));
			}
			else{
				args.add(ret[i]);
			}
		}
		
		String type = null;
		if (!args.isEmpty()){
			type = args.remove(0);
		}
		
		return new Request(type, args);
	}

	public String getType() {
		return type;
	}

	public List<String> getArgs() {
		return args;
	}
	
	
}
