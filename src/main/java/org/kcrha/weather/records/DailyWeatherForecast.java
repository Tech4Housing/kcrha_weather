package org.kcrha.weather.records;


import lombok.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyWeatherForecast {
    public String day;
    public Integer temperatureHigh;
    public Integer temperatureLow;

    public Date getDate() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(day);
    }
}
