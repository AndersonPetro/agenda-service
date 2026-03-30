package com.common.gmt.query;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class QueryFilters {

    public Map<String, String> collectFiltersToMap(Map<String, String> filters, List<String> availableFilters) {
        return filters.entrySet().stream()
                .filter(entry -> availableFilters.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
