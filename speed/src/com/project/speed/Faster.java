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
import com.project.speed.handler.ProjectTemplateHandler;
import com.project.speed.handler.ServiceHandler;
import com.project.speed.handler.ServletHandler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;

public class Faster {

	
	
	interface RequestProducer{
		Request produce();
	}
	
	
	static RequestProducer getConsoleProducer(){
		return new RequestProducer(){
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			@Override
			public Request produce() {
				try {
					System.out.print(">");
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
			.chain(new OptionHandler())
			.chain(new ExitHandler())
			.chain(new ServletHandler())
			.chain(new ServiceHandler())
			.chain(new DaoHandler())
			.chain(new EntityHandler())
			.chain(new ProjectTemplateHandler())
			.chain(new HelpHandler());
	
		RequestServer.start();
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
