package ru.geochat.model.dao;

import java.util.ArrayList;

import com.grum.geocalc.*;


public class Geo {
	private double latitude;
	private double longitude;

	
	public Geo(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Geo() {
	}
	
	public ArrayList<Geo> getBoundsByRadius(float radius)
	{
		Coordinate lat = new DegreeCoordinate(this.latitude);
		Coordinate lng = new DegreeCoordinate(this.longitude);
		Point thisGeo = new Point(lat, lng);
		BoundingArea area = EarthCalc.getBoundingArea(thisGeo, radius);
		Point nw = area.getNorthWest();
		Point se = area.getSouthEast();
		Geo geoNw = new Geo(nw.getLatitude(), nw.getLongitude());
		Geo geoSe = new Geo(se.getLatitude(), se.getLongitude());
		ArrayList<Geo> returnList = new ArrayList<Geo>();
		returnList.add(geoNw);
		returnList.add(geoSe);
		return returnList;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
}
