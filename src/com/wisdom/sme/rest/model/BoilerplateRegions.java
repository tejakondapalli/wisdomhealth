package com.wisdom.sme.rest.model;

public class BoilerplateRegions {

	private Integer id;
	private String start;
	private boolean isStartRegex;
	private String end;
	private boolean isEndRegex;
	private boolean isWholePhraseRegex;
	private boolean isExactWhiteSpace;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public boolean isStartRegex() {
		return isStartRegex;
	}

	public void setStartRegex(boolean isStartRegex) {
		this.isStartRegex = isStartRegex;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isEndRegex() {
		return isEndRegex;
	}

	public void setEndRegex(boolean isEndRegex) {
		this.isEndRegex = isEndRegex;
	}

	public boolean isWholePhraseRegex() {
		return isWholePhraseRegex;
	}

	public void setWholePhraseRegex(boolean isWholePhraseRegex) {
		this.isWholePhraseRegex = isWholePhraseRegex;
	}

	public boolean isExactWhiteSpace() {
		return isExactWhiteSpace;
	}

	public void setExactWhiteSpace(boolean isExactWhiteSpace) {
		this.isExactWhiteSpace = isExactWhiteSpace;
	}

}
