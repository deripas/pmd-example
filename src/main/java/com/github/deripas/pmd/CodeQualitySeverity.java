package com.github.deripas.pmd;

import com.google.gson.annotations.SerializedName;

/**
 * Code Quality reports severity.
 * A severity can be info, minor, major, critical, or blocker.
 */
public enum CodeQualitySeverity {

    @SerializedName("info")
    INFO,

    @SerializedName("minor")
    MINOR,

    @SerializedName("major")
    MAJOR,

    @SerializedName("critical")
    CRITICAL,

    @SerializedName("blocker")
    BLOCKER
}
