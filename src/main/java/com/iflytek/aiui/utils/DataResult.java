package com.iflytek.aiui.utils;

import com.iflytek.aiui.exception.BaseResponseCode;
import com.iflytek.aiui.exception.ResponseCodeInterface;

public class DataResult<T>{
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg=null;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data=null;
    }


    public DataResult() {
        this.code= BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
        this.data=null;
    }

    public DataResult(T data) {
        this.data = data;
        this.code= BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface) {
        this.data = null;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }

    public DataResult(ResponseCodeInterface responseCodeInterface, T data) {
        this.data = data;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }
    /**
     * 操作成功 data为null
     * @Author:      小霍
     * @CreateDate:  2019/9/4 23:08
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 23:08
     * @Version:     0.0.1
     * @param
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    public static <T> DataResult success(){
        return new <T>DataResult();
    }
    /**
     * 操作成功 data 不为null
     * @Author:      小霍
     * @CreateDate:  2019/9/4 23:09
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 23:09
     * @Version:     0.0.1
     * @param data
     * @return       com.xh.lesson.utils.DataResult<T>
     * @throws
     */
    public static <T> DataResult success(T data){
        return new <T>DataResult(data);
    }
    /**
     * 自定义 返回操作 data 可控
     * @Author:      小霍
     * @CreateDate:  2019/9/4 23:15
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 23:15
     * @Version:     0.0.1
     * @param code
     * @param msg
     * @param data
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(int code, String msg, T data){
        return new <T>DataResult(code,msg,data);
    }
    /**
     *  自定义返回  data为null
     * @Author:      小霍
     * @CreateDate:  2019/9/4 23:15
     * @UpdateUser:
     * @UpdateDate:  2019/9/4 23:15
     * @Version:     0.0.1
     * @param code
     * @param msg
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(int code, String msg){
        return new <T>DataResult(code,msg);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data为空
     * @param responseCode
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(BaseResponseCode responseCode){
        return new <T>DataResult(responseCode);
    }
    /**
     * 自定义返回 入参一般是异常code枚举 data 可控
     * @param responseCode
     * @param data
     * @return       com.xh.lesson.utils.DataResult
     * @throws
     */
    public static <T> DataResult getResult(BaseResponseCode responseCode, T data){

        return new <T>DataResult(responseCode,data);
    }
}
