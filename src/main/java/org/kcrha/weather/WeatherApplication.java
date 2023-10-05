package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.EnumUtils;
import org.kcrha.weather.models.cli.TaskType;
import org.kcrha.weather.notifications.ConsoleNotification;

public class WeatherApplication {

    final static Options OPTIONS = getOptions();


    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(OPTIONS, args);
            if (line.getArgList().isEmpty()) {
                presentHelp();
            } else {
                run(line, args);
            }
        } catch (ParseException exp) {
            System.err.println("Parsing command line options failed.  Reason: " + exp.getMessage());
        }
    }

    private static void run(CommandLine line, String[] args) {

        if (line.getArgList().size() != 1) {
            System.out.println("weather requires exactly 1 task argument!");
            presentHelp();
            return;
        }

        String attemptedTask = line.getArgList().get(0);
        if (!EnumUtils.isValidEnum(TaskType.class, attemptedTask.toUpperCase())) {
            System.out.printf("%s is not a valid task!%n", attemptedTask);
            presentHelp();
            return;
        }

        TaskType task = TaskType.valueOf(attemptedTask.toUpperCase());

        OPTIONS.addOptionGroup(task.getOptionGroup());

        if (line.hasOption("help")) {
            presentHelp(task);
            return;
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine taskCommand = null;
        try {
            taskCommand = parser.parse(OPTIONS, args);
        } catch (ParseException exp) {
            System.err.println("Parsing command line options failed.  Reason: " + exp.getMessage());
        }

        switch (task) {
            case ALERTS -> {
                ConsoleNotification notification = new ConsoleNotification();
                new AlertService(notification).run(taskCommand);
            }
            case FORECASTS -> {
                ConsoleNotification notification = new ConsoleNotification();
                new ForecastService(notification).run(taskCommand);
            }
            default -> throw new RuntimeException(String.format("Unknown TaskType: %s", task));
        }
    }

    private static void presentHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("weather [task]", OPTIONS);
    }

    private static void presentHelp(TaskType task) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        formatter.printHelp(String.format("weather %s", task.getTaskName()), OPTIONS);
    }

    private static Options getOptions() {
        OptionGroup sharedOptionGroup = new OptionGroup();

        Option help = Option.builder("h").longOpt("help").desc("Print this message").build();
        Option quiet = Option.builder("q").longOpt("quiet").desc("Suppress warning and error logs").build();
        Option verbose = Option.builder("v").longOpt("verbose").desc("Print additional logs").build();
        Option debug = Option.builder("d").longOpt("debug").desc("Print debug logs").build();
        Option version = Option.builder().longOpt("version").desc("Print the version information and exit").build();

        sharedOptionGroup.addOption(help);
        sharedOptionGroup.addOption(quiet);
        sharedOptionGroup.addOption(verbose);
        sharedOptionGroup.addOption(debug);
        sharedOptionGroup.addOption(version);

        Options options = new Options();
        options.addOptionGroup(sharedOptionGroup);

        return options;
    }
}
