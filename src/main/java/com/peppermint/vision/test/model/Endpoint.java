package com.peppermint.vision.test.model;

import java.util.Map;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endpoint {
    private String name;
    private String alias;
    private String host;
    private String path;
    private String user;
    private String password;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String category;
    private String comment;
    private boolean active;

    public String getUrl() {
        return host + Optional.ofNullable(path).orElse("");
    }
}
