package com.projet.demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    AGENT_READ("agent:read"),
    AGENT_UPDATE("agent:update"),
    AGENT_CREATE("agent:create"),
    AGENT_DELETE("agent:delete"),
    CLIENT_READ("client:read"),
    CLIENT_UPDATE("client:update"),
    CLIENT_DELETE("client:delete"),
    CLIENT_CREATE("client:create")


    ;

    @Getter
    private final String permission;
}
