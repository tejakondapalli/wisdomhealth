package com.wisdom.region.rest.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblBoilerplateRegion")
public class BoilerplateRegion implements java.io.Serializable {

	private Integer boilerplateRegionId;
	
	private String start;
	private String end;
	
	private Boolean startRegex;
	private Boolean endRegex;
	
	private String wholePhrase;
	private Boolean wholePhraseRegex;
	
	private Boolean exactWhitespace;
	
	

	public BoilerplateRegion(String start, String end, Boolean startRegex2, Boolean endRegex2,
			 String wholePhrase, Boolean wholePhraseRegex2, Boolean exactWhitespace2) {
		super();
		this.start = start;
		this.startRegex = startRegex2;
		this.endRegex = endRegex2;
		this.end = end;
		this.wholePhrase = wholePhrase;
		this.wholePhraseRegex = wholePhraseRegex2;
		this.exactWhitespace = exactWhitespace2;
		
	}

	public void setAllBoilerplateValues(String start, String end, Boolean startRegex2, Boolean endRegex2,
			 String wholePhrase, Boolean wholePhraseRegex2, Boolean exactWhitespace2) {
		this.start = start;
		this.startRegex = startRegex2;
		this.endRegex = endRegex2;
		this.end = end;
		this.wholePhrase = wholePhrase;
		this.wholePhraseRegex = wholePhraseRegex2;
		this.exactWhitespace = exactWhitespace2;
		
	}

	public BoilerplateRegion() {
	}

private Integer id;
	

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "boilerplateRegionId", unique = true, nullable = false)
	public Integer getBoilerplateRegionId() {
		return boilerplateRegionId;
	}

	public void setBoilerplateRegionId(Integer boilerplateRegionId) {
		this.boilerplateRegionId = boilerplateRegionId;
	}

	@Column(name = "start", length = 500)
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	@Column(name = "startRegex", length = 1, nullable = true)
	public Boolean getStartRegex() {
		return startRegex;
	}

	public void setStartRegex(Boolean startRegex) {
		this.startRegex = startRegex;
	}

	@Column(name = "endRegex", length = 1, nullable = true)
	public Boolean getEndRegex() {
		return endRegex;
	}

	public void setEndRegex(Boolean endRegex) {
		this.endRegex = endRegex;
	}

	@Column(name = "end", length = 500)
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	@Column(name = "wholePhrase", length = 500)
	public String getWholePhrase() {
		return wholePhrase;
	}

	public void setWholePhrase(String wholePhrase) {
		this.wholePhrase = wholePhrase;
	}

	@Column(name = "wholePhraseRegex", length = 1)
	public Boolean getWholePhraseRegex() {
		return wholePhraseRegex;
	}

	public void setWholePhraseRegex(Boolean wholePhraseRegex) {
		this.wholePhraseRegex = wholePhraseRegex;
	}

	@Column(name = "exactWhitespace", length = 1)
	public Boolean getExactWhitespace() {
		return exactWhitespace;
	}

	public void setExactWhitespace(Boolean exactWhitespace) {
		this.exactWhitespace = exactWhitespace;
	}


	

	
}
