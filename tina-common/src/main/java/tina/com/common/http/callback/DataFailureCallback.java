package tina.com.common.http.callback;

public interface DataFailureCallback<T> {
    void failure(int code, String errorMessage);
}
