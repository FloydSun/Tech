package com.project.speed.handler;

import com.project.speed.request.Request;

public abstract class Handler {
	
	Handler next;
	
	public Handler chain(Handler next){
		this.next = next;
		return next;
	}

	public void handle(Request req){
		if (req != null && !onHandle(req)){
			callNext(req);
		}
	}
	
	abstract public boolean onHandle(Request req);
	
	protected void callNext(Request req){
		if (null != next){
			next.handle(req);
		}
	}
}
