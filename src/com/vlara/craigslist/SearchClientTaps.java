package com.vlara.craigslist;

import java.io.IOException;
import java.util.List;

import com.threetaps.client.SearchClient;
import com.threetaps.client.ThreetapsClient;
import com.threetaps.dto.search.SearchRequest;
import com.threetaps.dto.search.SearchResponse;
import com.threetaps.model.Posting;

public class SearchClientTaps {

	protected SearchClient searchClient;

	public SearchClientTaps(){
		this.setUp();
	}
	
	protected void setUp(){
		searchClient = ThreetapsClient.getInstance().setAuthID(Config.api_key).getSearchClient();
	}
	
	public List<Posting> search(String searchTerm, String location, String category) throws IOException {
		final SearchRequest searchRequest = new SearchRequest();
		searchRequest.setText(searchTerm);
		searchRequest.setSource("CRAIG");
		searchRequest.setLocation(location);
		searchRequest.setCategory(category);
		
		SearchResponse searchResponse = searchClient.search(searchRequest);
		return searchResponse.getResults();
	}
	
}
