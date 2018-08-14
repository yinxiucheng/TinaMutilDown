package tina.com.live_base.model;


import android.os.Parcel;
import android.os.Parcelable;

import tina.com.live_base.base.model.BaseModel;

public class FollowerInfoModel extends BaseModel implements Parcelable {

    private String id;
    private String userId;
    private int followTotal;
    private int fansTotal;
    private String createdAt;
    private String updatedAt;

    public int getFollowTotal() {
        return followTotal;
    }

    public int getFansTotal() {
        return fansTotal;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeInt(this.followTotal);
        dest.writeInt(this.fansTotal);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public FollowerInfoModel() {
    }

    protected FollowerInfoModel(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.followTotal = in.readInt();
        this.fansTotal = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<FollowerInfoModel> CREATOR = new Creator<FollowerInfoModel>() {
        @Override
        public FollowerInfoModel createFromParcel(Parcel source) {
            return new FollowerInfoModel(source);
        }

        @Override
        public FollowerInfoModel[] newArray(int size) {
            return new FollowerInfoModel[size];
        }
    };
}
