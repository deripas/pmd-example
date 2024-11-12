package com.github.deripas.pmd;

import com.google.gson.Gson;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

class PmdSmokeTest implements TestWithResource {

    private static final String REPORT_JSON = "report.json";
    private static final String BAD_CLASS_JAVA = "BadClass.java";

    @TempDir
    private static File temp;

    private final File report = new File(temp, REPORT_JSON);

    @Test
    @Order(1)
    void testRun() {
        final PmdChecker checker = new PmdChecker(
            path("pmd-example.xml"),
            report.getAbsolutePath()
        );
        checker.check(List.of(
            path(BAD_CLASS_JAVA)
        ));
        Assertions.assertTrue(report.exists());
    }

    @Test
    @Order(2)
    void testReport() throws Exception {
        final String json = Files.readString(report.toPath());
        final CodeQualityViolation[] violations = new Gson()
            .fromJson(json, CodeQualityViolation[].class);

        Assertions.assertNotNull(violations);
        assertThat(violations, has("UnnecessaryImport", 3));
        assertThat(violations, has("MethodNamingConventions", 7));
        assertThat(violations, has("UnusedLocalVariable", 8));
        assertThat(violations, has("UnusedPrivateMethod", 15));
    }

    private static Matcher<CodeQualityViolation[]> has(
        String check,
        int line
    ) {
        return hasItemInArray(
            allOf(
                hasProperty("check", is("PMD")),
                hasProperty("message", containsString(check)),
                hasProperty("location", allOf(
                    hasProperty("path", endsWith(BAD_CLASS_JAVA)),
                    hasProperty("lines", hasProperty("begin", is(line)))
                ))
            )
        );
    }
}
