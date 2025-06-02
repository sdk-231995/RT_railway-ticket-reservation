package com.rtcall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.rtcall.exception.RestCallException;

@Service
public class RestTemplateService {
    
    @Autowired
    private RestTemplate restTemplate;

    public <T, R> R postForObject(String url, T request, Class<R> responseType) {
        try {
            return restTemplate.postForObject(url, request, responseType);
        } catch (RestClientException e) {
            throw new RestCallException("Failed to call service at " + url + ": " + e.getMessage());
        }
    }

    public <R> R getForObject(String url, Class<R> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (RestClientException e) {
            throw new RestCallException("GET call failed for " + url + ": " + e.getMessage());
        }
    }

}
