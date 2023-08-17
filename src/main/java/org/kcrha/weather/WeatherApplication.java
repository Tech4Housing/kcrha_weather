package org.kcrha.weather;

import org.apache.commons.cli.*;
import org.kcrha.weather.data_collectors.WeatherForecastCollector;
import org.kcrha.weather.records.DailyWeatherForecast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
				List<DailyWeatherForecast> dailyWeatherForecastList = weatherForecastCollector.retrieveDailyForecasts(1)
						.stream().sorted((forecast1, forecast2) -> {
					try {
						return forecast1.getDate().compareTo(forecast2.getDate());
					} catch (java.text.ParseException e) {
						throw new RuntimeException(e);
					}
				}).toList();
				for (DailyWeatherForecast dailyWeatherForecast : dailyWeatherForecastList) {
					System.out.printf("Date: %s, Temp Low: %s, Temp High: %s\n", dailyWeatherForecast.day, dailyWeatherForecast.temperatureLow, dailyWeatherForecast.temperatureHigh);
				}
			}
		}
		catch (ParseException exp) {
			System.err.println("Parsing command line options failed.  Reason: " + exp.getMessage());
		}
	}

	private static Options getOptions() {
		Options options = new Options();

		Option help = new Option("h", "help", false, "Print this message");
		Option version = new Option("v", "version", false, "Print the version information and exit");
		Option quiet = new Option("q", "quiet", false, "Suppress warning and error logs");
		Option verbose = new Option("v", "verbose", false, "Print additional logs");
		Option debug = new Option("d", "debug", false,"Print debug logs");

		options.addOption(help);
		options.addOption(version);
		options.addOption(quiet);
		options.addOption(verbose);
		options.addOption(debug);
		return options;
	}
}
