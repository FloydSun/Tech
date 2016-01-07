package com.project.speed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.project.speed.handler.DaoHandler;
import com.project.speed.handler.EntityHandler;
import com.project.speed.handler.ExitHandler;
import com.project.speed.handler.FrameHandler;
import com.project.speed.handler.Handler;
import com.project.speed.handler.HelpHandler;
import com.project.speed.handler.OptionHandler;
import com.project.speed.handler.ProjectHandler;
import com.project.speed.handler.ServiceHandler;
import com.project.speed.handler.ServletHandler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.OptionRule;

public class Speed {

	
	
	interface RequestProducer{
		Request produce();
	}
	
	public static String getProjectPath() {
		 
	       java.net.URL url = Speed.class .getProtectionDomain().getCodeSource().getLocation();
	       String filePath = null ;
	       try {
	           filePath = java.net.URLDecoder.decode (url.getPath(), "utf-8");
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	    if (filePath.endsWith(".jar"))
	       filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
	    java.io.File file = new java.io.File(filePath);
	    filePath = file.getAbsolutePath();
	    return filePath;
	}
	
	
	static RequestProducer getConsoleProducer(){
		return new RequestProducer(){
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			@Override
			public Request produce() {
				try {
					System.out.print("speed : " + OptionRule.getBasePath() + " >");
					return Request.parse(br.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	public static void main(String[] args) throws IOException{		
		
		RequestProducer producer = null;
		if (args.length > 0){
			producer = getArgsProducer(args);
		}
		else{
			producer = getConsoleProducer();
		}

		Handler requestHandler = new FrameHandler();
		requestHandler
			.chain(new ExitHandler())
			.chain(new ProjectHandler())
			.chain(new OptionHandler())
			.chain(new ServletHandler())
			.chain(new ServiceHandler())
			.chain(new DaoHandler())
			.chain(new EntityHandler())
			.chain(new HelpHandler());
	
		RequestServer.start();
		OptionRule.setBasePath(getProjectPath());
		Request req = null;
		while (RequestServer.isRunning()){
			RequestServer.post(producer.produce());
			while ((req = RequestServer.get()) != null){
				requestHandler.handle(req);
			}
		}
	}

	private static RequestProducer getArgsProducer(String[] args) {
		return new RequestProducer(){

			private int i = 0;
			
			@Override
			public Request produce() {
				if (i < args.length){
					return Request.parse(args[i++]);
				} else{
					return Request.parse(Request.EXIT);
				}
			}
		};
	}
}
