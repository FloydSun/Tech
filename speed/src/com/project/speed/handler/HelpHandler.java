package com.project.speed.handler;

import com.project.speed.request.Request;


public class HelpHandler extends Handler {

	@Override
	public boolean onHandle(Request cmd) {
		System.out.println("============================================");
		System.out.println(Request.PROJECT  + "\t<project name> <package name>");
		System.out.println(Request.FRAME  + "\t<component Name> <transaction> [<entityName> <tableName> <db name>]");
		System.out.println(Request.SERVLET  + "\t<component Name> <request map>");
		System.out.println(Request.WEBSERVICE  + "\t<component Name> <request map>");
		System.out.println(Request.SERVICE  + "\t<component Name> <transaction> [-w|-s <controller name>]");
		System.out.println(Request.UIFRAME + "\t<component Name> [method path file]");
		System.out.println(Request.UIPLUGIN + "\t<component Name> <uiframe component Name> [plugin file path]");
		System.out.println(Request.DAO  + "\t<component Name> <db name>  <transaction> [<service name>]");
		System.out.println(Request.ENTITY  + "\t<-f | -n> <component Name> <table name> [script path]");
		System.out.println(Request.SWD  + "\t<path>" + " 设置工作路径 ");
		System.out.println(Request.PKG  + "\t[<package>]");
		System.out.println(Request.FPKG  + "\t[<frame package>]");
		System.out.println(Request.PWD + "\t打印工作路径 ");
		System.out.println(Request.LS + "\t显示工作路径下文件 ");
		System.out.println(Request.EXIT);
		System.out.println(Request.HELP);
		System.out.println("可以通过使用file:///进行脚本命令执行");
		System.out.println("===========================================");
		return true;
	}

}
