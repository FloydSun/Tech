package com.project.speed.request;

import java.util.LinkedList;
import java.util.Queue;

public class RequestServer {
	private static Queue<Request> reqQueue = new LinkedList<Request>();
	static boolean start = false;

	public static boolean isRunning(){
		return start;
	}
	
	public static void post(Request cmd){
		if (start){
			reqQueue.offer(cmd);
		};
	}
	
	public static Request get(){
		return reqQueue.poll();
	}
	
	public static void stop(){
		reqQueue.clear();
		start = false;
	}
	
	public static void start(){
		start = true;
	}
	
	
}
