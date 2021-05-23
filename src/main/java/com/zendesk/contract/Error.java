package com.zendesk.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Error {

    @JsonProperty("code")
    @NotNull
    private String code;
    @JsonProperty("message")
    @NotNull
    private String message;

    public Error() {}

    public Error(@NotNull String code, @NotNull String message) {
        this.code = code;
        this.message = message;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
