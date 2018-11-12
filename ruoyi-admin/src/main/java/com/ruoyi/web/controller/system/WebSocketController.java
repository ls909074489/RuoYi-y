package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.web.core.config.websocket.WebSocketServer;

@Controller
@RequestMapping("/system/websocket")
public class WebSocketController {

	 private String prefix = "system/websocket";
	
    @GetMapping()
    public String page(HttpServletRequest request){
    	String returnPage = prefix + "/websocket_pc_page";
    	String userAgent = request.getHeader("user-agent");
    	System.out.println(userAgent);
    	if(userAgent.indexOf("Android") != -1){
    	    //安卓
    		returnPage = prefix + "/websocket_phone_page";
    	}else if(userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1){
    	   //苹果
    		returnPage = prefix + "/websocket_phone_page";
    	}else{
    	    //电脑
    		returnPage = prefix + "/websocket_pc_page";
    	}
        return returnPage;
    }

    
	 @RequestMapping(value="/pushVideoListToWeb")
	 public @ResponseBody Map<String,Object> pushVideoListToWeb() {
		 Map<String,Object> result =new HashMap<String,Object>();
		 try {
			 WebSocketServer.sendInfo("有新客户呼入,sltAccountId:----");
			 result.put("operationResult", true);
		 }catch (IOException e) {
			 result.put("operationResult", true);
		 }
		 return result;
	 }

}
