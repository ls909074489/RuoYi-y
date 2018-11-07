package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public String page(){
        return prefix + "/websocket_page";
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
