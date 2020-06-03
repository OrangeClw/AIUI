package com.iflytek.aiui.controller;

import com.iflytek.aiui.iat.WebIATWS;
import com.iflytek.aiui.test.WebITS;
import com.iflytek.aiui.utils.DataResult;
import com.iflytek.aiui.utils.UuidUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;


@RestController
@RequestMapping("/aiui")
public class AIUIController {

    @PostMapping("/voice2text")
    public DataResult<String> voice2text(@RequestParam(value = "wavData", required = false) MultipartFile multfile, HttpSession session) throws Exception {
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String suffix = ".wav";
        // 用uuid作为文件名，防止生成的临时文件重复
        File tempFile = new File("D:\\JavaEE\\intellsoft\\upload\\iat\\");
        if (!tempFile.exists()) {
            tempFile.mkdir();
        }
        final File iatFile = new File("D:\\JavaEE\\intellsoft\\upload\\iat\\" + UuidUtils.getUuid32() + suffix);
        // MultipartFile to File
        multfile.transferTo(iatFile);
        //String temp = relativelyPath+File.separator+"temp";
        String destTemp = "D:\\JavaEE\\intellsoft\\upload\\iat\\" + iatFile.getName().substring(0, iatFile.getName().lastIndexOf(".")) + ".pcm";
        // 调用cmd程序，转成pcm格式文件
        String command = "D:\\JavaEE\\intellsoft\\ffmpeg\\bin\\ffmpeg -y -i " + iatFile + " -acodec pcm_s16le -f s16le -ac 1 -ar 16000 " + destTemp;
        Process process = Runtime.getRuntime().exec(command);
        int status = process.waitFor();
        String message = "";

        if (status == 0) {
            // 调用讯飞接口，返回翻译后的结果
            WebIATWS ws = new WebIATWS(destTemp);
            try {
                DataResult<String> dataResult = ws.iat();
                session.setAttribute("iat_message", dataResult);
            } catch (Exception e) {
                e.printStackTrace();
                message = "您好，我是科大讯飞";
            }
        } else {
            System.out.println("音频文件转写失败");
            message = "音频文件转写失败";
        }
        //本地语音数据获取原始方式
//        WebIATWS ws = new WebIATWS("D:\\workspace_2020\\aiui\\src\\main\\resources\\iat\\16k_10.pcm");
//        DataResult<String> dataResult =  ws.iat();
//        session.setAttribute("iat_message", dataResult);
        return DataResult.success();
    }

    @RequestMapping("/iatSearch")
    public DataResult<String> iatSearch(HttpSession session) {
        DataResult<String> result = (DataResult<String>) session.getAttribute("iat_message");
        String data = result.getData();
        if (data != null) {
            data = data.replaceAll("[。. ，,]+", "");
            result.setData(data);
        }
        return result;
    }

    @RequestMapping("/iat")
    public DataResult<String> iat(HttpSession session) {
        return (DataResult<String>) session.getAttribute("iat_message");
    }

    @PostMapping("/its")
    public DataResult<String> webITS(String text, String from, String to) throws Exception {
        if(text == null || "".equals(text.trim())){
            DataResult<String> dataResult = null;
            dataResult.setData("");
            return dataResult;
        }
        // 只能用中文问题解决
        if("cn".equals(from) || "cn".equals(to)){
            if(from.equals(to)){
                return DataResult.success(text);
            }
            return DataResult.success(WebITS.itrans(text, from, to));
        }else {
            // 需要转两次
            String firstText = WebITS.itrans(text, from, "cn");
            return DataResult.success(WebITS.itrans(firstText, "cn", to));
        }

    }


}
