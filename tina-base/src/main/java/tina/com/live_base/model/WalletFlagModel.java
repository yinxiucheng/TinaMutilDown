package tina.com.live_base.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import tina.com.live_base.base.model.BaseModel;

/**
 * @author Ragnar
 * @date 2018/5/23 上午11:49
 */
public class WalletFlagModel extends BaseModel implements Parcelable {

    @SerializedName("has_recharge")
    private boolean hasRecharge;
    @SerializedName("has_income")
    private boolean hasIncome;

    public boolean isHasRecharge() {
        return hasRecharge;
    }

    public boolean isHasIncome() {
        return hasIncome;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.hasRecharge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasIncome ? (byte) 1 : (byte) 0);
    }

    public WalletFlagModel() {
    }

    protected WalletFlagModel(Parcel in) {
        this.hasRecharge = in.readByte() != 0;
        this.hasIncome = in.readByte() != 0;
    }

    public static final Creator<WalletFlagModel> CREATOR = new Creator<WalletFlagModel>() {
        @Override
        public WalletFlagModel createFromParcel(Parcel source) {
            return new WalletFlagModel(source);
        }

        @Override
        public WalletFlagModel[] newArray(int size) {
            return new WalletFlagModel[size];
        }
    };
}
