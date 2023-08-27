package org.kcrha.weather.models.cli;

import java.util.List;

public record Region(String region, List<Location> locations) {
}
