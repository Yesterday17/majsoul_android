package cn.yesterday17.majsoul_android.extension.metadata;

import androidx.annotation.Keep;

@Keep
public enum EnumGameServer {
    CHINA(0),
    JAPAN(1),
    AMERICA(2);

    private int num;

    EnumGameServer(int num) {
        this.num = num;
    }

    public static EnumGameServer from(int num) {
        switch (num) {
            case 1:
                return JAPAN;
            case 2:
                return AMERICA;
            case 0:
            default:
                return CHINA;
        }
    }
}
