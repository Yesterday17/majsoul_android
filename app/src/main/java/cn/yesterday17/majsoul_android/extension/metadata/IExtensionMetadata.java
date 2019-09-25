package cn.yesterday17.majsoul_android.extension.metadata;

import java.util.List;
import java.util.Set;

public interface IExtensionMetadata extends IMetadata {
    boolean isExtension();

    List<String> getScripts();

    boolean getLoadBeforeGame();

    void setLoadBeforeGame(boolean loadBeforeGame);

    Set<EnumGameServer> getApplyServers();

    List<ResourceReplaceEntry> getResourcePack();

    default void handleExtensionDefaults() {
        if (this.getScripts().isEmpty()) {
            this.getScripts().add("script.js");
        }

        if (this.getApplyServers().isEmpty()) {
            this.getApplyServers().add(EnumGameServer.CHINA);
        }
    }
}
