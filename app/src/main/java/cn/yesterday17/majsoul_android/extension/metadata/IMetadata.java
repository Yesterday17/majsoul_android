package cn.yesterday17.majsoul_android.extension.metadata;

import java.util.List;
import java.util.Map;

public interface IMetadata {
    default void handleMetadataDefaults() {
        if (this.getName().equals("")) {
            this.setName("未命名");
        }

        if (this.getAuthors().isEmpty()) {
            this.getAuthors().add("未知作者");
        }

        if (this.getDescription().equals("")) {
            this.setDescription("暂无简介");
        }

        if (this.getPreview().equals("")) {
            this.setPreview("preview.png");
        }
    }

    String getID();

    String getVersion();

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    List<String> getAuthors();

    @Deprecated
    String getPreview();

    @Deprecated
    void setPreview(String preview);

    Map<String, String> getDependencies();

    void handleDefaults();
}
