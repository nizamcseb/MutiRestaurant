package com.mst.mutirestaurant.support;

public class WorldPopulation {


	private String country;
	private String code;
 
	public WorldPopulation(String country, String code) {
		
		this.country = country;
		this.code = code;
	}
 

 
	public String getCountry() {
		return this.country;
	}
 
	
	public String getRank() {
		return this.code;
	}
	
	
}
