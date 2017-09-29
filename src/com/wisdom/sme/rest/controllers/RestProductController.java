package com.wisdom.sme.rest.controllers;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.region.test.model.DeltaRegionRules;
import com.wisdom.region.test.model.TestWithRelease;
import com.wisdom.sme.rest.dao.SMEDataDao;
import com.wisdom.usermgmt.rest.aspects.Authorized;

/**
 * Class RestBranchController
 */
@RestController
@RequestMapping("/products")
@EnableAutoConfiguration
public class RestProductController {

	@RequestMapping(value = "/edits", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public TestWithRelease getEditsByBranch(@RequestParam(required = true, value = "branchId") Integer branchId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit,HttpServletResponse response) {
		TestWithRelease branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("branchId", branchId);
			branchJson = regionDao.loadEdit(inputJson);
			response.setStatus(branchJson.getStatus());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			branchJson.setStatus(500);
			branchJson.setMessage("Internal Server Issue");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return branchJson;
		}
		return branchJson;
	}

	@RequestMapping(value = "/regionRules", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public DeltaRegionRules getRegionRules(HttpServletResponse response) {
		DeltaRegionRules regionRules = null;
		try {
			regionRules = regionDao.loadPreReleaseData();
			if(regionRules.getReleaseNumber() == null){
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return regionRules;
	}

	@RequestMapping(value = "/RC-Regions-LatestRelease.json", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public DeltaRegionRules getRegionLatestRules(HttpServletResponse response) {
		DeltaRegionRules regionRules = null;
		try {
			regionRules = regionDao.loadPreReleaseData();
			if (regionRules.getReleaseNumber() == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return null;
		}
		return regionRules;
	}

	// Wire the TblBranchDao used inside this controller.
	@Autowired
	private SMEDataDao regionDao;

} // class TblBranchController
