package cn.master.backend.config;

import lombok.Data;

/**
 * @author create by 11's papa on 2022/12/28-14:38
 */
@Data
public class ResponseInfo<T> {
    /**
     * 状态码
     */
    protected int code;

    /**
     * 响应信息
     */
    protected String message;

    /**
     * 返回数据
     */
    private T data;

    public static <T> ResponseInfo<T> success() {
        return new ResponseInfo<>();
    }

    public static <T> ResponseInfo<T> success(T data) {
        return new ResponseInfo<>(data);
    }

    public static <T> ResponseInfo<T> fail(String message) {
        return new ResponseInfo<>(ResponseCodeEnums.FAIL.getCode(), message);
    }

    public ResponseInfo() {
        this.code = ResponseCodeEnums.SUCCESS.getCode();
        this.message = ResponseCodeEnums.SUCCESS.getMessage();
    }

    public ResponseInfo(ResponseCodeEnums statusEnums) {
        this.code = statusEnums.getCode();
        this.message = statusEnums.getMessage();
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     */
    public ResponseInfo(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    /**
     * 有数据返回时，状态码为200，默认提示信息为“操作成功！”
     */
    public ResponseInfo(T data) {
        this.data = data;
        this.code = ResponseCodeEnums.SUCCESS.getCode();
        this.message = ResponseCodeEnums.SUCCESS.getMessage();
    }

    /**
     * 有数据返回，状态码为 200，人为指定提示信息
     */
    public ResponseInfo(T data, String msg) {
        this.data = data;
        this.code = ResponseCodeEnums.SUCCESS.getCode();
        this.message = msg;
    }

    public ResponseInfo(int code, String msg, T data) {
        this.data = data;
        this.message = msg;
        this.code = code;
    }
}
