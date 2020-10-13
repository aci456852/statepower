package com.example.statepower.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.statepower.bean.ExportExcel;
import com.example.statepower.bean.Record;
import com.example.statepower.bean.ResponseBean;
import com.example.statepower.service.FileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class RecordController {

    @Autowired
    private FileService fileService;

    //在export界面显示表格用
    @RequestMapping("/listAll")
    @ResponseBody
    public String listAll(@RequestParam String time1,String time2,int pageNumber,int pageSize,HttpServletResponse response) throws Exception {
        System.out.println("listAll time:"+time1+" "+time2);

        response.setContentType("text/json");
        response.setCharacterEncoding("utf-8");

        //System.out.println("pageNumber: "+pageNumber);
        //System.out.println("pageSize: "+pageSize);
        PageHelper.startPage(pageNumber,pageSize);
        List<Record> ins;
        if(time1==""||time2=="") ins = fileService.listALL();
        else ins = fileService.listTime(time1,time2);

        int total = ins.size();
        //System.out.println("ins: "+ins);
        PageInfo<Record> pageInfo = new PageInfo<>(ins); //没什么用处
//        System.out.println("pageInfo-pageNumber: "+pageInfo.getPageNum());
//        System.out.println("pageInfo-pageSize: "+pageInfo.getPageSize());
//        System.out.println("pageInfo-getTotal: "+pageInfo.getTotal());
//        System.out.println("pageInfo-getPages: "+pageInfo.getPages());
        JSONObject result = new JSONObject();
        result.put("rows",ins); //按道理应该是result.put("rows",pageInfo);
        result.put("total",pageInfo.getTotal());
        //System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    //在export界面画图用
    @RequestMapping("/chart")
    @ResponseBody
    public int[] chart(@RequestParam String time1,String time2) throws Exception {
        //System.out.println("time:"+time);
        int[] res={0,0,0,0,0,0,0,0,0,0};
        List<Record> ins;
        if(time1==""||time2=="") ins = fileService.listALL();
        else ins = fileService.listTime(time1,time2);
        for(int i=0;i<ins.size();i++)
        {
            int j=ins.get(i).getRnumber();
            res[j]++;
            //System.out.println(j + ":"+res[j]);
        }
        return res;
    }

    //插入数据用
    @RequestMapping("/insert")
    @ResponseBody
    public ResponseBean fileUpload(@RequestParam("rnumber") String rnumber,@RequestParam("ritem") String ritem,
                                   @RequestParam("rseverity") String rseverity,@RequestParam("rspecific") String rspecific,
                                   @RequestParam("rproblem") String rproblem,@RequestParam("file") MultipartFile uploadfile) {
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
            byte[] img = getBytesByFile(dest);
            fileService.Insert(Integer.parseInt(rnumber),ritem,rseverity,rspecific,rproblem,img);
            System.out.println("success!!!!");
            return new ResponseBean(200,"succ",null);
        } catch (IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new ResponseBean(500,"error","文件上传失败");
        }
    }

    public static byte[] getBytesByFile(File file) {
        try {
            //获取输入流
            FileInputStream fis = new FileInputStream(file);
            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] blobToByte(Blob blob) throws Exception{
        InputStream is = (InputStream) blob .getBinaryStream();
        byte[] bs = new byte[1000];
        int len = -1;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while((len=is.read(bs)) != -1){
            os.write(bs, 0, len);
        }
        byte[] bt = os.toByteArray();
        os.close();
        is.close();
        return bt;
    }


    //导出成excel
    @RequestMapping("/exportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response,String time1,String time2) throws IOException, IllegalAccessException{
        System.out.println("exportExcel time:"+time1+" "+time2);

        //文件名
        String fileName = time1+"-"+time2+"安全稽查数字化模块建设报表";
        //sheet名
        String sheetName = time1+"-"+time2+"报表";
        //设置表头
        List<String> titleList = new ArrayList<>();
        titleList.add("记录号");
        titleList.add("序号");
        titleList.add("检查项目");
        titleList.add("严重程度");
        titleList.add("具体问题展现");
        titleList.add("检查问题");
        titleList.add("图片");
        titleList.add("日期");
        //设置表格内容
        List<Record> dataList = fileService.listTime(time1,time2);
        //生成表格
        HSSFWorkbook workbook = ExportExcel.createExcel(sheetName,titleList, dataList);

        OutputStream output=response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename="+new String(fileName.getBytes("GB2312"),"ISO8859-1")+".xls");
        response.setContentType("application/x-xls");
        workbook.write(output);
        output.close();
    }
}
