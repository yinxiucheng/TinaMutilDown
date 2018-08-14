package tina.com.live_base.account;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import tina.com.live_base.base.model.BaseModel;
import tina.com.live_base.model.FollowerInfoModel;
import tina.com.live_base.model.RoomInfoModel;
import tina.com.live_base.model.WalletFlagModel;

/**
 * @author Ragnar
 * @date 2018/5/23
 * @description 用户信息
 */
@SuppressWarnings("unused")
public class UserModel extends BaseModel implements Parcelable {

    public static final String SEX_MALE = "m";
    public static final String SEX_FEMALE = "f";

    private int id;
    @SerializedName("old_account_uid")
    private String oldAccountUid;
    @SerializedName("screen_name")
    private String screenName;
    @SerializedName("country")
    private int country;
    @SerializedName("province")
    private int province;
    @SerializedName("city")
    private int city;
    @SerializedName("country_name")
    private String countryName;
    @SerializedName("province_name")
    private String provinceName;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("location")
    private String location;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("gender")
    private String gender; // 性别 m男 f女
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("description")
    private String description;
    @SerializedName("created_at")
    private long created_at;
    @SerializedName("has_assoc_phone")
    private boolean hasAssocPhone;
    @SerializedName("wallet_flag")
    private WalletFlagModel walletFlag;
    @SerializedName("idcard_status")
    private int idCardStatus;
    @SerializedName("assoc_phone_cc")
    private String assocPhoneCc;
    @SerializedName("assoc_phone")
    private String assocPhone;
    @SerializedName("assoc_uid")
    private String assocUid;
    @SerializedName("follow_info")
    private FollowerInfoModel followInfo;
    @SerializedName("room_info")
    private RoomInfoModel roomInfo;
    @SerializedName("level")
    private int level;
    @SerializedName("live_status")
    private int liveStatus;
    @SerializedName("following")
    private int following;
    @SerializedName("coins")
    private int coins;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOldAccountUid() {
        return oldAccountUid;
    }

    public void setOldAccountUid(String oldAccountUid) {
        this.oldAccountUid = oldAccountUid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public WalletFlagModel getWalletFlag() {
        return walletFlag;
    }

    public void setWalletFlag(WalletFlagModel walletFlag) {
        this.walletFlag = walletFlag;
    }

    public int getIdCardStatus() {
        return idCardStatus;
    }

    public void setIdCardStatus(int idCardStatus) {
        this.idCardStatus = idCardStatus;
    }

    public String getAssocPhoneCc() {
        return assocPhoneCc;
    }

    public void setAssocPhoneCc(String assocPhoneCc) {
        this.assocPhoneCc = assocPhoneCc;
    }

    public String getAssocPhone() {
        return assocPhone;
    }

    public void setAssocPhone(String assocPhone) {
        this.assocPhone = assocPhone;
    }

    public String getAssocUid() {
        return assocUid;
    }

    public void setAssocUid(String assocUid) {
        this.assocUid = assocUid;
    }

    public FollowerInfoModel getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowerInfoModel followInfo) {
        this.followInfo = followInfo;
    }

    public boolean isHasAssocPhone() {
        return hasAssocPhone;
    }

    public void setHasAssocPhone(boolean hasAssocPhone) {
        this.hasAssocPhone = hasAssocPhone;
    }

    public RoomInfoModel getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfoModel roomInfo) {
        this.roomInfo = roomInfo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", oldAccountUid='" + oldAccountUid + '\'' +
                ", screenName='" + screenName + '\'' +
                ", country=" + country +
                ", province=" + province +
                ", city=" + city +
                ", countryName='" + countryName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", location='" + location + '\'' +
                ", avatar='" + avatar + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", description='" + description + '\'' +
                ", created_at=" + created_at +
                ", hasAssocPhone=" + hasAssocPhone +
                ", walletFlag=" + walletFlag +
                ", idCardStatus=" + idCardStatus +
                ", assocPhoneCc='" + assocPhoneCc + '\'' +
                ", assocPhone='" + assocPhone + '\'' +
                ", assocUid='" + assocUid + '\'' +
                ", roomInfo='" + roomInfo + '\'' +
                ", followInfo=" + followInfo +
                '}';
    }

    public boolean isMale() {
        return TextUtils.equals(gender, SEX_MALE);
    }

    public boolean isFeMale() {
        return TextUtils.equals(gender, SEX_FEMALE);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.oldAccountUid);
        dest.writeString(this.screenName);
        dest.writeInt(this.country);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeString(this.countryName);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeString(this.location);
        dest.writeString(this.avatar);
        dest.writeString(this.gender);
        dest.writeString(this.birthday);
        dest.writeString(this.description);
        dest.writeLong(this.created_at);
        dest.writeByte(this.hasAssocPhone ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.walletFlag, flags);
        dest.writeInt(this.idCardStatus);
        dest.writeString(this.assocPhoneCc);
        dest.writeString(this.assocPhone);
        dest.writeString(this.assocUid);
        dest.writeParcelable(this.followInfo, flags);
        dest.writeParcelable(this.roomInfo, flags);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.id = in.readInt();
        this.oldAccountUid = in.readString();
        this.screenName = in.readString();
        this.country = in.readInt();
        this.province = in.readInt();
        this.city = in.readInt();
        this.countryName = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.location = in.readString();
        this.avatar = in.readString();
        this.gender = in.readString();
        this.birthday = in.readString();
        this.description = in.readString();
        this.created_at = in.readLong();
        this.hasAssocPhone = in.readByte() != 0;
        this.walletFlag = in.readParcelable(WalletFlagModel.class.getClassLoader());
        this.idCardStatus = in.readInt();
        this.assocPhoneCc = in.readString();
        this.assocPhone = in.readString();
        this.assocUid = in.readString();
        this.followInfo = in.readParcelable(FollowerInfoModel.class.getClassLoader());
        this.roomInfo = in.readParcelable(RoomInfoModel.class.getClassLoader());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
