package cn.yesterday17.majsoul_android.extension.metadata;

import java.util.List;

public interface IResourcePackMetadata extends IMetadata {
    boolean isResourcePack();

    List<ResourceReplaceEntry> getReplace();

    default void handleResourcePackDefaults() {
    }
}
