package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.kcrha.weather.data_collectors.AirQualityForecastCollector;
import org.kcrha.weather.data_collectors.WeatherForecastCollector;
import org.kcrha.weather.models.DailyAggregatedForecast;
import org.kcrha.weather.models.DailyAirQualityForecast;
import org.kcrha.weather.models.DailyWeatherForecast;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
                Map<LocalDate, DailyWeatherForecast> weatherForecasts = weatherForecastCollector.retrieveDailyForecasts(1)
                        .stream().collect(Collectors.toMap(DailyWeatherForecast::getDay, dailyWeatherForecast -> dailyWeatherForecast));
                AirQualityForecastCollector airQualityForecastCollector = new AirQualityForecastCollector();
                Map<LocalDate, DailyAirQualityForecast> airQualityForecasts = airQualityForecastCollector.retrieveDailyForecasts(1)
                        .stream().collect(Collectors.toMap(DailyAirQualityForecast::getDay, dailyWeatherForecast -> dailyWeatherForecast));

                List<DailyAggregatedForecast> aggregatedForecasts = new ArrayList<>();
                for (Map.Entry<LocalDate, DailyWeatherForecast> entry : weatherForecasts.entrySet()) {
                    DailyWeatherForecast dailyWeatherForecast = entry.getValue();
                    Integer aqi = airQualityForecasts.getOrDefault(entry.getKey(), DailyAirQualityForecast.builder().build()).getAirQualityIndex();
                    aggregatedForecasts.add(DailyAggregatedForecast.builder()
                                    .day(dailyWeatherForecast.getDay())
                                    .airQualityIndex(aqi)
                                    .temperatureLow(dailyWeatherForecast.getTemperatureLow())
                                    .temperatureHigh(dailyWeatherForecast.getTemperatureHigh())
                            .build());
                }

                aggregatedForecasts.stream().sorted(Comparator.comparing(DailyAggregatedForecast::getDay)).forEach(dailyAggregatedForecast -> {
                    System.out.printf("Date: %s, Temp Low: %s, Temp High: %s, AQI: %s\n", dailyAggregatedForecast.getDay(), dailyAggregatedForecast.getTemperatureLow(), dailyAggregatedForecast.getTemperatureHigh(), dailyAggregatedForecast.getAirQualityIndex());
                });
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
