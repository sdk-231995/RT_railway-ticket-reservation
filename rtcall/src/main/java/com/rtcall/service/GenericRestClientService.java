package com.rtcall.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.rtcall.exception.ServiceCallException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenericRestClientService {

    private final RestTemplate restTemplate;

    public <T> T get(String url, Class<T> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceCallException("GET request failed for: " + url + ", Status: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new ServiceCallException("Unexpected error during GET call to: " + url, ex);
        }
    }

    public <T, R> R post(String url, T requestBody, Class<R> responseType) {
        try {
            return restTemplate.postForObject(url, requestBody, responseType);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceCallException("POST request failed for: " + url + ", Status: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new ServiceCallException("Unexpected error during POST call to: " + url, ex);
        }
    }

    public <T> void put(String url, T requestBody) {
        try {
            restTemplate.put(url, requestBody);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceCallException("PUT request failed for: " + url + ", Status: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new ServiceCallException("Unexpected error during PUT call to: " + url, ex);
        }
    }

    public void delete(String url) {
        try {
            restTemplate.delete(url);
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceCallException("DELETE request failed for: " + url + ", Status: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new ServiceCallException("Unexpected error during DELETE call to: " + url, ex);
        }
    }
    
    public <T, R> R callApi(
            String url,
            HttpMethod method,
            T requestBody,
            Class<R> responseType,
            Map<String, ?> pathVariables,
            Map<String, ?> queryParams,
            HttpHeaders headers
    ) {
        try {
            // Build the full URL with query parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
            if (queryParams != null) {
                queryParams.forEach(builder::queryParam);
            }

            // Expand path variables
            String finalUrl = (pathVariables != null)
                    ? builder.buildAndExpand(pathVariables).toUriString()
                    : builder.toUriString();

            // Wrap the request body and headers
            HttpEntity<T> entity = new HttpEntity<>(requestBody, headers != null ? headers : new HttpHeaders());

            // Perform the HTTP request
            ResponseEntity<R> response = restTemplate.exchange(
                    finalUrl,
                    method,
                    entity,
                    responseType
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new ServiceCallException("Request failed for: " + url + ", Status: " + ex.getStatusCode(), ex);
        } catch (Exception ex) {
            throw new ServiceCallException("Unexpected error during call to: " + url, ex);
        }
    }
}
