package com.wisdom.dictionaries.rest.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.common.httpclient.RestClient;
import com.wisdom.dictionaries.rest.dao.VariantsDao;
import com.wisdom.dictionaries.test.model.DevEngineInput;
import com.wisdom.usermgmt.rest.aspects.Test;

/**
 * Class RestBranchController
 */
@RestController
@RequestMapping("/variants")
@EnableAutoConfiguration
public class RestVariantsController {

	/**
	 * Create a new branch with an auto-generated id and email and name as
	 * passed values.
	 */
	
	@RequestMapping(value = "", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String create(@RequestBody String variants) {
		String msg = "success";
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject();
			jsObject.put("variants", new JSONArray(variants));
			responseJson = variantDao.create(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			msg =  "Error creating the variant: " + ex.toString();
		}
		
		if(responseJson == null){
			responseJson = new JSONObject();
			responseJson.put("message", msg);
		}
		
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createUser(@RequestBody String variants) {
		JSONObject responseJson = new JSONObject();
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/workspaces/{id}", method = RequestMethod.PUT, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String updateWorkspace(@PathVariable Integer id, @RequestBody String workspaces) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject(workspaces);
			jsObject.put("workspaceId", id);
			responseJson = variantDao.updateWorkspaceById(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/workspaces/{id}", method = RequestMethod.DELETE, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String deleteWorkspace(@PathVariable Integer id) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject();
			jsObject.put("workspaceId", id);
			responseJson = variantDao.deleteWorkspaceById(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	@Test
	@RequestMapping(value = "/tests/{id}", method = RequestMethod.PUT, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String updateTest(@PathVariable Integer id, @RequestParam String type, @RequestBody String variants) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject(variants);
			jsObject.put("testId", id);
			jsObject.put("type", type);
			responseJson = variantDao.updateTestById(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadVariants(@RequestParam(required = true, value = "key") String key,
			@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject responseJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("key", key);
			inputJson.put("value", value);
			inputJson.put("page", page!=null && page > 0 ? page : 1);
			inputJson.put("limit", limit!=null && limit > 0 ? limit : 10);// default limit 10;
			
			responseJson = variantDao.loadVariants(inputJson);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/variantsbyids", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadVariantsById(@RequestBody String variants) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject(variants);
			responseJson = variantDao.loadVariantsByIds(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/variantByIds", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadVariantsByIds(@RequestBody String variantIds) {
		JSONObject responseJson = null;
		try {
			JSONArray jsObject = new JSONArray(variantIds);
			responseJson = variantDao.loadWorkspaceVariantsByIds(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/edits", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadEdit(@RequestParam Integer branchId) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject();
			jsObject.put("branchId", branchId);
			responseJson = variantDao.loadEdit(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/workspaces", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadWorkspace(@RequestBody String workspaces) {
		JSONObject responseJson = null;
		try {
			JSONObject jsObject = new JSONObject();
			jsObject.put("editIds", new JSONArray(workspaces));
			responseJson = variantDao.loadWorkspace(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/submitForTest", method = RequestMethod.POST, produces = {
			"text/plain" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String submitForTest(@RequestBody String testRequest,
			@RequestParam(required = false, value = "testId") Integer testId) {
		String responseJson = "Submitted Successfully";
		try {
			DevEngineInput devEngineInput = new DevEngineInput();
			
			JSONObject requstJson = new JSONObject(testRequest);
			requstJson.remove("workspaceIds");
			String testSumbitUrl = env.getProperty("test.submitUrl");
			RestClient restClient = new RestClient();
			responseJson = restClient.post(testSumbitUrl, requstJson.toString());
			responseJson = restClient.get(testSumbitUrl + "/" + testId);

		} catch (Exception ex) {
			return "{\"Error submitting to the Test\" : \"" + ex.getClass().getName() + "\"}";
		}
		return responseJson;
	}

	
	@RequestMapping(value = "/cmps/generate", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String cmpGenerate() {
		JSONObject responseJson = null;
		try {

			responseJson = variantDao.cmpGenerate();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = {
			"application/text" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String download(@RequestBody String variants) {
		JSONObject responseJson = new JSONObject();
		;
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	// Wire the TblBranchDao used inside this controller.
	@Autowired
	private VariantsDao variantDao;

	@Autowired
	private Environment env;

}
