package me.oganesson.cleaner.api.metadata;

import org.jetbrains.annotations.Nullable;

public interface IHashed {

    @Nullable String hashType();
    @Nullable String hash();

}
