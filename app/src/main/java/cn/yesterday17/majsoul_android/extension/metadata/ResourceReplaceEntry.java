package cn.yesterday17.majsoul_android.extension.metadata;

import java.util.ArrayList;
import java.util.List;


public class ResourceReplaceEntry {
    private List<String> from;
    private String to;
    private boolean allServers;

    ResourceReplaceEntry(List<String> from, String to, boolean allServers) {
        this.from = from;
        this.to = to;
        this.allServers = allServers;
    }

    ResourceReplaceEntry(String from, String to, boolean allServers) {
        this.from = new ArrayList<>();
        this.from.add(from);
        this.to = to;
        this.allServers = allServers;
    }

    ResourceReplaceEntry(String from) {
        this(from, from, true);
    }

    public List<String> getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isAllServers() {
        return allServers;
    }
}
