package org.kcrha.weather.models.alert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kcrha.weather.models.forecast.metrics.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegionForecastTest {

    RegionForecast regionForecast;

    @BeforeEach
    void setup() {
        regionForecast = new RegionForecast();
    }

    @Test
    void maxAirQualityIndexUpdatesAqiWithHigherValue() {
        AirQualityIndex loAqi = new AirQualityIndex(10);
        regionForecast.setAirQualityIndex(loAqi);

        AirQualityIndex hiAqi = new AirQualityIndex(90);
        regionForecast.maxAirQualityIndex(hiAqi);

        assert regionForecast.getAirQualityIndex() == hiAqi;
    }

    @Test
    void maxAirQualityIndexDoesNotUpdateAqiWithLowerValue() {
        AirQualityIndex hiAqi = new AirQualityIndex(90);
        regionForecast.setAirQualityIndex(hiAqi);

        AirQualityIndex loAqi = new AirQualityIndex(10);
        regionForecast.maxAirQualityIndex(loAqi);

        assert regionForecast.getAirQualityIndex() == hiAqi;
    }

    @Test
    void maxAirQualityIndexDoesNotUpdateIfNull() {
        AirQualityIndex aqi = new AirQualityIndex(0);
        regionForecast.setAirQualityIndex(aqi);

        regionForecast.maxAirQualityIndex(null);

        assert regionForecast.getAirQualityIndex() == aqi;
    }

    @Test
    void maxAirQualityIndexUpdatesIfValueIsAlreadyNull() {
        AirQualityIndex aqi = new AirQualityIndex(0);
        regionForecast.maxAirQualityIndex(aqi);

        assert regionForecast.getAirQualityIndex() == aqi;
    }

    @Test
    void maxHeatRiskIndexUpdatesAqiWithHigherValue() {
        HeatRiskIndex loHri = new HeatRiskIndex(10);
        regionForecast.setHeatRiskIndex(loHri);

        HeatRiskIndex hiHri = new HeatRiskIndex(90);
        regionForecast.maxHeatRiskIndex(hiHri);

        assert regionForecast.getHeatRiskIndex() == hiHri;
    }

    @Test
    void maxHeatRiskIndexDoesNotUpdateAqiWithLowerValue() {
        HeatRiskIndex hiHri = new HeatRiskIndex(90);
        regionForecast.setHeatRiskIndex(hiHri);

        HeatRiskIndex loHri = new HeatRiskIndex(10);
        regionForecast.maxHeatRiskIndex(loHri);

        assert regionForecast.getHeatRiskIndex() == hiHri;
    }

    @Test
    void maxHeatRiskIndexDoesNotUpdateIfNull() {
        HeatRiskIndex hri = new HeatRiskIndex(0);
        regionForecast.setHeatRiskIndex(hri);

        regionForecast.maxHeatRiskIndex(null);

        assert regionForecast.getHeatRiskIndex() == hri;
    }

    @Test
    void maxHeatRiskIndexUpdatesIfValueIsAlreadyNull() {
        HeatRiskIndex hri = new HeatRiskIndex(0);
        regionForecast.maxHeatRiskIndex(hri);

        assert regionForecast.getHeatRiskIndex() == hri;
    }

    @Test
    void addTemperatureAvgUpdatesCreatesNewListIfNull() {
        assert regionForecast.getTemperatureAverages() == null;
        TemperatureAverage ta = new TemperatureAverage(20);
        regionForecast.addTemperatureAvg(ta);
        assert regionForecast.getTemperatureAverages().equals(List.of(ta));
    }

    @Test
    void addTemperatureAvgUpdatesExistingList() {
        TemperatureAverage ta1 = new TemperatureAverage(20);
        TemperatureAverage ta2 = new TemperatureAverage(30);
        regionForecast.addTemperatureAvg(ta1);
        regionForecast.addTemperatureAvg(ta2);
        assert regionForecast.getTemperatureAverages().equals(List.of(ta1, ta2));

        TemperatureAverage newTa = new TemperatureAverage(25);
        assert regionForecast.getTemperatureAverage().getValue().equals(newTa.getValue());
    }

    @Test
    void maxTemperatureHighUpdatesAqiWithHigherValue() {
        TemperatureHigh loHigh = new TemperatureHigh(10);
        regionForecast.setTemperatureHigh(loHigh);

        TemperatureHigh hiHigh = new TemperatureHigh(90);
        regionForecast.maxTemperatureHigh(hiHigh);

        assert regionForecast.getTemperatureHigh() == hiHigh;
    }

    @Test
    void maxTemperatureHighDoesNotUpdateAqiWithLowerValue() {
        TemperatureHigh hiHigh = new TemperatureHigh(90);
        regionForecast.setTemperatureHigh(hiHigh);

        TemperatureHigh loHigh = new TemperatureHigh(10);
        regionForecast.maxTemperatureHigh(loHigh);

        assert regionForecast.getTemperatureHigh() == hiHigh;
    }

    @Test
    void minTemperatureLowUpdatesAqiWithLowerValue() {
        TemperatureLow hiLow = new TemperatureLow(90);
        regionForecast.setTemperatureLow(hiLow);

        TemperatureLow loLow = new TemperatureLow(10);
        regionForecast.minTemperatureLow(loLow);

        assert regionForecast.getTemperatureLow() == loLow;
    }

    @Test
    void minTemperatureLowDoesNotUpdateAqiWithHigherValue() {
        TemperatureLow loLow = new TemperatureLow(10);
        regionForecast.setTemperatureLow(loLow);

        TemperatureLow hiLow = new TemperatureLow(90);
        regionForecast.minTemperatureLow(hiLow);

        assert regionForecast.getTemperatureLow() == loLow;
    }
}