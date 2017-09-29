package com.wisdom.common.dao;

import static com.wisdom.common.rest.util.Constants.ACTION_DELETE;
import static com.wisdom.common.rest.util.Constants.ACTION_EDIT;
import static com.wisdom.common.rest.util.Constants.ADDED_SUCCESS;
import static com.wisdom.common.rest.util.Constants.BOILERPLATE_TYPE;
import static com.wisdom.common.rest.util.Constants.DICTIONARIES_TYPE;
import static com.wisdom.common.rest.util.Constants.GENERAL_TYPE;
import static com.wisdom.common.rest.util.Constants.NO_DATA_FOUND;
import static com.wisdom.common.rest.util.Constants.REGIONS_TYPE;
import static com.wisdom.common.rest.util.Constants.RULES_TYPE;
import static com.wisdom.common.rest.util.Constants.STATUS_ACTIVE;
import static com.wisdom.common.rest.util.Constants.STATUS_INACTIVE;
import static com.wisdom.common.rest.util.Constants.STATUS_MERGED;
import static com.wisdom.common.rest.util.Constants.STATUS_NOTTESTED;
import static com.wisdom.common.rest.util.Constants.STATUS_READY_TO_MERGE;
import static com.wisdom.common.rest.util.Constants.STATUS_TESTED;
import static com.wisdom.common.rest.util.Constants.UPDATED_SUCCESS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.common.domain.Audit;
import com.wisdom.common.domain.Branch;
import com.wisdom.common.domain.BranchMap;
import com.wisdom.common.domain.BranchMapKey;
import com.wisdom.common.domain.Release;
import com.wisdom.common.domain.ResponseStatus;
import com.wisdom.common.domain.User;
import com.wisdom.common.httpclient.RestClient;
import com.wisdom.dictionaries.rest.domain.Dictionary;
import com.wisdom.dictionaries.rest.domain.Edit;
import com.wisdom.dictionaries.rest.domain.Property;
import com.wisdom.dictionaries.rest.domain.PropertyValue;
import com.wisdom.dictionaries.rest.domain.Variant;
import com.wisdom.dictionaries.rest.domain.VariantPropertyValue;
import com.wisdom.dictionaries.rest.domain.VariantString;
import com.wisdom.dictionaries.test.model.DictionaryDelta;
import com.wisdom.region.rest.domain.BoilerplateRegionBranch;
import com.wisdom.region.rest.domain.NormalizedRegionTitle;
import com.wisdom.region.rest.domain.NormalizedRegionTitleBranch;
import com.wisdom.region.rest.domain.NormalizedRegionTitleUse;
import com.wisdom.region.rest.domain.NormalizedRegionTitleUseBranch;
import com.wisdom.region.rest.domain.RegionEdit;
import com.wisdom.region.rest.domain.RegionEditMapping;
import com.wisdom.region.rest.domain.RegionTitle;
import com.wisdom.region.rest.domain.RegionTitleBranch;
import com.wisdom.region.rest.domain.RegionUse;
import com.wisdom.region.rest.domain.RegionUseBranch;
import com.wisdom.region.test.model.Adhoc;
import com.wisdom.region.test.model.BoilerplateRegion;
import com.wisdom.region.test.model.NormalizedTitle2UseMapping;
import com.wisdom.region.test.model.RegionRules;
import com.wisdom.region.test.model.RegionRulesDelta;
import com.wisdom.region.test.model.TestData;
import com.wisdom.region.test.model.TestRequestModel;
import com.wisdom.region.test.model.Title2NormalizedTitleMapping;

@Repository
@Transactional
public class BranchDao {

	/**
	 * Save the branch in the database.
	 */
	public JSONObject create(JSONObject json) {
		JSONObject output = new JSONObject().put("message", ADDED_SUCCESS);

		try {
			BranchMap branchMap = null;
			// HQL example - Create Branch

			List<Object> branchResultList = getQueryOutput("from Branch where name = :name ", new String[] { "name" },
					new Object[] { json.getString("name") });
			if (branchResultList.size() == 0) {

				Branch branch = new Branch();

				branch.setUser(new User(json.getInt("userId")));
				branch.setName(json.getString("name"));
				branch.setType(GENERAL_TYPE);
				Date createdDate = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gmt = new Date(sdf.format(createdDate));
				branch.setCreatedOn(gmt);
				branch.setStatus(getStatusCode(STATUS_ACTIVE));

				entityManager.persist(branch);

				Query query = entityManager.createNativeQuery("SELECT max(branchMapId) FROM tblBranchMap");
				Integer maxBranchMapId = (Integer) query.getSingleResult();
				if (maxBranchMapId == null) {
					maxBranchMapId = 0;
				}
				maxBranchMapId = maxBranchMapId + 1;

				branchMap = new BranchMap();
				branchMap.setId(new BranchMapKey((int) maxBranchMapId, branch.getBranchId()));
				entityManager.persist(branchMap);

				branch = new Branch();
				branch.setUser(new User(json.getInt("userId")));
				branch.setName(json.getString("name"));
				branch.setType(RULES_TYPE);
				createdDate = new Date();
				sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				gmt = new Date(sdf.format(createdDate));
				branch.setCreatedOn(gmt);
				branch.setStatus(getStatusCode(STATUS_ACTIVE));

				entityManager.persist(branch);
				branchMap = new BranchMap();
				branchMap.setId(new BranchMapKey((int) maxBranchMapId, branch.getBranchId()));
				entityManager.persist(branchMap);

				branch = new Branch();
				branch.setUser(new User(json.getInt("userId")));
				branch.setName(json.getString("name"));
				branch.setType(REGIONS_TYPE);
				createdDate = new Date();
				sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				gmt = new Date(sdf.format(createdDate));
				branch.setCreatedOn(gmt);
				branch.setStatus(getStatusCode(STATUS_ACTIVE));

				entityManager.persist(branch);
				branchMap = new BranchMap();
				branchMap.setId(new BranchMapKey((int) maxBranchMapId, branch.getBranchId()));
				entityManager.persist(branchMap);

				branch = new Branch();
				branch.setUser(new User(json.getInt("userId")));
				branch.setName(json.getString("name"));
				branch.setType(DICTIONARIES_TYPE);
				createdDate = new Date();
				sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				gmt = new Date(sdf.format(createdDate));
				branch.setCreatedOn(gmt);
				branch.setStatus(getStatusCode(STATUS_ACTIVE));

				entityManager.persist(branch);
				branchMap = new BranchMap();
				branchMap.setId(new BranchMapKey((int) maxBranchMapId, branch.getBranchId()));
				entityManager.persist(branchMap);

				// output.put("branchId", branch.getBranchId());
				// output.put("releaseId", newRelease.getReleaseId());
			} else {
				output.put("message", "Branch name is already exists");
			}

		} catch (Exception e) {
			e.printStackTrace();
			output.put("message", e.getMessage());
		}
		return output;

	}

	public JSONObject updateTestById(JSONObject json) {

		boolean isAvailable = false;
		JSONObject output = new JSONObject();
		TestRequestModel testInput = new TestRequestModel();
		// JSONObject testInput = new JSONObject();
		// testInput.put("regionRulesDelta", JSONObject.NULL);
		// testInput.put("dictionaryDelta", new JSONArray());

		/*
		 * String type = json.getString("type"); json.remove("type");
		 */
		try {
			Integer testId = json.getInt("testId");
			JSONObject dictJson = json.getJSONObject(DICTIONARIES_TYPE);
			JSONObject regionJson = json.getJSONObject(REGIONS_TYPE);
			JSONObject boilerplateJson = json.getJSONObject(BOILERPLATE_TYPE);

			List dict_workspaceIds = dictJson.getJSONArray("workspaceIds").toList();
			List region_workspaceIds = regionJson.getJSONArray("workspaceIds").toList();
			List boilerplate_workspaceIds = boilerplateJson.getJSONArray("workspaceIds").toList();

			ObjectMapper mapper = new ObjectMapper();
			String testResult = "{}";// engine.testConstraintPath(json.toString());
			Integer dictbranchId = -1;
			Integer regbranchId = -1;
			Integer boibranchId = -1;

			if (dict_workspaceIds.size() > 0) {
				List<Object> editPropList = getQueryOutput(
						"from Edit where editId in (:editId) and (status = 1 OR status = 2) ",
						new String[] { "editId" }, new Object[] { dict_workspaceIds });
				// List<DictionaryDelta> dictDeltas = new
				// ArrayList<DictionaryDelta>();
				for (Iterator editPropItr = editPropList.iterator(); editPropItr.hasNext();) {
					Edit editObject = (Edit) editPropItr.next();
					JSONObject editMetaJson = new JSONObject(editObject.getMetadata());
					DictionaryDelta dictionaryDelta = new DictionaryDelta();
					if (dictbranchId == -1) {
						dictbranchId = editObject.getBranch().getBranchId();
					}

					String action = "ADD";
					if (getEditedActionStr(editObject.getAction()).toUpperCase().equals("DELETE")) {
						action = "DELETE";
					}
					dictionaryDelta.setAction(action);
					dictionaryDelta.setType(editMetaJson.getString("dictionary"));
					dictionaryDelta.setVarId(editMetaJson.getInt("variantId"));
					dictionaryDelta.setVarStr(editMetaJson.getString("varString"));
					HashMap propMap = new HashMap();
					propMap.put("cui", editMetaJson.getString("cui"));
					propMap.put("ncid", editMetaJson.getString("ncid"));
					propMap.put("tui", editMetaJson.getString("tui"));
					dictionaryDelta.setProps(propMap);
					testInput.getDictionaryDelta().add(dictionaryDelta);
					isAvailable = true;

				}
				// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dictDeltas);
				// String dictDataStr = mapper.writeValueAsString(dictDeltas);
				// testInput.put("dictionaryDelta", new JSONArray(dictDataStr));

				System.out.println(testInput);

			}
			System.out.println(testInput);
			RegionRulesDelta regionRulesDelta = new RegionRulesDelta();
			RegionRules newRules = new RegionRules();
			RegionRules retiredRules = new RegionRules();

			if (region_workspaceIds.size() > 0) {

				List<Object> rEditPropList = getQueryOutput("from RegionEdit where editId in (:editId) and (status = 1 OR status = 2)",
						new String[] { "editId" }, new Object[] { region_workspaceIds });

				boolean isNewRule = true;
				for (Iterator reditPropItr = rEditPropList.iterator(); reditPropItr.hasNext();) {
					RegionEdit editObject = (RegionEdit) reditPropItr.next();
					if (getEditedActionStr(editObject.getAction()).toUpperCase().equals("DELETE")) {
						isNewRule = false;
					}

					if (regbranchId == -1) {
						regbranchId = editObject.getFkBranchId();

					}
					// We need to check retired rules delete condition
					List<Object> rRegionPropList = getNativeQueryOutput(
							"SELECT fkRegionTitleId FROM tblRegionEditMapping trem  "
									+ "inner join tblRegionEdit tre on trem.fkEditId = tre.editId and tre.editId = "
									+ editObject.getEditId());

					List<Object> regionList = getNativeQueryOutput(
							"select trtb.regionTitleId as id,  trtb.title as regionTitle, tnrtb.title as NormalizedTitle, trtb.fkNormalizedRegionTitleId from tblRegionTitleBranch trtb "
									+ "inner join tblNormalizedRegionTitleBranch tnrtb on trtb.fkNormalizedRegionTitleId = tnrtb.normalizedRegionTitleId "
									+ "where trtb.regionTitleId =  " + rRegionPropList.get(0));

					for (Iterator iterator = regionList.iterator(); iterator.hasNext();) {
						Object[] object2 = (Object[]) iterator.next();
						Title2NormalizedTitleMapping normalizedTitleMapping = new Title2NormalizedTitleMapping();
						normalizedTitleMapping.setId(Integer.parseInt(String.valueOf(object2[0])));
						normalizedTitleMapping.setNormalizedTitle(String.valueOf(object2[2]));
						normalizedTitleMapping.setRawTitle(String.valueOf(object2[1]));
						if (isNewRule) {

							newRules.getTitle2NormalizedTitleMappings().add(normalizedTitleMapping);
						} else {
							retiredRules.getTitle2NormalizedTitleMappings().add(normalizedTitleMapping);
						}
						List<Object> regionUList = getNativeQueryOutput(
								"select distinct tnrtub.fkNormalizedRegionTitleId, tnrtb.title,trb.ncid  from tblNormalizedRegionTitleUseBranch tnrtub "
										+ "inner join tblNormalizedRegionTitleBranch tnrtb on tnrtub.fkNormalizedRegionTitleId = tnrtb.normalizedRegionTitleId "
										+ "and tnrtub.fkNormalizedRegionTitleId = " + object2[3]
										+ " inner join tblRegionUseBranch trb on tnrtub.fkRegionUseId = trb.regionuseId");

						for (Iterator regionUItr = regionUList.iterator(); regionUItr.hasNext();) {
							Object[] object3 = (Object[]) regionUItr.next();

							NormalizedTitle2UseMapping normalizedTitle2UseMapping = new NormalizedTitle2UseMapping();
							normalizedTitle2UseMapping.setId(Integer.parseInt(String.valueOf(object3[0])));
							normalizedTitle2UseMapping.setNcid(String.valueOf(object3[2]));
							normalizedTitle2UseMapping.setNormalizedTitle(String.valueOf(object3[1]));
							if (isNewRule) {
								newRules.getNormalizedTitle2UseMappings().add(normalizedTitle2UseMapping);
								regionRulesDelta.setNewRules(newRules);
								testInput.setRegionRulesDelta(regionRulesDelta);
								isAvailable = true;
							} else {
								retiredRules.getNormalizedTitle2UseMappings().add(normalizedTitle2UseMapping);
								regionRulesDelta.setRetiredRules(retiredRules);
								testInput.setRegionRulesDelta(regionRulesDelta);
								isAvailable = true;
							}

						}

					}

				}
			}

			if (boilerplate_workspaceIds.size() > 0) {

				List<Object> boilerplateList = getQueryOutput(
						"from BoilerplateRegionBranch where boilerplateRegionId in (:boilerplateRegionId) and (status = 1 OR status = 2)",
						new String[] { "boilerplateRegionId" }, new Object[] { boilerplate_workspaceIds });

				for (Iterator boilerplateItr = boilerplateList.iterator(); boilerplateItr.hasNext();) {
					BoilerplateRegionBranch boilerBrnch = (BoilerplateRegionBranch) boilerplateItr.next();

					if (boibranchId == -1) {
						boibranchId = boilerBrnch.getFkBranchId();

					}
					BoilerplateRegion boilerplate = new BoilerplateRegion();
					boilerplate.setId(boilerBrnch.getBoilerplateRegionId());
					// boilerplate.setEnterpriseId(String.valueOf(object4[0]));
					boilerplate.setStart(boilerBrnch.getStart()!=null && !"".equals(boilerBrnch.getStart().trim()) ? boilerBrnch.getStart(): null);
					boilerplate.setEnd(boilerBrnch.getEnd()!=null && !"".equals(boilerBrnch.getEnd().trim()) ? boilerBrnch.getEnd(): null);
					boilerplate.setWholePhrase(boilerBrnch.getWholePhrase()!=null && !"".equals(boilerBrnch.getWholePhrase().trim()) ? boilerBrnch.getWholePhrase(): null);
					boilerplate.setIsStartRegex(boilerBrnch.getStartRegex());
					//boilerplate.setEnd(boilerBrnch.getEnd());
					boilerplate.setIsEndRegex(boilerBrnch.getEndRegex());
					//boilerplate.setWholePhrase(boilerBrnch.getWholePhrase());
					boilerplate.setIsWholePhraseRegex(boilerBrnch.getWholePhraseRegex());
					boilerplate.setIsExactWhiteSpace(boilerBrnch.getExactWhitespace());
					if (getEditedActionStr(boilerBrnch.getAction()).toUpperCase().equals("DELETE")) {
						retiredRules.getBoilerplateRegions().add(boilerplate);
						regionRulesDelta.setRetiredRules(retiredRules);
						testInput.setRegionRulesDelta(regionRulesDelta);
						isAvailable = true;
					} else {
						newRules.getBoilerplateRegions().add(boilerplate);
						regionRulesDelta.setNewRules(newRules);
						testInput.setRegionRulesDelta(regionRulesDelta);
						isAvailable = true;
					}
				}

			}

			if (isAvailable) {

				testInput.setId(testId);
				TestData testData = new TestData();
				Adhoc adhoc = new Adhoc();
				adhoc.setDocument(json.getString("comments"));
				adhoc.setTestId(testId);
				List<Adhoc> adhocs = new ArrayList<Adhoc>();
				adhocs.add(adhoc);
				testData.setAdhoc(adhocs);

				testInput.setTestData(testData);

				String testSumbitUrl = env.getProperty("test.submitUrl");
				RestClient restClient = new RestClient();
				int statusCode = -1;
				System.out.println("testSumbitUrl" + testSumbitUrl);
				try {
					System.out.println("Test Input = " + mapper.writeValueAsString(testInput));
					testResult = restClient.post(testSumbitUrl, mapper.writeValueAsString(testInput));
					System.out.println("Test Result Status " + restClient.getStatus());
					try {
						TimeUnit.SECONDS.sleep(5);
						System.out.println(testSumbitUrl + "/" + testId);
						HttpClient httpClient = HttpClientBuilder.create().build();
						 
						// Create new getRequest with below mentioned URL
						HttpGet getRequest = new HttpGet(testSumbitUrl+"/"+testId);
						
			 
						// Add additional header to getRequest which accepts application/xml data
						getRequest.addHeader("accept", "application/json");
			 
						// Execute your request and catch response
						HttpResponse response = httpClient.execute(getRequest);
						
						// Get-Capture Complete application/xml body response
						BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
						String outputx;
						System.out.println("============Output:============");
			 
						testResult = "";
						// Simply iterate through XML response and show on console.
						while ((outputx = br.readLine()) != null) {
							testResult += (outputx);
						}
						System.out.println("Test Result  " + testResult);
						
						statusCode = response.getStatusLine().getStatusCode();
if(response.getStatusLine().getStatusCode() != 200) {
	throw new Exception("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());

						}
						
						
						//testResult = restClient.get(testSumbitUrl + "/" + testId);
						
			

						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					statusCode = Integer.parseInt(restClient.getStatus()+"");
					
					System.out.println(mapper.writeValueAsString(testInput));
					Query testUpdateQuery = entityManager.createQuery(
							"update Test set testInput = :testInput, testResult = :testResult where testId = :testId");
					testUpdateQuery.setParameter("testInput", mapper.writeValueAsString(testInput));

					testUpdateQuery.setParameter("testResult", testResult);
					testUpdateQuery.setParameter("testId", testId);
					json.put("id", testId);
					int isUpdated = testUpdateQuery.executeUpdate();
					if (isUpdated > 0) {

						
						if (dict_workspaceIds.size() > 0) {
							Query editQuery = entityManager.createQuery(
									"update Edit set lastUpdatedOn = :lastUpdatedOn , status = :status where editId in (:workspaceIds)");

							Date createdDate = new Date();
							DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							Date gmt = new Date(sdf.format(createdDate));

							editQuery.setParameter("lastUpdatedOn", gmt);
							editQuery.setParameter("status", getEditStatusCode(STATUS_TESTED));
							editQuery.setParameter("workspaceIds", dict_workspaceIds);

							isUpdated = editQuery.executeUpdate();
						}
						if (region_workspaceIds.size() > 0) {
							Query editQuery = entityManager.createQuery(
									"update RegionEdit set lastUpdatedOn = :lastUpdatedOn , status = :status where editId in (:workspaceIds)");
							Date createdDate = new Date();
							DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							Date gmt = new Date(sdf.format(createdDate));

							editQuery.setParameter("lastUpdatedOn", gmt);
							editQuery.setParameter("status", getEditStatusCode(STATUS_TESTED));
							editQuery.setParameter("workspaceIds", region_workspaceIds);

							isUpdated = editQuery.executeUpdate();
							output.put("message", UPDATED_SUCCESS);
							if (isUpdated > 0) {
								output.put("message", UPDATED_SUCCESS);
							} else {
								output.put("message", NO_DATA_FOUND + ", but Test was updated.");
							}
						}
						
						if (boilerplate_workspaceIds.size() > 0) {
							Query editQuery = entityManager.createQuery(
									"update BoilerplateRegionBranch set status = :status where boilerplateRegionId in (:workspaceIds)");
							
							
							editQuery.setParameter("status", getEditStatusCode(STATUS_TESTED));
							editQuery.setParameter("workspaceIds", boilerplate_workspaceIds);

							isUpdated = editQuery.executeUpdate();
							output.put("message", UPDATED_SUCCESS);
							if (isUpdated > 0) {
								output.put("message", UPDATED_SUCCESS);
							} else {
								output.put("message", NO_DATA_FOUND + ", but Test was updated.");
							}
						}
						output = new JSONObject(testResult);
						

						// testResult =
						// engine.testConstraintPathTextLog(json.toString());
						// logger.info("textOutput: " + testResult);
						// output.put("testResult", testResult);
					} else {
						output.put("message", NO_DATA_FOUND);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					//statusCode = 404;
				}
				
				Query responseStatusQuery = entityManager.createQuery("from ResponseStatus where statusCode = :statusCode ");
				
				responseStatusQuery.setParameter("statusCode", statusCode);
				List<ResponseStatus> responseCodeList = responseStatusQuery.getResultList();
				
				// List<Audit> audtList =
				Query testQuery = entityManager.createQuery("from Audit where fkTestId = :testId ");
				testQuery.setParameter("testId", testId);
				List<Audit> audList = testQuery.getResultList();
				int resStatusId = responseCodeList.get(0).getId();
				if (audList.size() > 0) {
					
					if (boibranchId > 0) {
						Query updateAuditQuery = entityManager.createQuery(
								"update Audit set comments = :comments , fkStatusId = :fkStatusId where fkTestId = :testId and fkBranchId = :branchId ");
						
						updateAuditQuery.setParameter("comments", json.get("comments"));
						updateAuditQuery.setParameter("fkStatusId", resStatusId);
						updateAuditQuery.setParameter("testId", testId);
						updateAuditQuery.setParameter("branchId", boibranchId);
						updateAuditQuery.executeUpdate();
						
					}
					
					if (regbranchId > 0) {
						Query updateAuditQuery = entityManager.createQuery(
								"update Audit set comments = :comments , fkStatusId = :fkStatusId where fkTestId = :testId and fkBranchId = :branchId ");
						
						updateAuditQuery.setParameter("comments", json.get("comments"));
						updateAuditQuery.setParameter("fkStatusId", resStatusId);
						updateAuditQuery.setParameter("testId", testId);
						updateAuditQuery.setParameter("branchId", regbranchId);
						updateAuditQuery.executeUpdate();
						
					}
					if (dictbranchId > 0) {
						Query updateAuditQuery = entityManager.createQuery(
								"update Audit set comments = :comments , fkStatusId = :fkStatusId where fkTestId = :testId and fkBranchId = :branchId ");
						
						updateAuditQuery.setParameter("comments", json.get("comments"));
						updateAuditQuery.setParameter("fkStatusId", resStatusId);
						updateAuditQuery.setParameter("testId", testId);
						updateAuditQuery.setParameter("branchId", dictbranchId);
						updateAuditQuery.executeUpdate();
						
					}
					// update
				} else {
					// insert
					if (boibranchId > 0) {
						Audit audit = new Audit();
						audit.setComments(json.getString("comments"));
						audit.setFkBranchId(boibranchId);
						audit.setFkStatusId(resStatusId);
						audit.setFkTestId(testId);
						entityManager.persist(audit);
						entityManager.flush();
						
					}
					if (regbranchId > 0) {
						Audit audit = new Audit();
						audit.setComments(json.getString("comments"));
						audit.setFkBranchId(regbranchId);
						audit.setFkStatusId(resStatusId);
						audit.setFkTestId(testId);
						entityManager.persist(audit);
						entityManager.flush();
						
					}
					if (dictbranchId > 0) {
						Audit audit = new Audit();
						audit.setComments(json.getString("comments"));
						audit.setFkBranchId(dictbranchId);
						audit.setFkStatusId(resStatusId);
						audit.setFkTestId(testId);
						entityManager.persist(audit);
						entityManager.flush();
						
					}
				}

				if (testResult == null) {
					testResult = "{}";
				}
				output.put("response",new JSONObject(testResult) );

			} else {
				output.put("message", NO_DATA_FOUND);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			output.put("message", e.getMessage());
		}

		return (output);
	}

	/**
	 * Delete the branch from the database.
	 */
	public void delete(Branch branch) {
		if (entityManager.contains(branch))
			entityManager.remove(branch);
		else
			entityManager.remove(entityManager.merge(branch));
		return;
	}

	/**
	 * Return all the branches stored in the database.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAll(JSONObject json, Boolean isReadyToMerge) {
		JSONObject responseJson = new JSONObject();

		List<JSONObject> branchList = new ArrayList<JSONObject>();
		List<JSONObject> branchTypeList = new ArrayList<JSONObject>();

		long startTime = System.currentTimeMillis();
		String lastUpdatedOn = "";

		try {
			String statusClause = "";
			if (isReadyToMerge) {
				statusClause = " and tb.status = 3 ";
			}
			String branchNameClause = "";

			List<Object> branchResultList = null;
			if (!json.isNull("branchName")) {
				branchNameClause = " and lower(tb.name) like lower('%" + json.getString("branchName") + "%') ";
			}

			String query = "select tb.name as branchName, tu.name as userName, tu.userId, tb.branchId, tb.type, tb.status,  "
					+ "	tb.createdOn, tb.lastUpdatedOn, "
					+ " ( SELECT COUNT(*) FROM tblEdit te where tb.branchId = te.fkBranchId ) AS eCount, "
					+ " ( SELECT COUNT(*) FROM tblRegionEdit tre where tb.branchId = tre.fkBranchId ) AS reCount, "
					+ " ( SELECT COUNT(*) FROM   tblBoilerplateRegionBranch tbrb where tb.branchId = tbrb.fkBranchId ) AS bCount "
					+ " from tblBranch tb " + " inner join tblUser tu on tb.fkUserId = tu.userId " + "where fkUserId = "
					+ json.getInt("userId") + statusClause + branchNameClause + " order by tb.createdOn desc ";

			branchResultList = getNativeQueryOutput(query);

			Integer entrySize = branchResultList.size();

			JSONObject branchNameJson = new JSONObject();
			// System.out.println("branchResultList.size : " +
			// branchResultList.size());
			for (Iterator iterator = branchResultList.iterator(); iterator.hasNext();) {
				Object[] brObject = (Object[]) iterator.next();
				lastUpdatedOn = "";
				if (brObject[7] != null) {
					lastUpdatedOn = brObject[7].toString();
				}

				String branchName = brObject[0].toString();
				if (!branchNameJson.isNull("name") && !branchNameJson.get("name").equals(branchName)) {
					// responseJson.put("branches", new JSONArray(branchList));
					branchList.add(branchNameJson);
					branchNameJson = new JSONObject();

					branchTypeList = new ArrayList<JSONObject>();
				}

				branchNameJson.put("name", branchName);
				branchNameJson.put("userName", brObject[1].toString());
				branchNameJson.put("userId", brObject[2]);

				JSONObject branchJson = new JSONObject();
				branchJson.put("branchId", Integer.parseInt(brObject[3].toString()));
				branchJson.put("type", brObject[4]);
				branchJson.put("status", getStatus((byte) brObject[5]));
				branchJson.put("createdOn", brObject[6].toString());
				branchJson.put("lastUpdatedOn", lastUpdatedOn);
				if (REGIONS_TYPE.equals(brObject[4])) {
					branchJson.put("count",
							Integer.parseInt(brObject[9].toString()) + Integer.parseInt(brObject[10].toString()));
				} else if (DICTIONARIES_TYPE.equals(brObject[4])) {
					branchJson.put("count", Integer.parseInt(brObject[8].toString()));
				} else {
					branchJson.put("count", 0);
				}
				// branchJson.put("releaseId",
				// branch.getRelease().getReleaseId());
				branchTypeList.add(branchJson);
				branchNameJson.put("types", new JSONArray(branchTypeList));

			}
			if (branchNameJson.toString() != null && !branchNameJson.toString().equals("{}")) {
				branchList.add(branchNameJson);
			}
			// System.out.println("branchNameJson : " +
			// branchNameJson.toString());
			// System.out.println("branchList : " + branchList);
			/*
			 * if(branchList.size() == 0) { if(!branchNameJson.isNull("name")){
			 * branchList.add(branchNameJson); } }
			 */
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Branch").put("count", branchList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("branches", new JSONArray(branchList));

			System.out.println("responseJson :: " + responseJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			responseJson.put("message", e.getMessage());
		}

		return responseJson;
		// return entityManager.createQuery("from Branch").getResultList();
	}

	/**
	 * Return the branch having the passed email.
	 */
	public Branch getByEmail(String email) {
		return (Branch) entityManager.createQuery("from Branch where email = :email").setParameter("email", email)
				.getSingleResult();
	}

	/**
	 * Return the branch having the passed id.
	 */
	public Branch getById(long id) {
		return entityManager.find(Branch.class, id);
	}

	/**
	 * Update the passed branch in the database.
	 */
	public JSONObject updateBranchById(JSONObject json) {
		JSONObject region_output = new JSONObject();
		JSONObject dict_output = new JSONObject();
		JSONObject output = new JSONObject();

		try {

			Integer branchId = json.getInt("branchId");

			String query = "select distinct branch.branchId,branch.branchName, editId,metadata from (select tbm.branchId,tb.name as branchName from   (select tbm.branchMapId from tblBranch tb  "
					+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId   where tbm.branchId = " + branchId
					+ " and tb.status !=3 and tb.status !=4 ) output  "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId  "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + DICTIONARIES_TYPE
					+ "') branch " + "inner join tblEdit te on branch.branchId = te.fkBranchId and te.status = 2 ";
			Map<String, JSONObject> cuiMaps = new HashMap<String, JSONObject>();

			List<Object> dicBranchList = getNativeQueryOutput(query);

			for (Iterator editItr = dicBranchList.iterator(); editItr.hasNext();) {
				Object[] objarr = (Object[]) editItr.next();
				JSONObject editJsonMeta = new JSONObject(objarr[3].toString());
				String tmpCui = editJsonMeta.getString("cui");
				if (cuiMaps.containsKey(tmpCui)) {
					JSONObject keyJson = new JSONObject();
					keyJson.put("editId", objarr[2]);
					keyJson.put("cui", editJsonMeta.getString("cui"));
					keyJson.put("ncid", editJsonMeta.getString("ncid"));
					keyJson.put("tui", editJsonMeta.getString("tui"));
					keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
					keyJson.put("varString", editJsonMeta.getString("varString"));
					keyJson.put("branchId", Integer.parseInt(objarr[0].toString()));
					keyJson.put("branchName", objarr[1]);
					// keyJson.put("username", branch.getUser().getName());

					List<Object> regBranchList = getNativeQueryOutput(
							"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
									+ "where tb.branchId = " + Integer.parseInt(objarr[0].toString()) + " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
									+ REGIONS_TYPE + "' ");
					keyJson.put("regionBranchId", (Integer) regBranchList.get(0));

					JSONObject conflictJson = new JSONObject();
					List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
					JSONObject cuiMapDictJson = cuiMaps.get(tmpCui);
					regBranchList = getNativeQueryOutput(
							"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
									+ "where tb.branchId = " + cuiMapDictJson.getInt("branchId") + " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
									+ REGIONS_TYPE + "' ");

					cuiMapDictJson.put("regionBranchId", (Integer) regBranchList.get(0));
					conflictsArray.add(keyJson);
					conflictsArray.add(cuiMapDictJson);
					conflictJson.put("conflicts", conflictsArray);
					conflictJson.put("message",
							"Error: CUI (#" + tmpCui + ") Conflict in Branch 1 " + keyJson.getString("branchName")
									+ " and Branch 2 " + cuiMaps.get(tmpCui).getString("branchName"));
					dict_output.put("error_conflict", conflictJson);
					// break;
					throw new Exception("Conflicted");
				} else {
					JSONObject keyJson = new JSONObject();
					keyJson.put("editId", objarr[2]);
					keyJson.put("cui", editJsonMeta.getString("cui"));
					keyJson.put("ncid", editJsonMeta.getString("ncid"));
					keyJson.put("tui", editJsonMeta.getString("tui"));
					keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
					keyJson.put("varString", editJsonMeta.getString("varString"));
					keyJson.put("branchId", Integer.parseInt(objarr[0].toString()));
					keyJson.put("branchName", objarr[1]);
					// keyJson.put("username", branch.getUser().getName());
					cuiMaps.put(tmpCui, keyJson);
				}
			}
			query = "select distinct branch.branchId,branch.branchName, editId,metadata from (select tbm.branchId, tb.name as branchName from  "
					+ "(select tbm.branchMapId from tblBranch tb  "
					+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId   where tb.status =3 ) output  "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId  "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + DICTIONARIES_TYPE
					+ "') branch "
					+ "inner join tblEdit te on branch.branchId = te.fkBranchId and te.status = 2 and branch.branchId != "
					+ branchId;
			dicBranchList = getNativeQueryOutput(query);

			JSONArray dict_warnArray = new JSONArray();

			for (Iterator editItr = dicBranchList.iterator(); editItr.hasNext();) {
				Object[] objarr = (Object[]) editItr.next();
				JSONObject editJsonMeta = new JSONObject(objarr[3].toString());
				String tmpCui = editJsonMeta.getString("cui");
				if (cuiMaps.containsKey(tmpCui)) {
					if (((JSONObject) cuiMaps.get(tmpCui)).getInt("branchId") == Integer
							.parseInt(objarr[0].toString())) {
						continue;
					}
					JSONObject keyJson = new JSONObject();
					keyJson.put("editId", objarr[2]);
					keyJson.put("cui", editJsonMeta.getString("cui"));
					keyJson.put("ncid", editJsonMeta.getString("ncid"));
					keyJson.put("tui", editJsonMeta.getString("tui"));
					keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
					keyJson.put("varString", editJsonMeta.getString("varString"));
					keyJson.put("branchId", Integer.parseInt(objarr[0].toString()));
					keyJson.put("branchName", objarr[1]);
					List<Object> regBranchList = getNativeQueryOutput(
							"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
									+ "where tb.branchId = " + Integer.parseInt(objarr[0].toString()) + " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
									+ REGIONS_TYPE + "' ");
					keyJson.put("regionBranchId", (Integer) regBranchList.get(0));

					// keyJson.put("username", branch.getUser().getName());
					JSONObject conflictJson = new JSONObject();
					List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
					conflictsArray.add(keyJson);
					JSONObject cuiMapJson = cuiMaps.get(tmpCui);
					regBranchList = getNativeQueryOutput(
							"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
									+ "where tb.branchId = " + cuiMapJson.getInt("branchId") + " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
									+ REGIONS_TYPE + "' ");
					cuiMapJson.put("regionBranchId", (Integer) regBranchList.get(0));

					conflictsArray.add(cuiMapJson);

					conflictJson.put("conflicts", conflictsArray);
					conflictJson.put("message",
							" CUI (#" + tmpCui + ") Conflict in Branch 1 " + keyJson.getString("branchName")
									+ " and Branch 2 " + cuiMaps.get(tmpCui).getString("branchName"));
					dict_warnArray.put(conflictJson);
					dict_output.put("warning_conflict", conflictJson);

				}
			}

			query = "select distinct branch.branchId,branch.branchName, tre.editId,trtb.mainRegionTitleId from "
					+ "(select tbm.branchId,tb.name as branchName from   (select tbm.branchMapId from tblBranch tb  "
					+ " inner join tblBranchMap tbm ON tb.branchId = tbm.branchId  " + "where tbm.branchId = "
					+ branchId + " and tb.status !=3 and tb.status !=4 ) output  "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId  "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + REGIONS_TYPE
					+ "') branch "
					+ "inner join tblRegionEdit tre on branch.branchId = tre.fkBranchId and tre.status = 2 "
					+ "inner join tblRegionEditMapping trem on tre.editId = trem.fkEditId "
					+ "inner join tblRegionTitleBranch trtb on trem.fkRegionTitleId = trtb.regionTitleId ";

			Map<String, JSONObject> regionIdMaps = new HashMap<String, JSONObject>();

			dicBranchList = getNativeQueryOutput(query);
			for (Iterator editItr = dicBranchList.iterator(); editItr.hasNext();) {
				Object[] objarr = (Object[]) editItr.next();
				if (objarr[3] != null) {

					String regionTitleId = objarr[3].toString();
					if (regionIdMaps.containsKey(regionTitleId)) {
						JSONObject keyJson = new JSONObject();
						keyJson.put("editId", objarr[2]);
						keyJson.put("regionTitleId", regionTitleId);
						keyJson.put("branchId", objarr[0]);
						keyJson.put("branchName", objarr[1]);
						// keyJson.put("username", branch.getUser().getName());
						List<Object> dict_BranchList = getNativeQueryOutput(
								"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
										+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
										+ "where tb.branchId = " + Integer.parseInt(objarr[0].toString()) + " ) output "
										+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
										+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
										+ DICTIONARIES_TYPE + "' ");
						keyJson.put("dictionaryBranchId", (Integer) dict_BranchList.get(0));

						JSONObject conflictJson = new JSONObject();
						List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
						JSONObject regionIdMapsJson = regionIdMaps.get(regionTitleId);
						dict_BranchList = getNativeQueryOutput(
								"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
										+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
										+ "where tb.branchId = " + regionIdMapsJson.getInt("branchId") + " ) output "
										+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
										+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
										+ DICTIONARIES_TYPE + "' ");
						regionIdMapsJson.put("dictionaryBranchId", (Integer) dict_BranchList.get(0));

						conflictsArray.add(keyJson);
						conflictsArray.add(regionIdMapsJson);
						conflictJson.put("conflicts", conflictsArray);
						conflictJson.put("message",
								"Error: RegionTitle (#" + regionTitleId + ") Conflict in Branch 1 "
										+ keyJson.getString("branchName") + " and Branch 2 "
										+ regionIdMaps.get(regionTitleId).getString("branchName"));
						region_output.put("error_conflict", conflictJson);

						// break;
						throw new Exception("Conflicted");
					} else {
						JSONObject keyJson = new JSONObject();
						keyJson.put("editId", objarr[2]);
						keyJson.put("regionTitleId", regionTitleId);
						keyJson.put("branchId", objarr[0]);
						keyJson.put("branchName", objarr[1]);
						// keyJson.put("username", branch.getUser().getName());
						regionIdMaps.put(regionTitleId, keyJson);
					}
				}

			}

			query = "select distinct branch.branchId,branch.branchName, tre.editId,trtb.mainRegionTitleId from "
					+ "(select tbm.branchId,tb.name as branchName from   (select tbm.branchMapId from tblBranch tb  "
					+ " inner join tblBranchMap tbm ON tb.branchId = tbm.branchId  " + "where  tb.status =3 ) output  "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId  "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + REGIONS_TYPE
					+ "') branch "
					+ "inner join tblRegionEdit tre on branch.branchId = tre.fkBranchId and tre.status = 2 "
					+ "inner join tblRegionEditMapping trem on tre.editId = trem.fkEditId "
					+ "inner join tblRegionTitleBranch trtb on trem.fkRegionTitleId = trtb.regionTitleId ";

			JSONArray region_warnArray = new JSONArray();
			dicBranchList = getNativeQueryOutput(query);
			for (Iterator editItr = dicBranchList.iterator(); editItr.hasNext();) {
				Object[] objarr = (Object[]) editItr.next();
				if (objarr[3] != null) {
					String tmpRegionId = objarr[3].toString();
					if (regionIdMaps.containsKey(tmpRegionId)) {

						if (((JSONObject) regionIdMaps.get(tmpRegionId)).getInt("branchId") == Integer
								.parseInt(objarr[0].toString())) {
							continue;
						}
						JSONObject keyJson = new JSONObject();
						keyJson.put("editId", objarr[2]);
						keyJson.put("regionTitleId", Integer.parseInt(objarr[3].toString()));
						keyJson.put("branchId", Integer.parseInt(objarr[0].toString()));
						keyJson.put("branchName", objarr[1]);
						List<Object> dict_BranchList = getNativeQueryOutput(
								"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
										+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
										+ "where tb.branchId = " + Integer.parseInt(objarr[0].toString()) + " ) output "
										+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
										+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
										+ DICTIONARIES_TYPE + "' ");
						keyJson.put("dictionaryBranchId", (Integer) dict_BranchList.get(0));

						JSONObject conflictJson = new JSONObject();
						JSONObject regionIdMapJson = regionIdMaps.get(tmpRegionId);
						dict_BranchList = getNativeQueryOutput(
								"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
										+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
										+ "where tb.branchId = " + regionIdMapJson.getInt("branchId") + " ) output "
										+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
										+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
										+ DICTIONARIES_TYPE + "' ");
						regionIdMapJson.put("dictionaryBranchId", (Integer) dict_BranchList.get(0));

						List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
						conflictsArray.add(keyJson);
						conflictsArray.add(regionIdMapJson);
						conflictJson.put("conflicts", conflictsArray);
						conflictJson.put("message",
								" RegionTitle (#" + tmpRegionId + ") Conflict in Branch 1 "
										+ keyJson.getString("branchName") + " and Branch 2 "
										+ regionIdMaps.get(tmpRegionId).getString("branchName"));
						region_warnArray.put(conflictJson);
						region_output.put("warning_conflict", conflictJson);
					}

				}
			}

			List<Object> dicRegBranchList = getNativeQueryOutput("select distinct tbm.branchId from "
					+ "(select tbm.branchMapId from tblBranch tb "
					+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId in " + "("
					+ branchId + ")) output " + "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and (tb.type = '" + DICTIONARIES_TYPE
					+ "' or tb.type = '" + REGIONS_TYPE + "') ");
			// System.out.println(dicBranchList.get(0));

			List<Object> editPropList = getQueryOutput(
					"select status from Edit where fkBranchId in (:branchId) group by status",
					new String[] { "branchId" }, new Object[] { dicRegBranchList });
			int totCount = editPropList.size();
			int testedCount = 0;
			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Byte statusByte = (Byte) editItr.next();
				if (STATUS_TESTED.equals(getEditStatus(statusByte))) {
					testedCount++;
				}
			}

			if (totCount != 0 && testedCount == totCount) {
				dict_output.put("message", "Tested");
			} else if (testedCount > 0 && testedCount < totCount) {
				dict_output.put("message", "Partially Tested");
			} else {
				dict_output.put("message", "Not Tested");
			}

			editPropList = getQueryOutput(
					"select status from RegionEdit where fkBranchId in (:branchId) group by status",
					new String[] { "branchId" }, new Object[] { dicRegBranchList });
			totCount = editPropList.size();
			testedCount = 0;
			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Byte statusByte = (Byte) editItr.next();
				if (STATUS_TESTED.equals(getEditStatus(statusByte))) {
					testedCount++;
				}
			}
			if (totCount != 0 && testedCount == totCount) {
				region_output.put("message", "Tested");
			} else if (testedCount > 0 && testedCount < totCount) {
				region_output.put("message", "Partially Tested");
			} else {
				region_output.put("message".toLowerCase(), "Not Tested");
			}

			Byte status = getStatusCode(json.getString("status"));

			Query branchUpdateQuery = entityManager
					.createQuery("update Branch set status = :status where branchId = :branchId");
			branchUpdateQuery.setParameter("status", status);
			branchUpdateQuery.setParameter("branchId", branchId);

			Integer isUpdated = branchUpdateQuery.executeUpdate();
			output.put(DICTIONARIES_TYPE.toLowerCase(), dict_output);
			output.put(REGIONS_TYPE.toLowerCase(), region_output);
			if (isUpdated > 0) {
				output.put("message", UPDATED_SUCCESS);
			} else {
				output.put("message", NO_DATA_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			output.put(DICTIONARIES_TYPE.toLowerCase(), dict_output);
			output.put(REGIONS_TYPE.toLowerCase(), region_output);

			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject moveRelease(JSONObject json) {

		JSONObject output = new JSONObject();

		JSONObject regionOutput = regionRelease(json);
		JSONObject dictOutput = dictRelease(json);
		output.put(DICTIONARIES_TYPE, dictOutput);
		output.put(REGIONS_TYPE, regionOutput);
		return (output);
	}

	private JSONObject regionRelease(JSONObject json) {

		JSONObject output = new JSONObject();
		try {
			String keyRelease = json.getString("key");
			List branchIdList = json.getJSONArray("branchIds").toList();
			String branchIds = branchIdList.toString().replace("[", "(").replace("]", ")");

			List<Object> regBranchList = getNativeQueryOutput("select tbm.branchId from "
					+ "(select tbm.branchMapId from tblBranch tb "
					+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId in " + branchIds
					+ " and tb.status = 3) output "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + REGIONS_TYPE + "' ");
			// System.out.println(dicBranchList.get(0));
			boolean isReleased = false;
			if (regBranchList.size() > 0) {
				List<Object> editPropList = getQueryOutput(
						"from RegionEdit where fkBranchId in (:branchIds) and status = :status ",
						new String[] { "branchIds", "status" },
						new Object[] { regBranchList, getEditStatusCode(STATUS_TESTED) });

				// List<Integer> variantIds = new ArrayList<Integer>();
				// List<String> cuiIds = new ArrayList<String>();
				Map<Integer, JSONObject> regionIdMaps = new HashMap<Integer, JSONObject>();
				Map<String, JSONObject> cuiMaps = new HashMap<String, JSONObject>();

				for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
					RegionEdit edit = (RegionEdit) editItr.next();
					List<Object> editMapPropList = getQueryOutput("from RegionEditMapping where id.fkEditId = :editId ",
							new String[] { "editId" }, new Object[] { edit.getEditId() });
					for (Iterator editMapItr = editMapPropList.iterator(); editMapItr.hasNext();) {
						RegionEditMapping regionEditMap = (RegionEditMapping) editMapItr.next();

						int tmpRegionId = regionEditMap.getId().getFkRegionTitleId();
						if (regionIdMaps.containsKey(tmpRegionId)) {
							List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
							JSONObject conflictJson = new JSONObject();
							conflictJson.put("conflicts", conflictsArray);
							conflictJson.put("message", "Warning: Variant (#" + tmpRegionId + ") Conflict");
							output = conflictJson;
							break;
							// throw new Exception("Conflicted");
						} else {
							JSONObject keyJson = new JSONObject();
							keyJson.put("editId", edit.getEditId());
							keyJson.put("regionTitleId", regionEditMap.getId().getFkRegionTitleId());
							regionIdMaps.put(tmpRegionId, keyJson);
						}
					}

				}
				List<Object> branchPropList = getQueryOutput("from Branch where branchId in (:branchIds) ",
						new String[] { "branchIds" }, new Object[] { regBranchList });

				Byte statusByte = Byte.parseByte("5");

				if (branchPropList.size() > 0) {

					for (Iterator branchItr = branchPropList.iterator(); branchItr.hasNext();) {
						Branch branch = (Branch) branchItr.next();

						int branchId = branch.getBranchId();
						editPropList = getQueryOutput(
								"from RegionEdit where fkBranchId = :branchId and status = :status ",
								new String[] { "branchId", "status" },
								new Object[] { branchId, getEditStatusCode(STATUS_TESTED) });
						for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
							RegionEdit edit = (RegionEdit) editItr.next();
							Integer editId = edit.getEditId();
							List<Object> editMapPropList = getQueryOutput(
									"from RegionEditMapping where id.fkEditId = :editId ", new String[] { "editId" },
									new Object[] { editId });
							for (Iterator editMapItr = editMapPropList.iterator(); editMapItr.hasNext();) {
								RegionEditMapping regionEditMap = (RegionEditMapping) editMapItr.next();

								int tmpRegionId = regionEditMap.getId().getFkRegionTitleId();
								List<Object> regionBraPropList = getQueryOutput(
										"from RegionTitleBranch where  regionTitleId = :regionTitleId ",
										new String[] { "regionTitleId" }, new Object[] { tmpRegionId });
								for (Iterator regionBPropItr = regionBraPropList.iterator(); regionBPropItr
										.hasNext();) {
									RegionTitleBranch regionTitleBranch = (RegionTitleBranch) regionBPropItr.next();
									Integer normId = regionTitleBranch.getFkNormalizedRegionTitleId();
									String title = regionTitleBranch.getTitle();
									if (ACTION_DELETE.equals(getEditedActionStr(edit.getAction()))) {
										Integer orginalRegionTitleId = regionTitleBranch.getMainRegionTitleId();
										Query rTitleUpdateQuery = entityManager.createQuery(
												"update RegionTitle set status = '0' where id = :regionTitleId ");
										rTitleUpdateQuery.setParameter("regionTitleId", orginalRegionTitleId);
										rTitleUpdateQuery.executeUpdate();
										// TODO add status merged

										isReleased = true;
										continue;
									}
									List<Object> regionPropList = null;
									if (ACTION_EDIT.equals(getEditedActionStr(edit.getAction()))) {
										Integer orginalRegionTitleId = regionTitleBranch.getMainRegionTitleId();
										regionPropList = getQueryOutput("from RegionTitle where  id = :regionTitleId ",
												new String[] { "regionTitleId" },
												new Object[] { orginalRegionTitleId });
									} else {
										regionPropList = new ArrayList<Object>();
										regionPropList.add(new RegionTitle());
									}

									for (Iterator regionPropItr = regionPropList.iterator(); regionPropItr.hasNext();) {
										RegionTitle rTitle = (RegionTitle) regionPropItr.next();
										rTitle.setTitle(title);
										List<Object> nRegionBPropList = getQueryOutput(
												"from NormalizedRegionTitleBranch where  normalizedRegionTitleId = :normalizedRegionTitleId ",
												new String[] { "normalizedRegionTitleId" }, new Object[] { normId });
										for (Iterator nregionBPropItr = nRegionBPropList.iterator(); nregionBPropItr
												.hasNext();) {
											NormalizedRegionTitleBranch nrtBranch = (NormalizedRegionTitleBranch) nregionBPropItr
													.next();
											String nrtBranchTitle = nrtBranch.getTitle();

											List<Object> nRegionPropList = null;

											if (rTitle.getFkNormalizedRegionTitleId() > 0) {
												nRegionPropList = getQueryOutput(
														"from NormalizedRegionTitle where  id = :normalizedRegionTitleId ",
														new String[] { "normalizedRegionTitleId" },
														new Object[] { rTitle.getFkNormalizedRegionTitleId() });
											} else {
												List<Object> nRegionPropListTmp = getQueryOutput(
														"from NormalizedRegionTitle where  title = :title ",
														new String[] { "title" }, new Object[] { nrtBranchTitle });
												nRegionPropList = new ArrayList();
												if (nRegionPropListTmp.size() > 0) {
													nRegionPropList.add(nRegionPropListTmp.get(0));
												} else {
													nRegionPropList.add(new NormalizedRegionTitle());
												}
											}
											for (Iterator nregionPropItr = nRegionPropList.iterator(); nregionPropItr
													.hasNext();) {
												NormalizedRegionTitle nrTitle = (NormalizedRegionTitle) nregionPropItr
														.next();

												if (nrTitle.getTitle() == null
														|| !nrTitle.getTitle().equals(nrtBranchTitle)) {
													NormalizedRegionTitle newNrTitle = new NormalizedRegionTitle();
													newNrTitle.setTitle(nrtBranchTitle);
													entityManager.persist(newNrTitle);
													entityManager.flush();
													rTitle.setFkNormalizedRegionTitleId(newNrTitle.getId());
													entityManager.persist(rTitle);
													entityManager.flush();

												} else {
													if (rTitle.getFkNormalizedRegionTitleId() == 0) {
														rTitle.setFkNormalizedRegionTitleId(nrTitle.getId());
													}
													entityManager.merge(rTitle);
													entityManager.flush();
												}

												List<Object> nRegionBUsePropList = getQueryOutput(
														"from NormalizedRegionTitleUseBranch where  fkNormalizedRegionTitleId = :normalizedRegionTitleId ",
														new String[] { "normalizedRegionTitleId" },
														new Object[] { normId });
												for (Iterator nRegionBUsePropItr = nRegionBUsePropList
														.iterator(); nRegionBUsePropItr.hasNext();) {
													NormalizedRegionTitleUseBranch nrtUseBranch = (NormalizedRegionTitleUseBranch) nRegionBUsePropItr
															.next();
													Integer nrtRegionUseBranchId = nrtUseBranch.getId()
															.getFkRegionUseId();
													List<Object> regionBUsePropList = getQueryOutput(
															"from RegionUseBranch where  regionUseId = :regionUseId ",
															new String[] { "regionUseId" },
															new Object[] { nrtRegionUseBranchId });
													for (Iterator regionBUsePropItr = regionBUsePropList
															.iterator(); regionBUsePropItr.hasNext();) {
														RegionUseBranch regionUseBranch = (RegionUseBranch) regionBUsePropItr
																.next();
														String branchNcid = regionUseBranch.getNcid();
														String branchDescription = regionUseBranch.getDescription();
														List<Object> regionUsePropList = getQueryOutput(
																"from RegionUse where  description = :description ",
																new String[] { "description" },
																new Object[] { branchDescription });
														if (regionUsePropList.size() > 0) {
															RegionUse regionUse = (RegionUse) regionUsePropList.get(0);
															List<Object> regionTitleUsePropList = getQueryOutput(
																	"from NormalizedRegionTitleUse where  fkNormalizedRegionTitleId = :normalizedRegionTitleId and fkRegionuseId = :regionUseId ",
																	new String[] { "normalizedRegionTitleId",
																			"regionUseId" },
																	new Object[] {
																			rTitle.getFkNormalizedRegionTitleId(),
																			regionUse.getId() });
															if (regionTitleUsePropList.size() == 0) {
																NormalizedRegionTitleUse nrtu = new NormalizedRegionTitleUse(
																		rTitle.getFkNormalizedRegionTitleId(),
																		regionUse.getId());
																entityManager.persist(nrtu);
																entityManager.flush();
															}
															// update

														} else {
															// insert
															RegionUse ruse = new RegionUse();
															ruse.setNcid(branchNcid);
															ruse.setDescription(branchDescription);
															entityManager.persist(ruse);
															entityManager.flush();
															NormalizedRegionTitleUse nrtu = new NormalizedRegionTitleUse(
																	rTitle.getFkNormalizedRegionTitleId(),
																	ruse.getId());
															entityManager.persist(nrtu);
															entityManager.flush();
														}
														isReleased = true;
													}

												}

											}

										}

									}

								}

							}
							if (isReleased) {
								Query editUpdateQuery = entityManager
										.createQuery("update RegionEdit set status = :status where editId = :editId");
								editUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
								editUpdateQuery.setParameter("editId", editId);
								Integer isUpdated = editUpdateQuery.executeUpdate();
							}

						}
						if (isReleased) {
							List<Object> gBranchList = getNativeQueryOutput(
									"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
											+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
											+ "where tb.branchId = " + branchId + ") output "
											+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
											+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
											+ GENERAL_TYPE + "' ");

							gBranchList.add((Integer) branchId);
							Query branchUpdateQuery = entityManager
									.createQuery("update Branch set status = :status where branchId in (:branchId)");
							branchUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
							branchUpdateQuery.setParameter("branchId", gBranchList);
							Integer isUpdated = branchUpdateQuery.executeUpdate();
						}
					}
				}

			}
			if (isReleased) {
				Query query = entityManager.createQuery(
						"SELECT release FROM Release release WHERE release.releaseId = (SELECT MAX(release2.releaseId) FROM Release release2)");
				List<Release> releaseOb = query.getResultList();
				Release releaseObj = null;
				Integer releaseMaxId = 0;
				if (releaseOb.size() > 0) {
					releaseObj = releaseOb.get(0);
					releaseMaxId = releaseObj.getReleaseId();
				} else {
					releaseObj = new Release();
					releaseObj.setMajor(Byte.parseByte("0"));
					releaseObj.setMinor(Byte.parseByte("0"));
					releaseObj.setPatch(Byte.parseByte("0"));
				}

				releaseMaxId++;

				Release release = new Release();
				release.setReleaseId(releaseMaxId);
				if (keyRelease.equals("patch")) {
					byte rPatch = releaseObj.getPatch();
					rPatch++;
					release.setMajor(releaseObj.getMajor());
					release.setMinor(releaseObj.getMinor());
					release.setPatch(rPatch);
				}
				if (keyRelease.equals("minor")) {
					byte rMinor = releaseObj.getMinor();
					rMinor++;
					release.setMajor(releaseObj.getMajor());
					release.setMinor(rMinor);
					release.setPatch(releaseObj.getPatch());
				}
				if (keyRelease.equals("major")) {
					byte rMajor = releaseObj.getMajor();
					rMajor++;
					release.setMajor(rMajor);
					release.setMinor(releaseObj.getMinor());
					release.setPatch(releaseObj.getPatch());
				}
				release.setComments(json.getString("comments"));
				entityManager.persist(release);
				entityManager.flush();

				if (output.isNull("message")) {
					output.put("message", "Successfully released");
				}

			} else {
				output.put("message", "No data found");
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			if (output.isNull("message")) {
				output.put("message", e.toString());
			}
		}

		return (output);
	}

	private JSONObject dictRelease(JSONObject json) {

		JSONObject output = new JSONObject();
		try {
			String keyRelease = json.getString("key");
			List branchIdList = json.getJSONArray("branchIds").toList();
			String branchIds = branchIdList.toString().replace("[", "(").replace("]", ")");

			List<Object> dicBranchList = getNativeQueryOutput(
					"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
							+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId in "
							+ branchIds + " and tb.status = 3) output "
							+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
							+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
							+ DICTIONARIES_TYPE + "' ");
			// System.out.println(dicBranchList.get(0));
			boolean isReleased = false;

			if (dicBranchList.size() > 0) {

				List<Object> editPropList = getQueryOutput(
						"from Edit where fkBranchId in (:branchIds) and status = :status ",
						new String[] { "branchIds", "status" },
						new Object[] { dicBranchList, getEditStatusCode(STATUS_TESTED) });

				// List<Integer> variantIds = new ArrayList<Integer>();
				// List<String> cuiIds = new ArrayList<String>();
				Map<Integer, JSONObject> variantIdMaps = new HashMap<Integer, JSONObject>();
				Map<String, JSONObject> cuiMaps = new HashMap<String, JSONObject>();
				for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
					Edit edit = (Edit) editItr.next();
					JSONObject editJsonMeta = new JSONObject(edit.getMetadata());
					if (!editJsonMeta.isNull("variantId") && (editJsonMeta.getInt("variantId") != -1)) {
						int tmpVariantId = editJsonMeta.getInt("variantId");
						if (variantIdMaps.containsKey(tmpVariantId)) {
							Branch branch = edit.getBranch();
							JSONObject keyJson = new JSONObject();
							keyJson.put("editId", edit.getEditId());
							keyJson.put("cui", editJsonMeta.getString("cui"));
							keyJson.put("ncid", editJsonMeta.getString("ncid"));
							keyJson.put("tui", editJsonMeta.getString("tui"));
							keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
							keyJson.put("varString", editJsonMeta.getString("varString"));
							keyJson.put("variantId", tmpVariantId);
							keyJson.put("branchId", branch.getBranchId());
							keyJson.put("branchName", branch.getName());
							keyJson.put("username", branch.getUser().getName());
							JSONObject conflictJson = new JSONObject();
							List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
							conflictsArray.add(keyJson);
							conflictsArray.add(variantIdMaps.get(tmpVariantId));
							conflictJson.put("conflicts", conflictsArray);
							conflictJson.put("message", "Warning: Variant (#" + tmpVariantId + ") Conflict");
							output = conflictJson;
							break;
							// throw new Exception("Conflicted");
						} else {
							Branch branch = edit.getBranch();
							JSONObject keyJson = new JSONObject();
							keyJson.put("editId", edit.getEditId());
							keyJson.put("cui", editJsonMeta.getString("cui"));
							keyJson.put("ncid", editJsonMeta.getString("ncid"));
							keyJson.put("tui", editJsonMeta.getString("tui"));
							keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
							keyJson.put("varString", editJsonMeta.getString("varString"));
							keyJson.put("variantId", tmpVariantId);
							keyJson.put("branchId", branch.getBranchId());
							keyJson.put("branchName", branch.getName());
							keyJson.put("username", branch.getUser().getName());
							variantIdMaps.put(tmpVariantId, keyJson);
						}
					} else {
						String tmpCui = editJsonMeta.getString("cui");
						if (cuiMaps.containsKey(tmpCui)) {
							Branch branch = edit.getBranch();
							JSONObject keyJson = new JSONObject();
							keyJson.put("editId", edit.getEditId());
							keyJson.put("cui", editJsonMeta.getString("cui"));
							keyJson.put("ncid", editJsonMeta.getString("ncid"));
							keyJson.put("tui", editJsonMeta.getString("tui"));
							keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
							keyJson.put("varString", editJsonMeta.getString("varString"));
							keyJson.put("branchId", branch.getBranchId());
							keyJson.put("branchName", branch.getName());
							keyJson.put("username", branch.getUser().getName());
							JSONObject conflictJson = new JSONObject();
							List<JSONObject> conflictsArray = new ArrayList<JSONObject>();
							conflictsArray.add(keyJson);
							conflictsArray.add(cuiMaps.get(tmpCui));
							conflictJson.put("conflicts", conflictsArray);
							conflictJson.put("message", "Error: CUI (#" + tmpCui + ") Conflict");
							output = conflictJson;
							throw new Exception("Conflicted");
						} else {
							Branch branch = edit.getBranch();
							JSONObject keyJson = new JSONObject();
							keyJson.put("editId", edit.getEditId());
							keyJson.put("cui", editJsonMeta.getString("cui"));
							keyJson.put("ncid", editJsonMeta.getString("ncid"));
							keyJson.put("tui", editJsonMeta.getString("tui"));
							keyJson.put("dictionary", editJsonMeta.getString("dictionary"));
							keyJson.put("varString", editJsonMeta.getString("varString"));
							keyJson.put("branchId", branch.getBranchId());
							keyJson.put("branchName", branch.getName());
							keyJson.put("username", branch.getUser().getName());
							cuiMaps.put(tmpCui, keyJson);
						}
					}
				}

				/*
				 * for (Iterator editItr = editPropList.iterator();
				 * editItr.hasNext();) { Edit edit = (Edit) editItr.next();
				 * JSONObject editJsonMeta = new JSONObject(edit.getMetadata());
				 * if (editJsonMeta.containsKey("variantId")) { int tmpVariantId
				 * = editJsonMeta.getInt("variantId"); if
				 * (variantIds.contains(tmpVariantId)) { throw new
				 * Exception("Variant "+tmpVariantId+" Conflict Warning"); }
				 * else { variantIds.add(tmpVariantId); } }else { String
				 * tmpCuiId = editJsonMeta.getString("cui"); if
				 * (cuiIds.contains(tmpCuiId)) { throw new
				 * Exception("CUI "+tmpCuiId+" Conflict Error"); } else {
				 * cuiIds.add(tmpCuiId); } }
				 * 
				 * }
				 */
				List<Object> branchPropList = getQueryOutput("from Branch where branchId in (:branchIds) ",
						new String[] { "branchIds" }, new Object[] { dicBranchList });

				Byte statusByte = Byte.parseByte("5");
				Integer releaseId = -1;
				if (branchPropList.size() > 0) {
					Query query = entityManager.createQuery(
							"SELECT release FROM Release release WHERE release.releaseId = (SELECT MAX(release2.releaseId) FROM Release release2)");
					List<Release> releaseOb = query.getResultList();
					Release releaseObj = null;
					Integer releaseMaxId = 0;
					if (releaseOb.size() > 0) {
						releaseObj = releaseOb.get(0);
						releaseMaxId = releaseObj.getReleaseId();
					} else {
						releaseObj = new Release();
						releaseObj.setMajor(Byte.parseByte("0"));
						releaseObj.setMinor(Byte.parseByte("0"));
						releaseObj.setPatch(Byte.parseByte("0"));
					}

					releaseMaxId++;

					Release release = new Release();
					release.setReleaseId(releaseMaxId);
					if (keyRelease.equals("patch")) {
						byte rPatch = releaseObj.getPatch();
						rPatch++;
						release.setMajor(releaseObj.getMajor());
						release.setMinor(releaseObj.getMinor());
						release.setPatch(rPatch);
					}
					if (keyRelease.equals("minor")) {
						byte rMinor = releaseObj.getMinor();
						rMinor++;
						release.setMajor(releaseObj.getMajor());
						release.setMinor(rMinor);
						release.setPatch(releaseObj.getPatch());
					}
					if (keyRelease.equals("major")) {
						byte rMajor = releaseObj.getMajor();
						rMajor++;
						release.setMajor(rMajor);
						release.setMinor(releaseObj.getMinor());
						release.setPatch(releaseObj.getPatch());
					}
					release.setComments(json.getString("comments"));

					for (Iterator branchItr = branchPropList.iterator(); branchItr.hasNext();) {
						Branch branch = (Branch) branchItr.next();

						int branchId = branch.getBranchId();
						editPropList = getQueryOutput("from Edit where fkBranchId = :branchId and status = :status ",
								new String[] { "branchId", "status" },
								new Object[] { branchId, getEditStatusCode(STATUS_TESTED) });
						for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
							Edit edit = (Edit) editItr.next();
							Integer editId = edit.getEditId();
							JSONObject editJsonMeta = new JSONObject(edit.getMetadata());
							String varString = editJsonMeta.getString("varString");
							String dictName = editJsonMeta.getString("dictionary");

							VariantString variantString = null;
							Integer dictId = -1;
							Integer varStringId = -1;

							if (ACTION_DELETE.equals(getEditedActionStr(edit.getAction()))) {
								Integer varId = editJsonMeta.getInt("variantId");
								Query variantUpdateQuery = entityManager
										.createQuery("update Variant set status = :status where id = :variantId ");
								variantUpdateQuery.setParameter("status", getStatusCode(STATUS_INACTIVE));
								variantUpdateQuery.setParameter("variantId", varId);
								variantUpdateQuery.executeUpdate();
								// TODO add status merged
								isReleased = true;
								continue;
							}

							List<Object> varStringList = getQueryOutput("from VariantString where value = :varString ",
									new String[] { "varString" }, new Object[] { varString });
							if (varStringList.size() == 0) {
								variantString = new VariantString();
								variantString.setValue(varString);
								entityManager.persist(variantString);
								entityManager.flush();
								varStringId = variantString.getId();
							} else {
								varStringId = ((VariantString) varStringList.get(0)).getId();
							}

							List<Object> dictList = getQueryOutput("from Dictionary where name = :name ",
									new String[] { "name" }, new Object[] { dictName });
							Dictionary dict = null;
							if (dictList.size() == 0) {
								dict = new Dictionary();
								dict.setName(dictName);
								entityManager.persist(dict);
								entityManager.flush();
								dictId = dict.getId();
							} else {
								dictId = ((Dictionary) dictList.get(0)).getId();
							}
							Dictionary updateDict = new Dictionary();
							updateDict.setId(dictId);

							VariantString updatevariantString = new VariantString();
							updatevariantString.setId(varStringId);

							Variant variant = new Variant();
							variant.setDictionary(updateDict);
							variant.setVariantstring(updatevariantString);

							if (!isReleased || releaseId == -1) {
								entityManager.persist(release);
								entityManager.flush();
								releaseId = release.getReleaseId();
							}
							variant.setRelease(release);
							variant.setComment("New Variant");
							variant.setStatus(statusByte);
							entityManager.persist(variant);
							entityManager.flush();

							editJsonMeta.remove("variantId");
							editJsonMeta.remove("varString");
							editJsonMeta.remove("type");
							editJsonMeta.remove("dictionary");
							editJsonMeta.remove("status");
							editJsonMeta.remove("action");

							isReleased = true;

							for (Iterator propItr = editJsonMeta.keys(); propItr.hasNext();) {
								String propKey = (String) propItr.next();

								List<Object> propList = getQueryOutput(
										"SELECT property from Property property where property.name = '" + propKey
												+ "' ");
								if (propList.size() > 0) {
									Integer propId = ((Property) propList.get(0)).getId();

									Property propObj = new Property();
									propObj.setId(propId);
									PropertyValue propValue = new PropertyValue();
									propValue.setProperty(propObj);
									propValue.setValue(editJsonMeta.getString(propKey));
									entityManager.persist(propValue);
									entityManager.flush();
									Integer propValueId = propValue.getId();
									Integer variantId = variant.getId();
									VariantPropertyValue variantPropertyValue = new VariantPropertyValue(variantId,
											propValueId);
									PropertyValue updatePropval = new PropertyValue();
									updatePropval.setId(propValueId);
									Variant updateVariant = new Variant();
									updateVariant.setId(variantId);
									variantPropertyValue.setPropertyValue(updatePropval);
									variantPropertyValue.setVariant(updateVariant);
									entityManager.persist(variantPropertyValue);
									entityManager.flush();

								}

							}

							Query editUpdateQuery = entityManager
									.createQuery("update Edit set status = :status where editId = :editId");
							editUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
							editUpdateQuery.setParameter("editId", editId);
							Integer isUpdated = editUpdateQuery.executeUpdate();

						}

						List<Object> gBranchList = getNativeQueryOutput(
								"select tbm.branchId from " + "(select tbm.branchMapId from tblBranch tb "
										+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId "
										+ "where tb.branchId = " + branchId + ") output "
										+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
										+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
										+ GENERAL_TYPE + "' ");

						gBranchList.add((Integer) branchId);
						Query branchUpdateQuery = entityManager
								.createQuery("update Branch set status = :status where branchId in (:branchId)");
						branchUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
						branchUpdateQuery.setParameter("branchId", gBranchList);
						Integer isUpdated = branchUpdateQuery.executeUpdate();

					}
				}
			}
			if (isReleased) {
				if (output.isNull("message")) {
					output.put("message", "Successfully released");
				}

			} else {
				output.put("message", "No data found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (output.isNull("message")) {
				output.put("message", e.toString());
			}
		}

		return (output);
	}

	@PersistenceContext
	private EntityManager entityManager;

	byte getEditStatusCode(String status) {
		if (STATUS_NOTTESTED.equalsIgnoreCase(status)) {
			return Byte.parseByte("1");
		} else if (STATUS_TESTED.equalsIgnoreCase(status)) {
			return Byte.parseByte("2");
		} else if (STATUS_INACTIVE.equalsIgnoreCase(status)) {
			return Byte.parseByte("3");
		} else {
			return 0;
		}

	}

	String getStatus(Byte status) {
		if (Byte.parseByte("1") == status) {
			return STATUS_ACTIVE;
		} else if (Byte.parseByte("2") == status) {
			return STATUS_INACTIVE;
		} else if (Byte.parseByte("3") == status) {
			return STATUS_READY_TO_MERGE;
		} else if (Byte.parseByte("4") == status) {
			return STATUS_MERGED;
		} else {
			return null;
		}

	}

	byte getStatusCode(String status) {
		if (STATUS_ACTIVE.equalsIgnoreCase(status)) {
			return Byte.parseByte("1");
		} else if (STATUS_INACTIVE.equalsIgnoreCase(status)) {
			return Byte.parseByte("2");
		} else if (STATUS_READY_TO_MERGE.equalsIgnoreCase(status)) {
			return Byte.parseByte("3");
		} else if (STATUS_MERGED.equalsIgnoreCase(status)) {
			return Byte.parseByte("4");
		} else {
			return 0;
		}

	}

	String getEditStatus(Byte status) {
		if (Byte.parseByte("1") == status) {
			return STATUS_NOTTESTED;
		} else if (Byte.parseByte("2") == status) {
			return STATUS_TESTED;
		} else if (Byte.parseByte("3") == status) {
			return STATUS_INACTIVE;
		} else {
			return null;
		}

	}

	private List<Object> getQueryOutput(String query, String[] params, Object[] values) {
		Query regionTitleQuery = entityManager.createQuery(query);
		for (int qIdx = 0; qIdx < params.length; qIdx++) {
			regionTitleQuery.setParameter(params[qIdx], values[qIdx]);
		}
		return regionTitleQuery.getResultList();
	}

	private List<Object> getQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createQuery(query);

		return regionTitleQuery.getResultList();
	}

	private List<Object> getNativeQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createNativeQuery(query);

		return regionTitleQuery.getResultList();
	}

	private String getEditedActionStr(int editAction) {
		String action = "Add";
		if (editAction == 2) {
			action = "Edit";

		} else if (editAction == 3) {
			action = "Delete";

		}
		return action;
	}

	@Autowired
	private Environment env;

}
