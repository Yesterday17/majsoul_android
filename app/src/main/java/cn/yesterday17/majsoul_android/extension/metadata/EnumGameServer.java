package cn.yesterday17.majsoul_android.extension.metadata;

enum EnumGameServer {
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
