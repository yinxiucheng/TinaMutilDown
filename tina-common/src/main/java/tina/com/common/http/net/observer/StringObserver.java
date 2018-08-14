package tina.com.common.http.net.observer;

import android.app.Dialog;
import android.text.TextUtils;

import io.reactivex.disposables.Disposable;
import tina.com.common.http.net.RxHttpUtils;
import tina.com.common.http.net.base.BaseStringObserver;
import tina.com.common.http.utils.ToastUtil;


/**
 * Created by Allen on 2017/10/31.
 *
 * @author Allen
 *         <p>
 *         自定义Observer 处理string回调
 */

public abstract class StringObserver extends BaseStringObserver {

    private Dialog mProgressDialog;

    public StringObserver() {
    }

    public StringObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param data 结果
     */
    protected abstract void onSuccess(String data);


    @Override
    public void doOnSubscribe(Disposable d) {
        //RxHttpUtils.addDisposable(d);
        RxHttpUtils.addToCompositeDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        dismissLoading();
        if (!isHideToast()&& !TextUtils.isEmpty(errorMsg)) {
            ToastUtil.show(errorMsg);
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(String string) {
        onSuccess(string);
    }


    @Override
    public void doOnCompleted() {
        dismissLoading();
    }

    /**
     * 隐藏loading对话框
     */
    private void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
