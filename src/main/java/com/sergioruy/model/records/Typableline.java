package com.sergioruy.model.records;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record Typableline(
        @Schema(description = "writable line") String line,
        @Schema(description = "UUID generated") String uuid) {
}
