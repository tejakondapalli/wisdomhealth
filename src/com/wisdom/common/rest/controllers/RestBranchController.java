package com.wisdom.common.rest.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.common.dao.BranchDao;
import com.wisdom.usermgmt.rest.aspects.Merge;
import com.wisdom.usermgmt.rest.aspects.Test;

/**
 * Class RestBranchController
 */
@RestController
@RequestMapping("/branches")
@EnableAutoConfiguration
public class RestBranchController {

	private Object variantDao;

	/**
	 * Create a new branch with an auto-generated id and email and name as
	 * passed values.
	 */
	
	@RequestMapping(method = RequestMethod.POST, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String create(@RequestBody String branch) {
		JSONObject responseJson = null;
		try {
			responseJson = branchDao.create(new JSONObject(branch));
		} catch (Exception ex) {
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllBranches(@RequestParam(required = false, value = "userId") Integer userId,
			@RequestParam(required = false, value = "branchName") String branchName) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("userId", userId);
			if (!"".equals(branchName)) {
				inputJson.put("branchName", branchName);

			}
			System.out.println("Input JSON " + inputJson);
			branchJson = branchDao.getAll(inputJson, false);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/ready", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllBranchestoMerge(@RequestParam(required = false, value = "userId") Integer userId) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("userId", userId);
			System.out.println("Input JSON " + inputJson);
			branchJson = branchDao.getAll(inputJson, true);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	//@Merge
	@RequestMapping(value = "/releases/move", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String moveRelease(@RequestParam String key, @RequestBody String variants) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject(variants);
			jsObject.put("key", key);
			responseJson = branchDao.moveRelease(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	@RequestMapping(value = "/tests/{id}", method = RequestMethod.PUT, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String updateTest(@PathVariable Integer id, @RequestParam String type, @RequestBody String variants) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject(variants);
			jsObject.put("testId", id);
			jsObject.put("type", type);
			responseJson = branchDao.updateTestById(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	/**
	 * Update the email and the name for the branch indentified by the passed
	 * id.
	 */
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String update(@PathVariable int id, @RequestBody String payload) {
		JSONObject responseJson = null;
		try {
			JSONObject json = new JSONObject(payload);
			json.put("branchId", id);
			responseJson = branchDao.updateBranchById(json);
		} catch (Exception ex) {
			return "Error updating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	// Wire the TblBranchDao used inside this controller.
	@Autowired
	private BranchDao branchDao;
	

} // class TblBranchController
