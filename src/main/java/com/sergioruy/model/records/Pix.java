package com.sergioruy.model.records;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public record Pix(
        @Schema(description = "Registered key of receiver.") String key,
        @Schema(description = "Transaction value.") BigDecimal value,
        @Schema(description = "Origin city of receiver") String originCity) {

}
