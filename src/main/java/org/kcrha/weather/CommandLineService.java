package org.kcrha.weather;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public interface CommandLineService {
    void run(CommandLine taskCommand);
}
