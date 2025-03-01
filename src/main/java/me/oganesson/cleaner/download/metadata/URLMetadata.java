package me.oganesson.cleaner.download.metadata;

import me.oganesson.cleaner.api.metadata.IMetadata;
import org.apache.http.client.methods.HttpGet;

public class URLMetadata implements IMetadata {

    private final String url;

    public URLMetadata(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public HttpGet download() {
        return new HttpGet(url());
    }
}
