package scripts.api.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.tribot.script.sdk.util.ScriptSettings;
import org.tribot.script.sdk.util.serialization.RuntimeTypeAdapterFactory;
import scripts.api.works.Mining;
import scripts.api.works.MotherlodeMine;
import scripts.api.works.Work;

public class PolymorphicScriptSettings {

    private final ScriptSettings settings;

    public PolymorphicScriptSettings() {
        this.settings = build();
    }

    private static ScriptSettings build() {
        RuntimeTypeAdapterFactory<Work> workAdapterFactory = RuntimeTypeAdapterFactory.of(Work.class)
                .registerSubtype(MotherlodeMine.class)
                .registerSubtype(Mining.class);

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(workAdapterFactory)
                .setPrettyPrinting()
                .create();

        return ScriptSettings.builder()
                .gson(gson)
                .build();
    }

    public ScriptSettings getSettings() {
        return settings;
    }
}
