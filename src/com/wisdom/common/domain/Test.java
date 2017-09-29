package com.wisdom.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tblTest")
public class Test implements java.io.Serializable {

	private Integer testId;
	private byte engineType;
	private String testInput;
	private String testResult;
	private Release release;
	
	
	public Test() {
	}

	public Test(byte engineType, String testInput, String testResult) {
		
		this.engineType = engineType;
		this.testInput = testInput;
		this.testResult = testResult;
	}

	

	@Id

	@Column(name = "testId", unique = true, nullable = false)
	public Integer getTestId() {
		return this.testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	@Column(name = "engineType", nullable = false)
	public byte getEngineType() {
		return this.engineType;
	}

	public void setEngineType(byte engineType) {
		this.engineType = engineType;
	}

	@Column(name = "testInput", nullable = false, length = 65535)
	public String getTestInput() {
		return this.testInput;
	}

	public void setTestInput(String testInput) {
		this.testInput = testInput;
	}

	@Column(name = "testResult", nullable = false, length = 65535)
	public String getTestResult() {
		return this.testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fkReleaseId", nullable = true)
	public Release getRelease() {
		return release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

}
