package com.iflytek.aiui.tts;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.iflytek.aiui.utils.DataResult;
import com.iflytek.aiui.utils.DateUtils;
import okhttp3.*;
import okio.ByteString;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 语音合成流式 WebAPI 接口调用示例 接口文档（必看）：https://www.xfyun.cn/doc/tts/online_tts/API.html
 * 语音合成流式WebAPI 服务，发音人使用方式：登陆开放平台https://www.xfyun.cn/后，到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值
 * 错误码链接：https://www.xfyun.cn/document/error-code （code返回错误码时必看）
 * 小语种需要传输小语种文本、使用小语种发音人vcn、tte=unicode以及修改文本编码方式
 * @author iflytek
 */

public class WebTTSWS {
    private static final String hostUrl = "https://tts-api.xfyun.cn/v2/tts"; //http url 不支持解析 ws/wss schema
    private static final String appid = "5d2e7371";//到控制台-语音合成页面获取
    private static final String apiSecret = "de6d3f8ebca5b4097ee2c8d2a8412d42";//到控制台-语音合成页面获取
    private static final String apiKey = "285b9d1617ff7869bdeb21b67949641d";//到控制台-语音合成页面获取
    private static final String text = "您好，欢迎致电天津市大学软件学院B105班全体师生开发的语音转写功能，我们班级专业从事人工智能、机器学习、神经网络等技术的开发、设计与研究，我们班秉承质量第一、科技为本、诚信经营、客户至上的经营理念，为您提供最实惠的价格、最优良的品质、最周到的服务！电话正在接通中，请稍后...";
    private static final String FILE_PATH = "D:\\JavaEE\\intellsoft\\aiui\\target\\classes\\static\\webttsws\\";
    public static final Gson json = new Gson();
    private DataResult<String> dataResult = DataResult.success();
    public static void main(String[] args) throws Exception {
        // 构建鉴权url
//        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
//        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
//        Request request = new Request.Builder().url(url).build();
//        // 存放音频的文件
//        String fileName = DateUtils.getDateStr() + ".mp3";
//        WebSocket webSocket = client.newWebSocket(request, new TTSWebSocketListener("xiaoyan", 50, 50,fileName));

        //ttsText2Voice(text, "xiaoyan", 50, 50);
    }

    public DataResult<String> ttsText2Voice(String text, String vcn, int volume, int speed) throws Exception {
        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        String fileName = DateUtils.getDateStr() + ".mp3";
        // 存放音频的文件
        WebSocket webSocket = client.newWebSocket(request, new TTSWebSocketListener(text, vcn, volume, speed, fileName));
        return dataResult;
    }

    private class TTSWebSocketListener extends WebSocketListener {
        private String vcn = "xiaoyan";
        private int volume = 50;
        private int speed = 50;
        private String fileName;
        private String text;
        private FileOutputStream os;

        public TTSWebSocketListener(String text, String vcn, int volume, int speed, String fileName) throws IOException {
            this(fileName);
            this.volume = volume;
            this.speed = speed;
            this.text = text;
            this.vcn = vcn;
        }

        public TTSWebSocketListener(String fileName) throws IOException {
            this.fileName = fileName;
            File f = new File(FILE_PATH + this.fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            this.os = new FileOutputStream(f);
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            try {
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //发送数据
            JsonObject frame = new JsonObject();
            JsonObject business = new JsonObject();
            JsonObject common = new JsonObject();
            JsonObject data = new JsonObject();
            // 填充common
            common.addProperty("app_id", appid);
            //填充business
            business.addProperty("aue", "lame");
            business.addProperty("sfl", 1);
            business.addProperty("tte", "UTF8");//小语种必须使用UNICODE编码
            business.addProperty("vcn", this.vcn);//到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
            business.addProperty("pitch", 50); // 音高
            business.addProperty("speed", speed); // 音速
            business.addProperty("volume", volume); // 音量
            business.addProperty("bgs", 1); // 背景音乐
            business.addProperty("reg", "0"); // 设置英文发音方式
            //填充data
            data.addProperty("status", 2);//固定位2
            try {
                data.addProperty("text", Base64.getEncoder().encodeToString(this.text.getBytes("utf8")));
                //使用小语种须使用下面的代码，此处的unicode指的是 utf16小端的编码方式，即"UTF-16LE"”
                //data.addProperty("text", Base64.getEncoder().encodeToString(text.getBytes("UTF-16LE")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //填充frame
            frame.add("common", common);
            frame.add("business", business);
            frame.add("data", data);
            webSocket.send(frame.toString());
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            //处理返回数据
            System.out.println("receive=>" + text);
            ResponseData resp = null;
            try {
                resp = json.fromJson(text, ResponseData.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (resp != null) {
                if (resp.getCode() != 0) {
                    System.out.println("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                    return;
                }
                if (resp.getData() != null) {
                    String result = resp.getData().audio;
                    byte[] audio = Base64.getDecoder().decode(result);
                    try {
                        os.write(audio);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (resp.getData().status == 2) {
                        // todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                        System.out.println("session end ");
                        System.out.println("合成的音频文件名字為：" + this.fileName);
                        webSocket.close(1000, "");
                        dataResult.setCode(2);
                        dataResult.setData("/webttsws/"+this.fileName);
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            System.out.println("socket closing");
        }
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            System.out.println("socket closed");
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            System.out.println("connection failed");
        }
    }


    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
                append("date: ").append(date).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }
    public static class ResponseData {
        private int code;
        private String message;
        private String sid;
        private Data data;
        public int getCode() {
            return code;
        }
        public String getMessage() {
            return this.message;
        }
        public String getSid() {
            return sid;
        }
        public Data getData() {
            return data;
        }
    }
    public static class Data {
        private int status;  //标志音频是否返回结束  status=1，表示后续还有音频返回，status=2表示所有的音频已经返回
        private String audio;  //返回的音频，base64 编码
        private String ced;  // 合成进度
    }
}