package cn.yesterday17.majsoul_android.extension.metadata;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MetadataDeserializer implements JsonDeserializer<Metadata> {

    @Override
    public Metadata deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();

        Metadata meta = new Metadata();

        // TODO: Validation
        if (!json.has("id")) {
            throw new JsonParseException("Missing id!");
        }
        meta.id = json.get("id").getAsString();

        if (!json.has("version")) {
            throw new JsonParseException("Missing version!");
        }
        meta.version = json.get("version").getAsString();

        if (json.has("name")) {
            meta.setName(json.get("name").getAsString());
        }

        if (json.has("author")) {
            JsonElement author = json.get("author");
            if (!author.isJsonArray()) {
                meta.getAuthors().add(author.getAsString());
            } else {
                author.getAsJsonArray().forEach((item) -> meta.getAuthors().add(item.getAsString()));
            }
        }

        if (json.has("description")) {
            meta.setDescription(json.get("description").getAsString());
        }

        if (json.has("preview")) {
            meta.setPreview(json.get("preview").getAsString());
        }

        if (json.has("dependencies")) {
            json.getAsJsonObject("dependencies").entrySet().forEach(
                    (entry) -> meta.getDependencies().put(entry.getKey(), entry.getValue().getAsString()));
        }


        // replace / resource pack entry
        String replaceKey = json.has("replace") ? "replace" : "resourcepack";
        if (json.has(replaceKey)) {
            List<ResourceReplaceEntry> replaceEntryList = meta.getReplace();
            json.getAsJsonArray(replaceKey).forEach((replace) -> {
                if (!replace.isJsonObject()) {
                    replaceEntryList.add(new ResourceReplaceEntry(replace.getAsString()));
                } else {
                    JsonObject rep = replace.getAsJsonObject();
                    if (!rep.get("from").isJsonArray()) {
                        // from is a string
                        replaceEntryList.add(new ResourceReplaceEntry(
                                rep.get("from").getAsString(),
                                rep.get("to").getAsString(),
                                rep.get("all-servers").getAsBoolean()
                        ));
                    } else {
                        List<String> from = new ArrayList<>();
                        rep.get("from").getAsJsonArray().forEach((JsonElement e) -> from.add(e.getAsString()));
                        replaceEntryList.add(new ResourceReplaceEntry(
                                from,
                                rep.get("to").getAsString(),
                                rep.get("all-servers").getAsBoolean()
                        ));
                    }
                }
            });
        }


        // Extension
        if (json.has("entry")) {
            JsonElement entry = json.get("entry");
            if (!entry.isJsonArray()) {
                meta.getScripts().add(entry.getAsString());
            } else {
                entry.getAsJsonArray().forEach((key) -> meta.getScripts().add(key.getAsString()));
            }
        }

        if (json.has("loadBeforeGame")) {
            meta.setLoadBeforeGame(json.get("loadBeforeGame").getAsBoolean());
        }

        if (json.has("applyServer")) {
            json.getAsJsonArray("applyServer").forEach(
                    (server) -> meta.getApplyServers().add(EnumGameServer.from(server.getAsInt())));
        }

        return meta;
    }
}
