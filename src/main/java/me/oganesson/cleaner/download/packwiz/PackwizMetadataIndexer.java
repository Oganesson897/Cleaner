package me.oganesson.cleaner.download.packwiz;

import com.moandjiezana.toml.Toml;
import me.oganesson.cleaner.api.metadata.IMetadata;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class PackwizMetadataIndexer {

    private static final Toml TOML = new Toml();

    public static Set<IMetadata> indexMetadata(File indexDir) {
        Set<IMetadata> metadata = new HashSet<>();

        var fileCandidates = indexDir.listFiles((dir, name) -> name.endsWith(".pw.toml"));
        if (fileCandidates != null) {
            for (File file : fileCandidates) {
                var content = TOML.read(file);
                var isCurse = content.getTable("download").getString("mode").contains("curseforge");
                if (isCurse) {
                    var curse = content.getTable("update.curseforge");
                    //metadata.add(new CurseMetadata(curse.getLong("project-id"), curse.getLong("file-id")));
                } else {
                    var download = content.getTable("download");
                    //metadata.add(new URLMetadata(download.getString("url")));
                }
            }
        }

        return metadata;
    }

}
