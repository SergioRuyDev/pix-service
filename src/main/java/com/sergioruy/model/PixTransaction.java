package com.sergioruy.model;

import com.sergioruy.model.enumeration.StatusPix;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;

public class PixTransaction {

    @Schema(description = "Transaction identifier")
    private String id;

    @Schema(description = "Value of transaction")
    private BigDecimal value;

    @Schema(description = "Type of pix key")
    private String keyType;

    @Schema(description = "Pix Key")
    private String key;

    @Schema(description = "Pix Typableline")
    private String line;

    @Schema(description = "Status of Pix")
    private StatusPix status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public StatusPix getStatus() {
        return status;
    }

    public void setStatus(StatusPix status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PixTransaction that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
