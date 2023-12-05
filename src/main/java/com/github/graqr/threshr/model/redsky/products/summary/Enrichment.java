package com.github.graqr.threshr.model.redsky.products.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.graqr.threshr.model.redsky.products.plp.search.Images;
import com.github.graqr.threshr.model.redsky.products.plp.search.Video;
import io.micronaut.serde.annotation.Serdeable;

import java.net.URL;

@Serdeable
public record Enrichment(
        @JsonProperty("buy_url")
        URL buyUrl,
        Images images,
        Video[] videos
) {
}
