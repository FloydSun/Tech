package com.project.speed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.speed.handler.ExitHandler;
import com.project.speed.handler.Handler;
import com.project.speed.handler.HelpHandler;
import com.project.speed.handler.OptionHandler;
import com.project.speed.handler.internal.AddMethodHandler;
import com.project.speed.handler.server.DaoHandler;
import com.project.speed.handler.server.EntityHandler;
import com.project.speed.handler.server.FrameHandler;
import com.project.speed.handler.server.ProjectHandler;
import com.project.speed.handler.server.ServiceHandler;
import com.project.speed.handler.server.ServletHandler;
import com.project.speed.handler.server.WebServiceHandler;
import com.project.speed.handler.web.UIFrameHandler;
import com.project.speed.handler.web.UIPluginHandler;
import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;
import com.project.speed.rule.OptionRule;

public class Speed {

	interface RequestProducer {
		Request produce();
	}

	public static String getProjectPath() {
		java.net.URL url = Speed.class.getProtectionDomain().getCodeSource()
				.getLocation();
		String filePath = null;
		try {
			filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filePath.endsWith(".jar"))
			filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		java.io.File file = new java.io.File(filePath);
		filePath = file.getAbsolutePath();
		return filePath;
	}

	static RequestProducer getConsoleProducer() {
		return new RequestProducer() {

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

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

	public static void start(RequestProducer producer) {

 		if (null == producer) {
			return;
		}

		Handler requestHandler = new FrameHandler();
		requestHandler.add(new ExitHandler()).add(new ProjectHandler())
				.add(new OptionHandler()).add(new WebServiceHandler())
				.add(new ServletHandler()).add(new ServiceHandler())
				.add(new DaoHandler()).add(new EntityHandler())
				.add(new UIFrameHandler())
				.add(new UIPluginHandler())
				.add(new AddMethodHandler())				
				.add(new HelpHandler());

		RequestServer.start();
		OptionRule.setBasePath(getProjectPath());
		Request req = null;
		while (RequestServer.isRunning()) {
			RequestServer.post(producer.produce());
			while ((req = RequestServer.get()) != null) {
				try {
					requestHandler.handle(req);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {

		RequestProducer producer = null;
		if (args.length > 0) {
			producer = getArgsProducer(args);
		} else {
			producer = getConsoleProducer();
		}

		start(producer);
	}

	private static RequestProducer getArgsProducer(String[] args) {
		if (args[0].startsWith("file:///")) {
			String scriptPath = args[0].replaceFirst("file:///", "");
			final File script = new File(scriptPath);
			if (script.isFile() && script.exists()) {
				try {
					return new RequestProducer() {
						Pattern blankPattern = Pattern.compile("^\\s*");   
						InputStreamReader read = new InputStreamReader(
								new FileInputStream(script), "utf-8");
						BufferedReader bufferedReader = new BufferedReader(read);

						boolean isBlank(String scriptLine){
							if (null != scriptLine && blankPattern.matcher(scriptLine).matches()){
								Matcher matcher = blankPattern.matcher(scriptLine);
								if (matcher.find() && matcher.end() == scriptLine.length()){
									System.out.println(scriptLine);
									return true;
								}
							}
							return false;
						}
						
						boolean isComment(String scriptLine){
							if (scriptLine.startsWith("//")){
								System.out.println(scriptLine);
								return true;
							}
							return false;
						}
						
						@Override
						public Request produce() {
							try {
								String scriptLine = bufferedReader.readLine();
								while (scriptLine != null && (isBlank(scriptLine) || isComment(scriptLine))){
									scriptLine = bufferedReader.readLine();
								};
								if (null != scriptLine) {
									System.out.println("√ " + scriptLine);
									return Request.parse(scriptLine);
								} else {
									read.close();
									return Request.parse(Request.EXIT);
								}
							} catch (IOException e) {
								e.printStackTrace();
								return Request.parse(Request.EXIT);
							}
						}
					};
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		} else {
			return new RequestProducer() {

				private int i = 0;
				RequestProducer producer = getConsoleProducer();
				@Override
				public Request produce() {
					if (i < args.length) {
						return Request.parse(args[i++]);
					} else {
						return producer.produce();
					}
				}
			};
		}
	}

}
