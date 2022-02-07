package org.group7.model.component;

public enum ComponentEnum {
    GUARD_COMPONENT(""),
    INTRUDER_COMPONENT(""),
    WALL_COMPONENT(""),
    TELEPORTER_COMPONENT(""),
    SHADED_AREA_COMPONENT(""),
    TARGET_AREA_COMPONENT(""),
    INTRUDER_SPAWN_AREA(""),
    GUARD_SPAWN_AREA("");

    private final String texture;

    ComponentEnum(String texture){this.texture = texture;}
    public String getTexture(){return texture;}
}
