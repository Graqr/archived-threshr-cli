package com.github.graqr.threshr.model.redsky.products.pdp.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Price(
        @JsonProperty("formatted_current_price") String formattedCurrentPrice,
        @JsonProperty("formatted_current_price_type") String formattedCurrentPriceType,
        @JsonProperty("reg_retail") Double regRetail,
        @JsonProperty("formatted_unit_price") String formattedUnitPrice,
        @JsonProperty("formatted_unit_price_suffix") String formattedUnitPriceSuffix,
        @JsonProperty("location_id") Long locationId, @JsonProperty("current_retail") Double currentRetail,
        @JsonProperty("external_system_id") String externalSystemId,
        @JsonProperty("is_current_price_range") Boolean isCurrentPriceRange) {
}
