package org.kcrha.weather;

import org.apache.commons.cli.Options;

public class RefreshService implements CommandLineService {
    public void run(Options options) {
        System.out.println("Refresh data");
    }
}