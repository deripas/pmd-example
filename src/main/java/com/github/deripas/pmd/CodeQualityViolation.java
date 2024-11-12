package com.github.deripas.pmd;

import com.google.common.hash.Hashing;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * Code Quality reports.
 * <a href="https://docs.gitlab.com/ee/ci/testing/code_quality.html#implement-a-custom-tool">Implement a custom tool</a>
 *
 * @see CodeQualityViolationBuilder
 */
@Data
@Builder(toBuilder = true)
public final class CodeQualityViolation {

    /**
     * A unique name representing the static analysis check that emitted this issue.
     */
    @SerializedName("check_name")
    private String check;

    /**
     * A description of the code quality violation.
     */
    @SerializedName("description")
    private String message;

    /**
     * A severity string (can be info, minor, major, critical, or blocker).
     */
    @SerializedName("severity")
    private CodeQualitySeverity severity;

    /**
     * A unique fingerprint to identify the code quality violation. For example, an MD5 hash.
     */
    @SerializedName("fingerprint")
    private String fingerprint;

    /**
     * Code quality violation location.
     */
    @SerializedName("location")
    private Location location;


    /**
     * Represents the location of a code quality violation.
     */
    @Data
    @NoArgsConstructor
    public static class Location {

        /**
         * The relative path to the file containing the code quality violation.
         */
        @SerializedName("path")
        private String path;

        /**
         * The line on which the code quality violation occurred.
         */
        @SerializedName("lines")
        private Lines lines;

        /**
         * Constructs a new Location instance representing the location of a code quality violation.
         *
         * @param path the relative path to the file containing the code quality violation
         * @param line the line number where the code quality violation occurred
         */
        public Location(
            String path,
            int line
        ) {
            this.path = path;
            this.lines = new Lines(line);
        }

        /**
         * Retrieves the line number where the code quality violation occurred.
         *
         * @return the beginning line number of the code quality violation
         */
        public int getLine() {
            return this.lines.getBegin();
        }
    }

    /**
     * Represents the lines related to a code quality violation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Lines {

        /**
         * The line on which the code quality violation occurred.
         */
        @SerializedName("begin")
        private int begin;
    }


    /**
     * Constructs a new CodeQualityViolation instance.
     */
    public static class CodeQualityViolationBuilder {

        /**
         * Builds a new CodeQualityViolation instance.
         *
         * @return the new CodeQualityViolation instance
         */
        public CodeQualityViolation build() {
            return new CodeQualityViolation(
                this.check,
                Objects.requireNonNull(this.message),
                Optional.ofNullable(this.severity).orElse(CodeQualitySeverity.MAJOR),
                Optional.ofNullable(this.fingerprint).orElseGet(() -> generateFingerprint(this.message, this.location)),
                Objects.requireNonNull(this.location)
            );
        }

        /**
         * Generates a fingerprint by creating an MD5 hash of the
         * message, location path, and location line number of the violation.
         *
         * @param message  the message of the violation
         * @param location the location of the violation
         * @return the generated fingerprint as a String
         */
        @SuppressWarnings({"UnstableApiUsage", "deprecation"})
        @SneakyThrows
        private static String generateFingerprint(
            String message,
            Location location
        ) {
            return Hashing
                .md5()
                .newHasher()
                .putString(message, StandardCharsets.UTF_8)
                .putString(location.getPath(), StandardCharsets.UTF_8)
                .putInt(location.getLine())
                .hash()
                .toString();
        }
    }
}
