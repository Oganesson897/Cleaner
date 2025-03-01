package me.oganesson.cleaner.download.metadata;

import me.oganesson.cleaner.ConfigHolder;
import me.oganesson.cleaner.api.metadata.IMetadata;
import org.apache.http.client.methods.HttpGet;

public class CurseMetadata implements IMetadata {

    public static final String CURSE_API_BASE = "https://api.curseforge.com";

    private final String projectId;
    private final String fileId;

    public CurseMetadata(long curseProjectId, long curseFileId) {
        this.projectId = String.valueOf(curseProjectId);
        this.fileId = String.valueOf(curseFileId);
    }

    public String getProjectId() {return projectId;}
    public String getFileId() {return fileId;}

    @Override
    public String url() {
        return CURSE_API_BASE + "/v1/mods/" + projectId;
    }

    @Override
    public HttpGet download() {
        HttpGet get = new HttpGet(url());
        get.addHeader("Accept", "application/json");
        get.addHeader("x-api-key", ConfigHolder.mod.curseforgeApiKey);
        return get;
    }
}
