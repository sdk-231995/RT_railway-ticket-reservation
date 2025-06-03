package com.rtcall.service;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.rtcall.exception.RestCallException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenericRestClientService2 {

  private final RestTemplate restTemplate;

  private HttpHeaders getHttpHeaders(Map<String ,String > headersValue) {
	  
	  HttpHeaders httpHeaders=new  HttpHeaders();
	  httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	  httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		    
//		    if (headersValue != null) {
//		        for (Map.Entry<String, String> entry : headersValue.entrySet()) {
//		            httpHeaders.add(entry.getKey(), entry.getValue());
//		        }
//		    }
		    
	  httpHeaders.setAll(headersValue);
	  return httpHeaders;
	  
  }
  
  public ResponseEntity<Object> getRestResponseMessage(String url,HttpMethod httpMethod,Class<Object> returnType,HttpEntity< Object> entityObject) {
	  
	  try {
		return restTemplate.exchange(url, httpMethod,entityObject,returnType);
	} catch (Exception e) {
		throw new RestCallException(e.getMessage());
	}
	  
  }
  
//  public ResponseEntity<Object> getRestResponse(String url,HttpMethod httpMethod,HttpEntity< Object> entityObject,Class returnType) {
//	  
//	  try {
//		return restTemplate.exchange(url, httpMethod,entityObject,returnType);
//	} catch (Exception e) {
//		throw new RestCallException(e.getMessage());
//	}
//	  
//  }

  public ResponseEntity<Object> serviceCall(String url,HttpMethod httpMethod,Object req, Class<Object> returnType,HttpHeaders httpHeaders){
	  HttpEntity<Object> entity=new HttpEntity<>(req,httpHeaders);
	  return getRestResponseMessage(url,httpMethod,returnType,entity);
  }
  
  public ResponseEntity<Object> getServiceRestCall(String url,HttpMethod httpMethod,Object req,Class<Object> returnType,Map<String ,String > headersValue){
	  return serviceCall(url,httpMethod,req,returnType,getHttpHeaders(headersValue));
  }
  
  public ResponseEntity<Object> fetchMessageFromOtherService(Map<String ,String > headersValue ){
	  
	  URI uriComponentsBuilder=UriComponentsBuilder.fromUriString("http://localhost:8084")
			  .path("/api/tickets/Testing")
			  .build()
		        .toUri();
	  System.out.println("url "+uriComponentsBuilder);
			  return  getServiceRestCall(uriComponentsBuilder.toString(),HttpMethod.GET,null,Object.class,headersValue);
	 
  }
  
}
