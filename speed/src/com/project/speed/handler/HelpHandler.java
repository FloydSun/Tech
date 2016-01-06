package com.project.speed.handler;

import com.project.speed.request.Request;


public class HelpHandler extends Handler {

	@Override
	public boolean onHandle(Request cmd) {
		System.out.println("template <project name> <package name>");
		System.out.println("frame <package name> <component Name> <transaction> [<entityName> <tableName> <db name>]");
		System.out.println("servlet <class name> <request map>");
		System.out.println("service <class name> <transaction> [<servlet name>]");
		System.out.println("dao <class name> <db name>  <transaction> [<service name>]");
		System.out.println("entity <-i | -n> <class name> <table name> ");
		System.out.println("cd <path>");
		System.out.println("pkg <package>");
		return true;
	}

}
