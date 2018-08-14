package tina.com.live_base.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import tina.com.live_base.base.model.BaseModel;

/**
 * Created by xiuchengyin on 2018/5/28.
 */

public class RoomInfoModel extends BaseModel implements Parcelable {
    @SerializedName("id")
    public String id;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("room_name")
    public String roomName;
    @SerializedName("room_id")
    public String roomId;
    @SerializedName("cover_pic")
    private String roomCover;
    @SerializedName("audience")
    private int audienceCount;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomCover() {
        return roomCover;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.roomName);
        dest.writeString(this.roomId);
        dest.writeString(this.roomCover);
        dest.writeInt(this.audienceCount);
    }

    protected RoomInfoModel(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.roomName = in.readString();
        this.roomId = in.readString();
        this.roomCover = in.readString();
        this.audienceCount = in.readInt();
    }

    public static final Creator<RoomInfoModel> CREATOR = new Creator<RoomInfoModel>() {
        @Override
        public RoomInfoModel createFromParcel(Parcel source) {
            return new RoomInfoModel(source);
        }

        @Override
        public RoomInfoModel[] newArray(int size) {
            return new RoomInfoModel[size];
        }
    };
}
