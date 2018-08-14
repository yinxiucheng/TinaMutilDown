package tina.com.common.http.net.observer;

import android.app.Dialog;
import android.text.TextUtils;

import io.reactivex.disposables.Disposable;
import tina.com.common.http.net.ResponseCode;
import tina.com.common.http.net.RxHttpUtils;
import tina.com.common.http.net.base.BaseDataObserver;
import tina.com.common.http.net.bean.BaseData;
import tina.com.common.http.utils.ToastUtil;

public abstract class DataObserver<T> extends BaseDataObserver<T> {

    private Dialog mProgressDialog;

    public DataObserver() {
    }

    public DataObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void onError(int errorCode, String errorMsg);

    /**
     * 成功回调
     *
     * @param data 结果
     */
    protected abstract void onSuccess(T data);

    @Override
    public void doOnSubscribe(Disposable d) {
        //RxHttpUtils.addDisposable(d);
        RxHttpUtils.addToCompositeDisposable(d);
    }

    @Override
    public void doOnError(int errorCode, String errorMsg) {
        dismissLoading();
        if (!isHideToast() && !TextUtils.isEmpty(errorMsg)) {
            ToastUtil.show(errorMsg);
        }
        onError(errorCode, errorMsg);
    }

    @Override
    public void doOnNext(BaseData<T> data) {
        if (data.getCode() == ResponseCode.SUCCESS) {
            onSuccess(data.getData());
        } else {
            ResponseCode dataCode = data.getCode();
            onError(dataCode.code, data.getMessage());
        }
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
