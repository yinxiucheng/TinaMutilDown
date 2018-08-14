package com.tina;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tina.listener.OnDeleteAppListener;
import com.tina.listener.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tina.com.common.download.entity.DownloadInfo;
import tina.com.common.download.entity.DownloadStatus;
import tina.com.common.image.GlideImageLoader;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DownloadInfo> mAppInfos;
    private OnItemClickListener mListener;
    private OnDeleteAppListener mDeleteListener;


    public RecyclerViewAdapter(List<DownloadInfo> appInfos) {
        this.mAppInfos = appInfos;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnDeleteAppListener(OnDeleteAppListener listener) {
        this.mDeleteListener = listener;
    }

    public void setData(List<DownloadInfo> appInfos) {
        this.mAppInfos = appInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_download, parent, false);
        final AppViewHolder holder = new AppViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(v.getContext(), AppDetailActivity.class);
//                intent.putExtra("EXTRA_APPINFO", mAppInfos.get(holder.getLayoutPosition()));
//                v.getContext().startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData((AppViewHolder) holder, position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            bindData((AppViewHolder) viewHolder, position);
        } else {
            AppViewHolder holder = (AppViewHolder) viewHolder;
            final DownloadInfo appInfo = mAppInfos.get(position);
            holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
            holder.txtApkType.setText(DownloadStatus.getStausText(appInfo.getStatus()));
            holder.progressBar.setProgress(appInfo.getProgress());
            holder.btnDownload.setText(DownloadStatus.getNextStausText(appInfo.getStatus()));
            holder.btnDownload.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(v, position, appInfo);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAppInfos.size();
    }

    private void bindData(final AppViewHolder holder, final int position) {
        final DownloadInfo appInfo = mAppInfos.get(position);
        holder.tvName.setText(appInfo.getName());
        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
        holder.txtApkType.setText(DownloadStatus.getStausText(appInfo.getStatus()));
        holder.progressBar.setProgress(appInfo.getProgress());
        holder.btnDownload.setText(DownloadStatus.getNextStausText(appInfo.getStatus()));

        GlideImageLoader.loadRoundCenterCropImage(holder.itemView.getContext(), holder.ivIcon, appInfo.getImage(), 5);

        holder.btnDownload.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(v, position, appInfo);
            }
        });

        holder.imgMore.setOnClickListener(v -> {
            if (holder.txtDelete.getVisibility() != View.VISIBLE) {
                holder.txtDelete.setVisibility(View.VISIBLE);
            } else {
                holder.txtDelete.setVisibility(View.GONE);
            }
        });

        holder.txtDelete.setOnClickListener(v -> {
            Log.d("RecyclerViewAdapter", "delete app");
            mDeleteListener.onDelete(appInfo);
        });
    }

    public static final class AppViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_app)
        public ImageView ivIcon;
        @BindView(R.id.txt_apk_name)
        public TextView tvName;
        @BindView(R.id.txt_apk_type)
        public TextView txtApkType;
        @BindView(R.id.tvDownloadPerSize)
        TextView tvDownloadPerSize;
        @BindView(R.id.txt_apk_desc)
        public TextView txtApkDesc;
        @BindView(R.id.txt_install)
        public TextView btnDownload;
        @BindView(R.id.txt_delete)
        public TextView txtDelete;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.img_more)
        ImageView imgMore;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.itemView)
        public LinearLayout itemView;
        @BindView(R.id.card)
        CardView card;

        public AppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
