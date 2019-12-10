package com.luv2code.springboot.cruddemo.rest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.luv2code.springboot.cruddemo.pojo.ExchangeRateParam;

@RestController
@RequestMapping("/api")
public class UserRestController {
	
	
	@GetMapping("/khabri/average/excahngeRate")
	public ResponseEntity<?> averageExchangeRate(@Valid ExchangeRateParam exch) throws Exception {		
		String uri="https://api.exchangeratesapi.io/history";
		RestTemplate restTemplate = new RestTemplate();		
		HttpHeaders headers = new HttpHeaders();	
		System.out.println(exch.getStart_at()+"     "+exch.getEnd_at()+"    "+exch.getSymbols());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
		        .queryParam("start_at", exch.getStart_at())
		        .queryParam("end_at", exch.getEnd_at())
		        .queryParam("symbols", exch.getSymbols());
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ParameterizedTypeReference<HashMap<String, Object>> responseType = 
	               new ParameterizedTypeReference<HashMap<String, Object>>() {};
		HttpEntity<HashMap<String,Object>> response = restTemplate.exchange(
		        builder.toUriString(), 
		        HttpMethod.GET, 
		        entity, 
		        responseType);
		HashMap<String,Object> responseMap=response.getBody();
		String startDate=(String) responseMap.get("start_at");
		String endDate=(String) responseMap.get("end_at");
		Long numberOfDays=findNumberOfDays(startDate, endDate);
		HashMap<String,BigDecimal> result=averageOfExchangeRate(responseMap,numberOfDays);
		
		return ResponseEntity.ok(result);
		
	}
	
	private HashMap<String,BigDecimal> averageOfExchangeRate(HashMap<String,Object> responseBody,Long n) {
		Map<String,Map<String,Double>> rates=(Map<String, Map<String,Double>>) responseBody.get("rates");
		HashMap<String,BigDecimal> hm=new HashMap<String, BigDecimal>();
		rates.forEach((k,v)->{
			v.forEach((k1,v1)->{
				hm.put(k1, BigDecimal.valueOf(v1/n).add(hm.getOrDefault(k1, BigDecimal.valueOf(0.0))));					
			});
		});
		hm.forEach((k,v)->System.out.println(k+"  :  "+v));
		return hm;
		
	
	}
	
	private Long findNumberOfDays(String startDate,String endDate) {
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");		
		Long numberOfDays=0L;
		try {
		    Date date1 = myFormat.parse(startDate);
		    Date date2 = myFormat.parse(endDate);
		    long diff = date2.getTime() - date1.getTime();
		    numberOfDays=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		    numberOfDays=numberOfDays+1;
		    System.out.println ("Days: " + numberOfDays);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		return numberOfDays;
	}
	

	
	
	
	

}
