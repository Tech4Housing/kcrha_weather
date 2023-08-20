package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.kcrha.weather.aggregators.AggregateTemperatureAirQualityService;
import org.kcrha.weather.aggregators.TemperatureAirQualityForecast;
import org.kcrha.weather.alerts.ConsoleService;

import java.util.List;

public class WeatherApplication {
    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("weather", options);
            } else {
                AggregateTemperatureAirQualityService forecastAggregateService = new AggregateTemperatureAirQualityService();
                List<TemperatureAirQualityForecast> aggregatedForecasts = forecastAggregateService.getForecasts();

                ConsoleService consoleService = new ConsoleService();
                consoleService.sendAlert(aggregatedForecasts);
            }
        } catch (ParseException exp) {
            System.err.println("Parsing command line options failed.  Reason: " + exp.getMessage());
        }
    }

    private static Options getOptions() {
        Options options = new Options();

        Option help = new Option("h", "help", false, "Print this message");
        Option version = new Option("v", "version", false, "Print the version information and exit");
        Option quiet = new Option("q", "quiet", false, "Suppress warning and error logs");
        Option verbose = new Option("v", "verbose", false, "Print additional logs");
        Option debug = new Option("d", "debug", false, "Print debug logs");

        options.addOption(help);
        options.addOption(version);
        options.addOption(quiet);
        options.addOption(verbose);
        options.addOption(debug);
        return options;
    }
}
