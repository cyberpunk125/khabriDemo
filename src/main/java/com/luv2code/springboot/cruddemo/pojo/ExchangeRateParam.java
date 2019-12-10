package com.luv2code.springboot.cruddemo.pojo;

import javax.validation.constraints.NotNull;

public class ExchangeRateParam {
	
	@NotNull
	private String start_at;
	
	@NotNull
	private String end_at;
	
	@NotNull
	private String symbols;

	

	public String getStart_at() {
		return start_at;
	}

	public void setStart_at(String start_at) {
		this.start_at = start_at;
	}

	public String getEnd_at() {
		return end_at;
	}

	public void setEnd_at(String end_at) {
		this.end_at = end_at;
	}

	public String getSymbols() {
		return symbols;
	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

}
