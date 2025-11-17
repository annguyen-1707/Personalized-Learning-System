package vn.fu_ohayo.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(List<DayOfWeek> days) {
        return days != null ? days.stream()
                .map(Enum::name)
                .collect(Collectors.joining(",")) : "";
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(String daysString) {
        if (daysString == null || daysString.isBlank()) return Collections.emptyList();
        return Arrays.stream(daysString.split(","))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toList());
    }
}
