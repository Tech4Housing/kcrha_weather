package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.kcrha.weather.data_collectors.AirQualityForecastCollector;
import org.kcrha.weather.data_collectors.WeatherForecastCollector;
import org.kcrha.weather.models.DailyAirQualityForecast;
import org.kcrha.weather.models.DailyWeatherForecast;

import java.util.*;

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
                WeatherForecastCollector weatherForecastCollector = new WeatherForecastCollector();
                List<DailyWeatherForecast> weatherForecasts = weatherForecastCollector.retrieveDailyForecasts(1)
                        .stream().sorted(Comparator.comparing(DailyWeatherForecast::getDay)).toList();
                AirQualityForecastCollector airQualityForecastCollector = new AirQualityForecastCollector();
                List<DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(1)
                        .stream().sorted(Comparator.comparing(DailyAirQualityForecast::getDay)).toList();
                for (DailyWeatherForecast dailyWeatherForecast : weatherForecasts) {
                    System.out.printf("Date: %s, Temp Low: %s, Temp High: %s\n", dailyWeatherForecast.getDay(), dailyWeatherForecast.getTemperatureLow(), dailyWeatherForecast.getTemperatureHigh());
                }
                for (DailyAirQualityForecast dailyAirQualityForecast : airQualityForecasts) {
                    System.out.printf("Date: %s, AQI: %s\n", dailyAirQualityForecast.getDay(), dailyAirQualityForecast.getAirQualityIndex());
                }
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
