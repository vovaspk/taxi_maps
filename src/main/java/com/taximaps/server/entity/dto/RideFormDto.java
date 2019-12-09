package com.taximaps.server.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RideFormDto {
    private String origin;
    private String destination;
    private Date date;
}
