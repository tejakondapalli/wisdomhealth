package com.wisdom.sme.rest.model;

import java.util.ArrayList;
import java.util.List;

public class DeltaEdit {
	
	String stableReleaseNumber = null;
	List<com.wisdom.sme.rest.model.RegionEdit> edits;

	public String getStableReleaseNumber() {
		return stableReleaseNumber;
	}

	public void setStableReleaseNumber(String stableReleaseNumber) {
		this.stableReleaseNumber = stableReleaseNumber;
	}

	public List<com.wisdom.sme.rest.model.RegionEdit> getEdits() {
		if(edits == null) {
			edits = new ArrayList<com.wisdom.sme.rest.model.RegionEdit>();
		}
		return edits;
	}

	public void setEdits(List<com.wisdom.sme.rest.model.RegionEdit> edits) {
		this.edits = edits;
	}
	
	
	

}
