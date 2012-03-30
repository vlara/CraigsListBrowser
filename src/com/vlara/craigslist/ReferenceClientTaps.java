package com.vlara.craigslist;

import java.io.IOException;
import java.util.List;

import com.threetaps.client.ReferenceClient;
import com.threetaps.client.ThreetapsClient;
import com.threetaps.model.Category;
import com.threetaps.model.Location;
import com.threetaps.model.Source;

public class ReferenceClientTaps {
	protected ReferenceClient referenceClient;
	
	public ReferenceClientTaps(){
		this.setUp();
	}
	
	protected void setUp(){
		referenceClient = ThreetapsClient.getInstance().setAuthID(Config.api_key).getReferenceClient();
	}
	
	public List<Source> getSources() throws IOException {
		List<Source> sources = referenceClient.getSources();
		return sources;
	}
	
	public List<Location> getLocations() throws IOException {
		List<Location> locations = referenceClient.getLocations();
		return locations;
	}
	
	public List<Category> getCategories() throws IOException {
		List<Category> categories = referenceClient.getCategories();
		return categories;
	}
}
