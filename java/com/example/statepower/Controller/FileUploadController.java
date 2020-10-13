package com.example.statepower.Controller;

import com.example.statepower.bean.ResponseBean;
import com.example.statepower.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


@RestController()
@RequestMapping("/file")
public class FileUploadController {

    @Autowired
    private FileService fileService;
    /**
     * 实现文件上传
     * */
    @PostMapping("/img")
    public ResponseBean fileUpload(@RequestParam("file") MultipartFile uploadfile) {
        if (uploadfile.isEmpty()) {
            return new ResponseBean(500,"error","文件上传失败");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String format = sdf.format(new Date());
        File file = new File(format);

        String oldName = uploadfile.getOriginalFilename();
        assert oldName != null;
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."),oldName.length());

        String filename=file.getAbsolutePath() + File.separator + newName;
        File dest = new File(filename);

        if(!dest.isDirectory()){
            dest.mkdirs();//递归生成文件夹
        }
        try {
            uploadfile.transferTo(dest); //保存文件
            //fileService.insql(filename);
            return new ResponseBean(200,"succ",null);
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseBean(500,"error","文件上传失败");
        }
    }


}