package cn.yesterday17.majsoul_android.extension.metadata;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Keep
public class Metadata implements IMetadata, IResourcePackMetadata, IExtensionMetadata {
    String id;
    String version;

    private String name = "";
    private String description = "";
    private List<String> author = new ArrayList<>();
    private String preview = "";  // not implementing this
    private Map<String, String> dependencies = new HashMap<>();

    private Set<EnumGameServer> applyServers = new HashSet<>();
    private boolean loadBeforeGame = false;
    private List<String> scripts = new ArrayList<>();

    private List<ResourceReplaceEntry> resourceReplaceEntries = new ArrayList<>();

    // Metadata
    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<String> getAuthors() {
        return this.author;
    }

    @Override
    public String getPreview() {
        return this.preview;
    }

    @Override
    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public Map<String, String> getDependencies() {
        return this.dependencies;
    }

    // Extension Part
    @Override
    public boolean isExtension() {
        return !this.isResourcePack();
    }

    @Override
    public List<String> getScripts() {
        return this.scripts;
    }

    @Override
    public boolean getLoadBeforeGame() {
        return this.loadBeforeGame;
    }

    @Override
    public void setLoadBeforeGame(boolean loadBeforeGame) {
        this.loadBeforeGame = loadBeforeGame;
    }

    @Override
    public Set<EnumGameServer> getApplyServers() {
        return this.applyServers;
    }

    @Override
    public List<ResourceReplaceEntry> getResourcePack() {
        return this.resourceReplaceEntries;
    }

    @Override
    public void handleDefaults() {
        this.handleMetadataDefaults();

        if (this.isResourcePack()) {
            handleResourcePackDefaults();
        } else {
            handleExtensionDefaults();
        }
    }

    // ResourcePack Part
    @Override
    public boolean isResourcePack() {
        return false;
    }

    @Override
    public List<ResourceReplaceEntry> getReplace() {
        return this.resourceReplaceEntries;
    }
}
