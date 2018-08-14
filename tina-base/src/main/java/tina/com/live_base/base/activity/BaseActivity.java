package tina.com.live_base.base.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import tina.com.live_base.base.fragment.BaseFragment;
import tina.com.live_base.utils.DeviceUtil;
import tina.com.live_base.utils.BaseUtils;

/**
 * @author yxc
 * @date 2018/7/16
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ViewManager.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ViewManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * @date 2018/6/1
     * @description 设置浸透状态栏
     */
    protected void initTranslucentStatusBar(boolean isLightBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLightBar)
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                            .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                else
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                View view = new View(this);
                view.setLayoutParams(new ViewGroup.LayoutParams(DeviceUtil.getScreenWidth(), DeviceUtil.getStatusBarHeight()));
                view.setBackgroundColor(Color.BLACK);
                ((ViewGroup) getWindow().getDecorView()).addView(view);
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        // 隐藏ActionBar
        if (getSupportActionBar() != null) getSupportActionBar().hide();
    }



    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

}
