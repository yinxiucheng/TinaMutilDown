package tina.com.live_base.base.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import tina.com.live_base.base.activity.BaseActivity;
import tina.com.live_base.base.mvp.BaseMvpView;
import tina.com.live_base.base.mvp.BasePresenter;
import tina.com.live_base.dialog.CommonProgressDialog;
import tina.com.live_base.utils.BaseUIUtil;

/**
 * @author Ragnar
 * @date 2018/5/14 下午6:00
 */
public abstract class BaseFragment extends Fragment implements BaseMvpView {

    private BasePresenter presenter;

    protected IFragmentShowOrDismiss iFragmentStateCall;

    private CommonProgressDialog progressDialog;

    public CommonProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = initPresenter();
        presenter.onAttach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetach();
    }

    public void finishActivity() {
        if (getActivity() != null) getActivity().finish();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * 将当前fragment弹出FragmentManager栈，回调
     */
    public void popBackStackForCallback() {
        if (getActivity() == null) return;
        try {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            if (iFragmentStateCall != null) {
                iFragmentStateCall.onFragmentStateChange(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现Fragment在show和dismiss的时候能够回调的接口
     */
    public interface IFragmentShowOrDismiss {
        void onFragmentStateChange(boolean show);
    }

    /**
     * 页面返回的处理，在按返回键的时候，根据业务场景需要去实现
     *
     * @return 是否消费返回事件
     */
    public boolean onBack() {
        return false;
    }


    protected boolean isProcessing() {
        return BaseUIUtil.isProcessing();
    }

    public void showProcessingDialog() {
        showProcessingDialog(null);
    }

    public void showProcessingDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) return;
        if (progressDialog == null) progressDialog = new CommonProgressDialog(getContext());
        progressDialog.show();
    }

    public void closeProcessingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    public boolean checkFragmentEnable() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }
}
