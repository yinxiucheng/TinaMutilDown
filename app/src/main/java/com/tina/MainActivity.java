package com.tina;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.tbruyelle.rxpermissions2.RxPermissions;
import butterknife.BindView;
import butterknife.ButterKnife;
import tina.com.common.download.DownloadManager;
import tina.com.common.download.utils.ToastUtil;
import tina.com.common.download.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DownloadManager downloadManager;

    @BindView(R.id.muti_download)
    Button mutiDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        permission();
    }

    private void permission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        ToastUtil.show("用户已经同意该权限");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        ToastUtil.show("用户拒绝了该权限");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                        ToastUtil.show("权限被拒绝，请在设置里面开启相应权限，若无相应权限会影响使用");
                    }
                });
    }

    private void init() {
        downloadManager = DownloadManager.getInstance(Utils.getContext());
        mutiDownload.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.muti_download: {
                Intent intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                break;
            }
            default:
        }
    }
}
