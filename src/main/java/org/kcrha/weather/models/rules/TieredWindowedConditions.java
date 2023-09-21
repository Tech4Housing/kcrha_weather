package org.kcrha.weather.models.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kcrha.weather.models.forecast.AggregateForecast;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class TieredWindowedConditions {
    Map<Integer, WindowedConditions> windowedConditions;

    public Map<LocalDate, Integer> getActivatedTiersForDates(List<? extends AggregateForecast> forecasts) {
        Map<LocalDate, Integer> activatedTiersForDate = forecasts.stream().collect(Collectors.toMap(AggregateForecast::getDate, e -> 0));

        for (Map.Entry<Integer, WindowedConditions> entry : windowedConditions.entrySet()) {
            Integer tier = entry.getKey();

            entry.getValue()
                    .getMatchingDatesForForecasts(forecasts)
                    .forEach(
                            date -> activatedTiersForDate.put(date, tier > activatedTiersForDate.get(date) ? tier : activatedTiersForDate.get(date))
                    );
        }

        return activatedTiersForDate;
    }
}
