package tina.com.live_base.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import tina.com.live_base.R;

/**
 * @date 2018/6/20
 * @description 公用信息弹框
 */
public class CommonProgressDialog extends AppCompatDialog {

    public CommonProgressDialog(Context context) {
        super(context);
        setViewDialogParams();
        setContentView(R.layout.voice_dialog_pregress_common);
        setCanceledOnTouchOutside(false);
    }

    public void setViewDialogParams() {
        Window window = getWindow();
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
