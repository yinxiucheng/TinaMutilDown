package tina.com.common.http.callback;

import android.support.annotation.Nullable;

public interface DataSuccessCallback<T> {
  void success(@Nullable T t);
}
