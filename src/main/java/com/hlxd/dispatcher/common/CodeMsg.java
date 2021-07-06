package com.hlxd.dispatcher.common;

/**
 * 错误CODE和MSG
 **/
public class CodeMsg {

    private int retCode;
    private String message;

    // 按照模块定义CodeMsg
    // 通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg BUSY = new CodeMsg(100, "没有空闲的应用");


    private CodeMsg(int retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }

    public int getRetCode() {
        return retCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

