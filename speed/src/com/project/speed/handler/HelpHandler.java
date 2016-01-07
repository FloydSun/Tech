package com.project.speed.handler;

import com.project.speed.request.Request;


public class HelpHandler extends Handler {

	@Override
	public boolean onHandle(Request cmd) {
		System.out.println("============================================");
		System.out.println(Request.PROJECT  + " <project name> <package name>");
		System.out.println(Request.FRAME  + " <component Name> <transaction> [<entityName> <tableName> <db name>]");
		System.out.println(Request.SERVLET  + " <component Name> <request map>");
		System.out.println(Request.SERVICE  + " <component Name> <transaction> [<servlet name>]");
		System.out.println(Request.DAO  + " <component Name> <db name>  <transaction> [<service name>]");
		System.out.println(Request.ENTITY  + " <-f | -n> <component Name> <table name> ");
		System.out.println(Request.SWD  + " <path>" + " 设置工作路径 ");
		System.out.println(Request.PKG  + " [<package>]");
		System.out.println(Request.PWD + " 打印工作路径 ");
		System.out.println(Request.LS);
		System.out.println(Request.EXIT);
		System.out.println(Request.HELP);
		System.out.println("===========================================");
		return true;
	}

}
