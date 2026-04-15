package com.agenda.contract.v1.response;

import com.agenda.domain.appointment.dtos.TimeSlotDto;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record TimeSlotResponse(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Boolean available
) {
    public static TimeSlotResponse fromDomain(TimeSlotDto dto) {
        return TimeSlotResponse.builder()
                .date(dto.getDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .available(dto.getAvailable())
                .build();
    }
}

