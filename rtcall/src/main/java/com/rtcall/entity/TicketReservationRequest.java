package com.rtcall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketReservationRequest {
    private String fromStation;
    private String toStation;
    private String trainCode;
    private String fromDate;
    private String toDate;
    private double fromKm;
    private double toKm;
}
