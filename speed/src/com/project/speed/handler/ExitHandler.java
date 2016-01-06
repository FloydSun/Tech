package com.project.speed.handler;

import com.project.speed.request.Request;
import com.project.speed.request.RequestServer;


//exit
public class ExitHandler extends Handler {

	@Override
	public boolean onHandle(Request cmd) {
		if (Request.EXIT.equals(cmd.getType())){
			RequestServer.stop();
			return true;
		}
		return false;
	}

}
