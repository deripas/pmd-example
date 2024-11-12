package com.github.deripas.pmd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pmd.lang.document.FileLocation;
import net.sourceforge.pmd.lang.document.TextFile;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;
import net.sourceforge.pmd.reporting.RuleViolation;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

/**
 * A renderer class for integrating GitLab code quality reports.
 * Extends AbstractIncrementingRenderer to handle the emission of code quality violations.
 */
@Slf4j
public class CodeQualityRenderer extends AbstractIncrementingRenderer {

    private static final String NAME = "PMD";
    private static final String DELIMITER = "|";

    private final Gson gson;
    private final JsonWriter jsonWriter;

    @SneakyThrows
    public CodeQualityRenderer(Writer writer) {
        super("gitlab", "Gitlab code quality integration");
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        jsonWriter = gson.newJsonWriter(writer);
        setWriter(writer);
    }

    @Override
    public String defaultFileExtension() {
        return "json";
    }

    @Override
    public void start() throws IOException {
        jsonWriter.beginArray();
    }

    @Override
    public void end() throws IOException {
        jsonWriter.endArray();
    }

    @Override
    public void startFileAnalysis(TextFile dataSource) {
        log.info("check file {}", dataSource.getFileId().getOriginalPath());
    }

    @SneakyThrows
    @Override
    public void renderFileViolations(Iterator<RuleViolation> violations) {
        while (violations.hasNext()) {
            final RuleViolation rule = violations.next();
            final CodeQualityViolation violation = violation(rule);
            jsonWriter.jsonValue(gson.toJson(violation));
        }
    }

    private static CodeQualityViolation violation(RuleViolation rule) {
        return CodeQualityViolation.builder()
            .check(NAME)
            .message(message(rule))
            .location(location(rule))
            .severity(severity(rule))
            .build();
    }

    private static String message(RuleViolation rule) {
        final String name = rule.getRule().getName();
        return String.join(DELIMITER, NAME, name, rule.getDescription());
    }

    private static CodeQualityViolation.Location location(RuleViolation rule) {
        final FileLocation location = rule.getLocation();
        return new CodeQualityViolation.Location(
            location.getFileId().getOriginalPath(),
            location.getStartLine()
        );
    }

    private static CodeQualitySeverity severity(RuleViolation rule) {
        switch (rule.getRule().getPriority()) {
            case HIGH:
                return CodeQualitySeverity.BLOCKER;
            case MEDIUM_HIGH:
                return CodeQualitySeverity.CRITICAL;
            case MEDIUM:
                return CodeQualitySeverity.MAJOR;
            case MEDIUM_LOW:
                return CodeQualitySeverity.MINOR;
            default:
                return CodeQualitySeverity.INFO;
        }
    }
}
