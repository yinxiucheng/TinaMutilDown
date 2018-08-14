package tina.com.live_base.base.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import tina.com.live_base.base.activity.BaseActivity;
import tina.com.live_base.base.mvp.BaseMvpView;
import tina.com.live_base.base.mvp.BasePresenter;


/**
 * @author Ragnar
 * @date 2018/5/14 下午6:00
 */
public abstract class BaseDialogFragment extends DialogFragment implements BaseMvpView {

    private BasePresenter presenter;

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

    public boolean checkFragmentEnable() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()){
                return false;
            }
        }
        return true;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }

}
