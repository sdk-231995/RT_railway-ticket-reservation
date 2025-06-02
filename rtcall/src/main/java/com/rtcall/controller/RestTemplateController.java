package com.rtcall.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rtcall.entity.TicketReservationRequest;
import com.rtcall.service.GenericRestClientService;
import com.rtcall.service.RestTemplateService;

@RestController
@RequestMapping("/rtcall")
public class RestTemplateController {

	@Autowired
	private  GenericRestClientService genericRestClientService;
    @Autowired
    private RestTemplateService restService;

    @PostMapping("/book-ticket")
    public ResponseEntity<String> bookTicket(@RequestBody TicketReservationRequest request) {
        String reservationServiceUrl = "http://localhost:8084/ticket-reservation/book";
        String response = restService.postForObject(reservationServiceUrl, request, String.class);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/proxy-ticket-pdf/{pnrNo}")
    public ResponseEntity<byte[]> proxyGeneratePdf(@PathVariable String pnrNo) {
        String url = "http://localhost:8084/api/tickets/generate-ticket-pdf/{pnrNo}";

        byte[] pdfBytes = genericRestClientService.callApi(
            url,
            HttpMethod.GET,
            null,
            byte[].class,
            Map.of("pnrNo", pnrNo),
            null,
            null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
            ContentDisposition.builder("attachment").filename("Ticket-" + pnrNo + ".pdf").build()
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
