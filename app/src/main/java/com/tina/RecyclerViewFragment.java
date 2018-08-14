package com.tina;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tina.data.DataSource;
import com.tina.listener.OnDeleteAppListener;
import com.tina.listener.OnItemClickListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tina.com.common.download.DownloadManager;
import tina.com.common.download.data.DBHelper;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.download.entity.ThreadInfo;
import tina.com.common.download.observer.DataWatcher;
import tina.com.common.download.utils.DownloadConfig;
import tina.com.common.download.utils.DownloadUtils;
import tina.com.common.download.utils.Trace;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends Fragment implements OnItemClickListener<DownloadInfo>, OnDeleteAppListener<DownloadInfo> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<DownloadInfo> mAppInfos;
    private RecyclerViewAdapter mAdapter;
    private Unbinder unbinder;
    private Executor executor;

    DataWatcher dataWatcher = new DataWatcher() {
        @Override
        public void notifyObserver(DownloadInfo data) {
            int index = mAppInfos.indexOf(data);
            if (index != -1) {
                mAppInfos.remove(data);
                mAppInfos.add(index, data);
                mAdapter.notifyItemChanged(index, "payload");
            }
            Trace.e(data.toString());
        }
    };

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, view);
        executor = Executors.newCachedThreadPool();
        DownloadManager.getInstance(getContext()).addObservable(getContext(), dataWatcher);
        return view;
    }

    public void initRecycler() {
        //tofo 这一段应该用线程切换
        mAppInfos = DBHelper.getInstance().queryDownloadInfoAll();
        if (mAppInfos == null || mAppInfos.isEmpty()) {
            mAppInfos = DataSource.getInstance().getData();
            DBHelper.getInstance().insertDownloadInfoTX(mAppInfos);
        } else {
            for (DownloadInfo downloadInfo : mAppInfos) {
                if (downloadInfo.status == DownloadStatus.DOWNLOADING
                        || downloadInfo.status == DownloadStatus.WAITING) {
                    if (downloadInfo.getAcceptRanges()) {
                        downloadInfo.status = DownloadStatus.PAUSED;
                        executor.execute(() -> {
                            changeThreadInfoStatus(downloadInfo);
                        });
                    } else {
                        downloadInfo.status = DownloadStatus.IDLE;
                        downloadInfo.reset();
                    }
                }
                executor.execute(() -> DBHelper.getInstance().newOrUpdate(downloadInfo));
            }
        }

        mAdapter = new RecyclerViewAdapter(mAppInfos);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnDeleteAppListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setData(mAppInfos);
    }

    private void changeThreadInfoStatus(DownloadInfo downloadInfo){
        List<ThreadInfo> list = DBHelper.getInstance().queryThreadInfoListByTag(downloadInfo.getTag());
        for (ThreadInfo threadInfo: list) {
            threadInfo.setStatus(DownloadStatus.PAUSED);
            DBHelper.getInstance().newOrUpdateThreadInfo(threadInfo);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Dir: /Download
     */
    private final File mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");

    @Override
    public void onItemClick(View v, int position, DownloadInfo appInfo) {
        Trace.e("appInfo's Status:" + appInfo.getStatus());
        if (appInfo.getStatus() == DownloadStatus.DOWNLOADING) {
            pause(appInfo);
        } else if (appInfo.getStatus() == DownloadStatus.COMPLETED) {
            install(appInfo);
        } else if (appInfo.getStatus() == DownloadStatus.INSTALLED) {
            unInstall(appInfo);
        } else if (appInfo.getStatus() == DownloadStatus.IDLE
                || appInfo.getStatus() == DownloadStatus.PAUSED
                || appInfo.getStatus() == DownloadStatus.CANCELED
                || appInfo.getStatus() == DownloadStatus.FAILED
                || appInfo.getStatus() == DownloadStatus.WAITING) {
            download(appInfo);
        } else {
            download(appInfo);
            Trace.e("error status");
        }
    }

    private void download(DownloadInfo appInfo) {
        DownloadManager.getInstance(getContext()).download(appInfo);
    }

    private void pause(DownloadInfo appInfo) {
        DownloadManager.getInstance(getContext()).pause(appInfo);
    }

    private void install(DownloadInfo appInfo) {
        DownloadUtils.installApp(getActivity(), new File(mDownloadDir, appInfo.getFileName() + ".apk"));
    }

    private void deleteAppInfo(Context ctx, final DownloadInfo appInfo) {
        AlertDialog dialog = new AlertDialog.Builder(ctx).setTitle("确认删除" + appInfo.getName() + "?")
                .setCancelable(false)
                .setMessage("确认删除" + appInfo.getName() + "?")
                .setPositiveButton("删除", (dialog1, which) -> {
                    DownloadManager.getInstance(getContext()).cancel(appInfo);
//                        Utils.deleteAppInfo(new File(mDownloadDir, appInfo.getName() + ".apk"));
                })
                .setNegativeButton("取消", null).create();
        dialog.show();
    }

    private void unInstall(DownloadInfo appInfo) {
//        Utils.unInstallApp(getActivity(), appInfo.getPackageName());
    }

    @Override
    public void onDelete(DownloadInfo appInfo) {
        deleteAppInfo(getActivity(), appInfo);
    }

    private RecyclerViewAdapter.AppViewHolder getViewHolder(int position) {
        return (RecyclerViewAdapter.AppViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DownloadManager.getInstance(getContext()).removeObservable(getContext(), dataWatcher);
        unbinder.unbind();
    }

    public void pauseAll() {
        DownloadManager.getInstance(getContext()).pauseAll();
    }

    public void recoverAll() {
        DownloadManager.getInstance(getContext()).recoverAll();
    }


    public void cancelAll() {
        DownloadManager.getInstance(getContext()).cancelAll();
    }


}
