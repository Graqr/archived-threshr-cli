package com.github.graqr.threshr.model.redsky.products.plp.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record VideoFile(
        @JsonProperty("mime_type") String mimeType,
        @JsonProperty("video_height_pixels") String videoHeightPixels,
        @JsonProperty("video_url") String videoUrl) {
}
