package me.oganesson.cleaner.api.metadata;

import net.minecraftforge.fml.relauncher.Side;
import org.apache.http.client.methods.HttpGet;
import org.jetbrains.annotations.Nullable;

public interface IMetadata {

    @Nullable Side side();
    @Nullable String name();

    default boolean optional() {
        return false;
    }

    @Nullable IHashed hash();

    String url();
    HttpGet download();

}
