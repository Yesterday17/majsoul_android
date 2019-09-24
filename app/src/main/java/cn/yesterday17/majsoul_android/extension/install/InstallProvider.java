package cn.yesterday17.majsoul_android.extension.install;

public interface InstallProvider {
    void getMetadata();

    void install(String path);
}
