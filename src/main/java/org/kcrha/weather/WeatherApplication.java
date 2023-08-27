package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.EnumUtils;
import org.kcrha.weather.models.cli.TaskType;

import java.util.List;

public class WeatherApplication {


    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.getArgList().isEmpty()) {
                presentHelp(options);
            } else {
                run(options, line.getArgList());
            }
        } catch (ParseException exp) {
            System.err.println("Parsing command line options failed.  Reason: " + exp.getMessage());
        }
    }

    private static void presentHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("weather [task]", options);
    }

    private static void run(Options options, List<String> args) {

        if (args.size() != 1) {
            System.out.println("weather requires exactly 1 task argument!");
            presentHelp(options);
            return;
        }

        String attemptedTask = args.get(0);
        if (!EnumUtils.isValidEnum(TaskType.class, attemptedTask.toUpperCase())) {
            System.out.printf("%s is not a valid task!%n", attemptedTask);
            presentHelp(options);
            return;
        }

        TaskType task = TaskType.valueOf(attemptedTask.toUpperCase());
        switch (task) {
            case ALERTS -> new AlertService().run(options);
            case FORECASTS -> new ForecastService().run(options);
            case REFRESH -> new RefreshService().run(options);
            default -> throw new RuntimeException(String.format("Unknown TaskType: %s", task));
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
