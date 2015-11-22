package com.tarena.allrun.entity;

import java.io.Serializable;

public class TopicEntity implements Serializable{
	private String 
	username,body,imageAddress,locationAddress;
	private double time,longitude,latitude;
	
	
	public TopicEntity(String username, String body, String imageAddress,
			String locationAddress, double time, double longitude,
			double latitude) {
		super();
		this.username = username;
		this.body = body;
		this.imageAddress = imageAddress;
		this.locationAddress = locationAddress;
		this.time = time;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getImageAddress() {
		return imageAddress;
	}
	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}
	public String getLocationAddress() {
		return locationAddress;
	}
	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	

}
