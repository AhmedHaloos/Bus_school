package com.eng.ashm.buschool.data;

/**
 * Result class is responsible for carrying the datasource request result
 * either the request is succeeded or get an error.
 */
public final class Result<T> {

    public static final int ERROR = -1;
    public static final int SUCCEED = 1;
    private T t = null;
    private Exception ex = null;
    public int STATE = ERROR;
    private Result(T t, Exception ex){
        this.t = t;
        this.ex = ex;
    }
    /**
     *
     * @param t
     * @param ex
     * @param <T>
     * @return
     */
    public final static <T> Result<T> createResult(T t, Exception ex){
        Result<T> result = null;
        if (ex == null) {
            result = new Result(t, null);
            result.STATE = SUCCEED;
        }
        else if (t == null){
             result = new Result<>(null, ex);
             result.STATE = ERROR;
        }
        return result;
    }
    /**
     *
     * @return
     */
    public final T getResult(){
        return t;
    }
    /**
     *
     * @return
     */
    public final Exception getError(){
        return ex;
    }

}
