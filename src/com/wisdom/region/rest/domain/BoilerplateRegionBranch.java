package com.wisdom.region.rest.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblBoilerplateRegionBranch")
public class BoilerplateRegionBranch implements java.io.Serializable {

	private Integer boilerplateRegionId;
	
	private String start;
	private String end;
	
	private boolean startRegex;
	private boolean endRegex;
	
	private String wholePhrase;
	private boolean wholePhraseRegex;
	
	private boolean exactWhitespace;
	
	private byte action;
	private Integer fkBoilerplateRegionId;
	private Integer fkBranchId;
	private Integer fkTestId;
	private byte status;
	

	public BoilerplateRegionBranch(String start, String end, boolean startRegex2, boolean endRegex2,
			 String wholePhrase, boolean wholePhraseRegex2, boolean exactWhitespace2, Integer fkBoilerplateRegionId, Integer fkBranchId, byte action,Integer fkTestId,byte status) {
		super();
		this.start = start;
		this.startRegex = startRegex2;
		this.endRegex = endRegex2;
		this.end = end;
		this.wholePhrase = wholePhrase;
		this.wholePhraseRegex = wholePhraseRegex2;
		this.exactWhitespace = exactWhitespace2;
		this.fkBoilerplateRegionId = fkBoilerplateRegionId;
		this.fkBranchId = fkBranchId;
		this.action = action;
		this.fkTestId = fkTestId;
		this.status = status;
		
	}

	public void setAllBoilerplateValues(String start, String end, boolean startRegex2, boolean endRegex2,
			 String wholePhrase, boolean wholePhraseRegex2, boolean exactWhitespace2, Integer fkBoilerplateRegionId, Integer fkBranchId, byte action,Integer fkTestId,byte status) {
		this.start = start;
		this.startRegex = startRegex2;
		this.endRegex = endRegex2;
		this.end = end;
		this.wholePhrase = wholePhrase;
		this.wholePhraseRegex = wholePhraseRegex2;
		this.exactWhitespace = exactWhitespace2;
		this.fkBoilerplateRegionId = fkBoilerplateRegionId;
		this.fkBranchId = fkBranchId;
		this.action = action;
		this.fkTestId = fkTestId;
		this.status = status;
		
	}

	public BoilerplateRegionBranch() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
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

	@Column(name = "startRegex", length = 1)
	public boolean getStartRegex() {
		return startRegex;
	}

	public void setStartRegex(boolean startRegex) {
		this.startRegex = startRegex;
	}

	@Column(name = "endRegex", length = 1)
	public boolean getEndRegex() {
		return endRegex;
	}

	public void setEndRegex(boolean endRegex) {
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
	public boolean getWholePhraseRegex() {
		return wholePhraseRegex;
	}

	public void setWholePhraseRegex(boolean wholePhraseRegex) {
		this.wholePhraseRegex = wholePhraseRegex;
	}

	@Column(name = "exactWhitespace", length = 1)
	public boolean getExactWhitespace() {
		return exactWhitespace;
	}

	public void setExactWhitespace(boolean exactWhitespace) {
		this.exactWhitespace = exactWhitespace;
	}


	public byte getAction() {
		return action;
	}


	public void setAction(byte action) {
		this.action = action;
	}


	public Integer getFkBoilerplateRegionId() {
		return fkBoilerplateRegionId;
	}


	public void setFkBoilerplateRegionId(Integer fkBoilerplateRegionId) {
		this.fkBoilerplateRegionId = fkBoilerplateRegionId;
	}


	public Integer getFkBranchId() {
		return fkBranchId;
	}


	public void setFkBranchId(Integer fkBranchId) {
		this.fkBranchId = fkBranchId;
	}

	public Integer getFkTestId() {
		return fkTestId;
	}

	public void setFkTestId(Integer fkTestId) {
		this.fkTestId = fkTestId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	
	
}
