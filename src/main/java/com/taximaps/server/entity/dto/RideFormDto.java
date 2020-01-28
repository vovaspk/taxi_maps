package com.taximaps.server.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class RideFormDto {
    @NotNull(message = "Origin must not be null")
    private String origin;
    @NotNull(message = "Destination must not be null")
    private String destination;
    @NotNull(message = "You must choose car type")
    private String carType;
    @NotNull(message = "Date must not be null")
    private Date date;
}
