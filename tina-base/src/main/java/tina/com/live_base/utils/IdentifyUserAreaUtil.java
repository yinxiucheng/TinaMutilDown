package tina.com.live_base.utils;


/**
 * Created by jsg on 2015/3/30.
 */
public class IdentifyUserAreaUtil {


    public static enum AreaType {
        MAINLAND(1), HK_TW_MACAO(2) {
        };
        private int value;

        private AreaType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
