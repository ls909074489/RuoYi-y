package com.ruoyi.web.controller.ls.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.ruoyi.common.base.AjaxResult;
import com.ruoyi.framework.util.FileUploadUtils;
import com.ruoyi.web.controller.ls.bean.ActionResultModel;


@Controller
@RequestMapping("/common/attachment")
public class AttachmentController {

    
	@ResponseBody
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file,
            HttpServletRequest request){
		AjaxResult result=new AjaxResult();
		try {
			result.put("flag", true);
			result.put("msg", FileUploadUtils.upload(file));
		} catch (IOException e) {
			result.put("suc", false);
			e.printStackTrace();
		}
        return result;
    }
	
	
	
	@ResponseBody
	@PostMapping("/uploads")
    public ActionResultModel<String> uploads(HttpServletRequest request) throws Exception {
		ActionResultModel<String> arm = new ActionResultModel<String>();
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        // 遍历文件参数（即formData的file）
        Iterator<String> iterator = req.getFileNames();
        List<String> records=new ArrayList<String>();
        while (iterator.hasNext()) {
            MultipartFile file = req.getFile(iterator.next());
//            String fileNames = file.getOriginalFilename();
//            // 文件名
//            fileNames = new String(fileNames.getBytes("UTF-8"));
//            // 文件内容
//            byte[] content = file.getBytes();
//
//            FileUtils.writeByteArrayToFile(new File(fileNames), content);
            
            String path=FileUploadUtils.upload(file);
            records.add(path);
        }
        arm.setRecords(records);
        arm.setSuc(true);
        return arm;
    }
}
