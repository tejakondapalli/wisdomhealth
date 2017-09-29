package com.wisdom.region.rest.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.common.domain.Branch;
import com.wisdom.common.httpclient.RestClient;
import com.wisdom.region.rest.dao.RegionsDao;
import com.wisdom.usermgmt.rest.aspects.Test;

/**
 * Class RestBranchController
 */
@RestController
@RequestMapping("/regions")
@EnableAutoConfiguration
public class RestRegionsController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String create(@RequestBody String regions) {
		JSONObject responseJson = null;
		try {
			JSONArray jsonArray = new JSONArray(regions);
			JSONObject jsObject = new JSONObject();
			jsObject.put("regions", jsonArray);
			responseJson = regionDao.create(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/titles/create", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createRegionTitles(@RequestBody String regiontitles) {
		JSONObject responseJson = null;
		try {
			responseJson = regionDao.createRegionTitles(new JSONObject(regiontitles));
		} catch (Exception ex) {
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/uses/create", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createRegionUses(@RequestBody String regionUses) {
		JSONObject responseJson = null;
		try {
			responseJson = regionDao.createRegionUses(new JSONObject(regionUses));
		} catch (Exception ex) {
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/normalizedtitles/create", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String createRegionNTitles(@RequestBody String normalizedregiontitles) {
		JSONObject responseJson = null;
		try {
			responseJson = regionDao.createNormalizedRegionTitles(new JSONObject(normalizedregiontitles));
		} catch (Exception ex) {
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/deleteBranch/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(int id) {
		try {
			Branch branch = new Branch(id);
			regionDao.delete(branch);
		} catch (Exception ex) {
			return "Error deleting the branch: " + ex.toString();
		}
		return "TblBranch succesfully deleted!";
	}

	
	@RequestMapping(value = "/deleteEdit/{editId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteEdit(@PathVariable int editId) {
		JSONObject responseJson = null;
		try {
			responseJson = regionDao.deleteEdit(editId);
		} catch (Exception ex) {
			return "Error deleting the Edit: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(method = RequestMethod.GET, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllRegions(@RequestParam(required = true, value = "key") String key,
			@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("key", key);
			inputJson.put("value", value);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;

			branchJson = regionDao.getAll(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/maps",method = RequestMethod.GET, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllMappedRegions(@RequestParam(required = true, value = "key") String key,
			@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("key", key);
			inputJson.put("value", value);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;

			branchJson = regionDao.getAllMapTitles(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/titles", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllRegionTitles(@RequestParam(required = true, value = "key") String key,
			@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("key", key);
			inputJson.put("value", value);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.getLoadRegionTitles(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/boilerplates", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getBoilerPlates(@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("value", value);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadBoilerplates(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/brnachboilerplates", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public ResponseEntity<String> saveBranchBoilerplate(@RequestBody String request,
			@RequestParam(required = false, value = "branchId") Integer branchId) {
		JSONObject responseJson = new JSONObject();
		try {
			JSONObject inputObj = new JSONObject(request);
			JSONArray inputJsonArray = new JSONArray().put(inputObj);
			responseJson = regionDao.createBranchBoilerplate(inputJsonArray, branchId);

		} catch (Exception ex) {
			ex.printStackTrace();
			responseJson.put("message", "Failed To create");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/brnachboilerplates", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public ResponseEntity<String> getBranchBoilerplate(
			@RequestParam(required = false, value = "branchId") Integer branchId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = regionDao.getBranchBoilerplates(branchId, page, limit);

		} catch (Exception ex) {
			ex.printStackTrace();
			responseJson.put("message", "Failed To Get data");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
	}

		@RequestMapping(value = "/deleteEditBoilerPlate/{boilerPlateRegionId}", method = RequestMethod.DELETE)
		@ResponseBody
		public ResponseEntity<String> deleteBranchBoilerplateRegion(@PathVariable int boilerPlateRegionId) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson = regionDao.deleteBoilerplatesRegion(boilerPlateRegionId);
		
		} catch (Exception ex) {
			ex.printStackTrace();
			responseJson.put("message", "Failed To Get data");
			return new ResponseEntity<>(responseJson.toString(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
	}

	
	@RequestMapping(value = "/regionusesbynid", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getRegionUsesByNcid(
			@RequestParam(required = true, value = "normalizedRegionId") Integer normalizedRegionId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("normalizedRegionId", normalizedRegionId);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadRegionUsesByNormalizedId(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/normalizedtitles", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllNRegionTitles(@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "value") String value,
			@RequestParam(required = false, value = "normalizedRegionId") String normalizedRegionId,

			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();

			if (normalizedRegionId != null) {
				inputJson.put("normalizedRegionId", normalizedRegionId);
			} else {

				if (key != null) {
					inputJson.put("key", key);
				}

				if (value != null) {
					inputJson.put("value", value);
				}
			}

			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadNormalizedRegionTitles(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/uses", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getAllRegionUses(@RequestParam(required = true, value = "key") String key,
			@RequestParam(required = true, value = "value") String value,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("key", key);
			inputJson.put("value", value);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadRegionUses(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/edits", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getEdits(@RequestParam(required = true, value = "branchId") Integer branchId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("branchId", branchId);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadEdit(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/editsbybranch", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getEditsByBranch(@RequestParam(required = true, value = "branchId") Integer branchId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("branchId", branchId);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadEditByBranch(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("Failed while executing the branch", ex);
			return ex.getMessage();
		}
		return branchJson.toString();
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
			responseJson = regionDao.updateTestById(jsObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson.toString();
	}

	
	@RequestMapping(value = "/regionusesbyid", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String loadRegionUsesById(@RequestParam(required = true, value = "regionUseId") Integer regionUseId,
			@RequestParam(required = false, value = "page") Integer page,
			@RequestParam(required = false, value = "limit") Integer limit) {
		JSONObject branchJson = null;
		try {
			JSONObject inputJson = new JSONObject();
			inputJson.put("regionUseId", regionUseId);
			inputJson.put("page", page > 0 ? page : 1);
			inputJson.put("limit", limit > 0 ? limit : 10);// default limit 10;
			branchJson = regionDao.loadRegionUsesById(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/editsbyids", method = RequestMethod.POST, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String getEditById(@RequestBody String edits) {
		JSONObject editRegionsJson = null;
		try {
			JSONArray inputJsonArray = new JSONArray(edits);
			editRegionsJson = regionDao.loadEditByIds(inputJsonArray);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return editRegionsJson.toString();
	}

	
	@RequestMapping(value = "/branchedits/{editId}", method = RequestMethod.PUT, produces = { "application/json" })
	@ResponseBody
	public String updateBranchEdits(@PathVariable int editId, @RequestBody String body) {
		JSONObject branchJson = null;
		try {

			JSONArray jsonArray = new JSONArray(body);

			JSONObject inputJson = jsonArray.getJSONObject(0);
			inputJson.put("editId", editId);
			branchJson = regionDao.updateBranchEdit(inputJson);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return branchJson.toString();
	}

	
	@RequestMapping(value = "/submitForTest", method = RequestMethod.POST, produces = {
			"text/plain" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String submitForTest(@RequestBody String testRequest) {
		String responseJson = "Submitted Successfully";
		try {

			JSONObject requstJson = new JSONObject(testRequest);
			JSONArray titleArray = requstJson.getJSONArray("titles");
			/*TestRequestModelBuilder testBuilder = new TestRequestModelBuilder(titleArray,
					requstJson.getString("document"));
			testBuilder.buildModel();
			String testJson = testBuilder.getTestModelJson();
*/
			String testSumbitUrl = env.getProperty("test.submitUrl");
			RestClient restClient = new RestClient();
			responseJson = restClient.post(testSumbitUrl, "{}");
			responseJson = restClient.get(testSumbitUrl + "/" + 1);

		} catch (Exception ex) {
			return "{\"Error submitting to the Test\" : \"" + ex.getClass().getName() + "\"}";
		}
		return responseJson;
	}

	
	@RequestMapping(value = "/submitForTest/{testId}", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	@ResponseBody
	public String checkTestSubmitStatus(@PathVariable int testId) {
		String responseJson = "Submitted Successfully";
		try {
			String testSumbitUrl = env.getProperty("test.submitUrl");
			RestClient restClient = new RestClient();
			responseJson = restClient.get(testSumbitUrl + testId);
		} catch (Exception ex) {
			return "Error creating the branch: " + ex.toString();
		}
		return responseJson;
	}

	// Wire the TblBranchDao used inside this controller.
	@Autowired
	private RegionsDao regionDao;

	@Autowired
	private Environment env;

}
