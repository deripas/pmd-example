package com.github.deripas.pmd;

import lombok.SneakyThrows;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import net.sourceforge.pmd.lang.document.FileCollector;

import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class PmdChecker {

    private final PMDConfiguration configuration;
    private final String reportPath;

    public PmdChecker(
        String configPath,
        String reportPath
    ) {
        this.configuration = new PMDConfiguration();
        this.configuration.addRuleSet(configPath);
        this.configuration.setThreads(1);
        this.reportPath = reportPath;
    }

    @SneakyThrows
    public void check(Collection<String> files) {
        final List<Path> paths = files.stream()
            .map(Path::of)
            .collect(Collectors.toList());
        try (final PmdAnalysis pmd = PmdAnalysis.create(configuration);
             final Writer writer = new PrintWriter(reportPath)
        ) {
            final FileCollector fileCollector = pmd.files();
            paths.forEach(fileCollector::addFile);

            pmd.addRenderer(new CodeQualityRenderer(writer));
            pmd.performAnalysis();
        }
    }
}
