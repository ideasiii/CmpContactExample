package api;

/**
 * Created by Jugo on 2019/9/16
 */

public abstract class ErrorCode
{
    public static final int ERROR_SUCCESS = 0;
    public static final int ERROR_CONNECT = -1;
    public static final int ERROR_JSON = -2;
    public static final int ERROR_EXCEPTION = -3;
    public static final int ERROR_SEND = -4;
    public static final int ERROR_RECEIVE = -5;
}
