package cn.itsource.common.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.FastDfsApiOpr;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FastDfsController {

    @PostMapping("/fastdfs")
    public AjaxResult upload(MultipartFile file){
        String fileName = file.getOriginalFilename();
        //获取文件的扩展名
        fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
        try {
            String upload = FastDfsApiOpr.upload(file.getBytes(), file.getOriginalFilename());
            return AjaxResult.me().setSuccess(true).setMessage("/保存成功").setRestObj(upload);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("/系统异常"+e.getMessage());
        }
    }
    @GetMapping("fastdfs")
    public AjaxResult delete(String fileId){
        try {
            fileId = fileId.substring(1);
            String groupName = fileId.substring(0,fileId.indexOf("/"));
            String fileName = fileId.substring(fileId.indexOf("/")+1);
            FastDfsApiOpr.delete(groupName,fileName);
            return AjaxResult.me().setSuccess(true).setMessage("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("删除失败"+e.getMessage());
        }

    }

}
