package cn.master.backend.exception;

/**
 * 自定义异常类
 *
 * @author create by 11's papa on 2022/12/28-14:40
 */
public class CustomException extends RuntimeException {
    public CustomException() {
        super();
    }

    public CustomException(String msg) {
        super(msg);
    }
}
