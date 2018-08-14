package tina.com.live_base.base.mvp;

import android.support.annotation.NonNull;

/**
 * @author Ragnar
 * @date 2018/6/4
 */
public interface BaseMvpView {

    @NonNull
    BasePresenter initPresenter();

}
