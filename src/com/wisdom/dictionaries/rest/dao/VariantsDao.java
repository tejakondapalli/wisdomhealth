package com.wisdom.dictionaries.rest.dao;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdom.common.domain.Branch;
import com.wisdom.common.domain.Release;
import com.wisdom.common.domain.Test;
import com.wisdom.common.httpclient.RestClient;
import com.wisdom.common.rest.util.CmpFileBuilder;
import com.wisdom.dictionaries.rest.domain.Dictionary;
import com.wisdom.dictionaries.rest.domain.Edit;
import com.wisdom.dictionaries.rest.domain.Property;
import com.wisdom.dictionaries.rest.domain.PropertyValue;
import com.wisdom.dictionaries.rest.domain.Variant;
import com.wisdom.dictionaries.rest.domain.VariantPropertyValue;
import com.wisdom.dictionaries.rest.domain.VariantString;

@Repository
@Transactional
public class VariantsDao {

	String ADDED_SUCCESS = "Added successfully";
	String DELETED_SUCCESS = "Deleted successfully";
	String UPDATED_SUCCESS = "Updated successfully";
	String CLONED_SUCCESS = "Cloned successfully";
	String NO_DATA_FOUND = "No data found!";
	String INVALID_ACTION = "Invalid action";
	String STATUS_ACTIVE = "Active";
	String STATUS_INACTIVE = "Discard";
	String STATUS_READY_TO_MERGE = "ReadyToMerge";
	String STATUS_MERGED = "Merged";
	String STATUS_TESTED = "Tested";
	String STATUS_NOTTESTED = "Not Tested";
	String ACTION_EDIT = "Edit";
	String ACTION_DELETE = "Delete";
	String ACTION_ADD = "Add";
	String RULES_TYPE = "Rules";
	String REGIONS_TYPE = "Regions";
	String DICTIONARIES_TYPE = "Dictionaries";
	String GENERAL_TYPE = "General";

	@PersistenceContext
	private EntityManager entityManager;

	static Logger logger = LoggerFactory.getLogger(VariantsDao.class);

	/**
	 * Save the branch in the database.
	 */
	public JSONObject create(JSONObject inputJson) {

		JSONObject output = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		// Prep work
		try {
			// HQL example - Create Variant

			if (!inputJson.isNull("variants")) {
				jsonArray = inputJson.getJSONArray("variants");
			} else {
				jsonArray.put(inputJson);
			}

			for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
				JSONObject json = (JSONObject) iterator.next();
				System.out.println(json);
				boolean proceed = false;
				if (json.isNull("editId") && ("Delete".equalsIgnoreCase(json.getString("action"))
						|| "Edit".equalsIgnoreCase(json.getString("action")))) {

					/*
					 * List<String> listOfValues = new ArrayList<String>();
					 * JSONObject metadata = json.getJSONObject("metadata");
					 * listOfValues.add(metadata.getString("cui"));
					 * listOfValues.add(metadata.getString("ncid"));
					 * listOfValues.add(metadata.getString("tui"));
					 * 
					 * List<Object> propvalues =
					 * getQueryOutput("from PropertyValue where value in (:value)"
					 * , new String[] { "value" }, new Object[] { listOfValues
					 * });
					 * 
					 * if (propvalues.size() >= 3) { proceed = true; } else {
					 * output.put("message", NO_DATA_FOUND); }
					 */
					proceed = true;
				} else if (!json.isNull("editId") || "Add".equalsIgnoreCase(json.getString("action"))) {
					/*
					 * if (!json.containsKey("editId")) {
					 * //json.remove("variantId"); }
					 */
					proceed = true;
				} else {
					output.put("message", INVALID_ACTION);
				}

				if (proceed) {
					Edit edit = new Edit();

					if (!json.isNull("editId")) {
						Integer editId = json.getInt("editId");
						json.remove("editId");
						if ("Delete".equalsIgnoreCase(json.getString("action"))
								|| "Edit".equalsIgnoreCase(json.getString("action"))
								|| "Add".equalsIgnoreCase(json.getString("action"))) {

							if ("Edit".equalsIgnoreCase(json.getString("action"))
									|| "Add".equalsIgnoreCase(json.getString("action"))) {

								Query editQuery = entityManager.createQuery(
										"update Edit set lastUpdatedOn = :lastUpdatedOn, status = :status, metadata = :metadata where editId = :editId");

								Date createdDate = new Date();
								DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
								Date gmt = new Date(sdf.format(createdDate));

								editQuery.setParameter("lastUpdatedOn", gmt);
								editQuery.setParameter("status", getEditStatusCode(STATUS_NOTTESTED));
								editQuery.setParameter("metadata", json.getJSONObject("metadata").toString());
								editQuery.setParameter("editId", editId);
								Integer isUpdated = editQuery.executeUpdate();
								if (isUpdated > 0) {
									output.put("message", UPDATED_SUCCESS);
								} else {
									output.put("message", NO_DATA_FOUND);
								}
							} else {
								Query editQuery = entityManager.createQuery("delete from Edit where editId = :editId");
								editQuery.setParameter("editId", editId);
								Integer isUpdated = editQuery.executeUpdate();
								if (isUpdated > 0) {
									output.put("message", DELETED_SUCCESS);
								} else {
									output.put("message", NO_DATA_FOUND);
								}
							}

						} else {
							output.put("message", "Invalid Status");
						}
					} else {
						Integer branchId = json.getInt("branchId");
						Query query = entityManager
								.createNativeQuery("SELECT fkTestId FROM tblEdit WHERE fkBranchId =" + branchId);
						List<Object> testIdObj = query.getResultList();
						int testId = -1;

						if (testIdObj.size() > 0) {
							testId  = Integer.parseInt(testIdObj.get(0).toString());
						}
						if (testId == -1) {
							List<Object> regBranchList = getNativeQueryOutput("select tbm.branchId from "
									+ "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId = " + branchId
									+ " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + REGIONS_TYPE + "' ");
							// System.out.println(dicBranchList.get(0));
							List<Object> editPropList = getQueryOutput(
									"from Edit where fkBranchId in (:branchIds) ",
									new String[] { "branchIds"},
									new Object[] { regBranchList });

							if(editPropList.size() > 0) {
								testId  = ((Edit)editPropList.get(0)).getTestId();
							}
						}

						json.remove("branchId");
						json.remove("testId");
						if (testId == -1) {
							query = entityManager.createQuery(
									"SELECT test FROM Test test WHERE test.testId = (SELECT MAX(test.testId) FROM Test test)");
							List<Object> testList = query.getResultList();
							Test testObj = null;
							Integer testmaxId = 1;
							if (testList.size() == 0) {
								testObj = new Test();
							} else {
								testObj = (Test) testList.get(0);
								testmaxId = testObj.getTestId() + 1;
							}

							Test newTest = new Test();
							newTest.setTestId(testmaxId);
							newTest.setEngineType(Byte.parseByte("1"));
							newTest.setTestInput("{}");
							newTest.setTestResult("{}");
							entityManager.persist(newTest);
							entityManager.flush();
							testId = testmaxId;

							/*
							 * Query branchQuery = session.
							 * createQuery("from Branch where branchId =:branchId "
							 * ); branchQuery.setLong("branchId", branchId);
							 * 
							 * List<Branch> branchResultList =
							 * branchQuery.list();
							 * 
							 * if (branchResultList.size() > 0) { Integer
							 * releaseId =
							 * branchResultList.get(0).getRelease().getReleaseId
							 * (); Release release = new Release();
							 * release.setReleaseId(releaseId);
							 * newTest.setRelease(release);
							 * entityManager.persist(newTest);
							 * entityManager.flush(); testId = testmaxId; } else
							 * { throw new Exception("Invalid ReleasId"); }
							 */

						}
						Date createdDate = new Date();
						DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
						Date gmt = new Date(sdf.format(createdDate));
						edit.setCreatedOn(gmt);
						edit.setTestId(testId);
						edit.setBranch(new Branch(branchId));
						edit.setStatus(getEditStatusCode(STATUS_NOTTESTED));
						edit.setMetadata(json.getJSONObject("metadata").toString());

						if ("Add".equalsIgnoreCase(json.getString("action"))) {
							edit.setAction(Byte.parseByte("1"));
							output.put("message", ADDED_SUCCESS);
						} else if ("Edit".equalsIgnoreCase(json.getString("action"))) {
							edit.setAction(Byte.parseByte("2"));
							output.put("message", UPDATED_SUCCESS);

						} else if ("Delete".equalsIgnoreCase(json.getString("action"))) {
							edit.setAction(Byte.parseByte("3"));
							output.put("message", DELETED_SUCCESS);

						}

						entityManager.persist(edit);
						entityManager.flush();

						output.put("testId", testId);
						output.put("editId", edit.getEditId());
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			output.put("message", e.getMessage());
		}
		return (output);

	}

	public JSONObject moveRelease(JSONObject json) {

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
			 * JSONObject editJsonMeta = new JSONObject(edit.getMetadata()); if
			 * (editJsonMeta.containsKey("variantId")) { int tmpVariantId =
			 * editJsonMeta.getInt("variantId"); if
			 * (variantIds.contains(tmpVariantId)) { throw new
			 * Exception("Variant "+tmpVariantId+" Conflict Warning"); } else {
			 * variantIds.add(tmpVariantId); } }else { String tmpCuiId =
			 * editJsonMeta.getString("cui"); if (cuiIds.contains(tmpCuiId)) {
			 * throw new Exception("CUI "+tmpCuiId+" Conflict Error"); } else {
			 * cuiIds.add(tmpCuiId); } }
			 * 
			 * }
			 */
			List<Object> branchPropList = getQueryOutput("from Branch where branchId in (:branchIds) ",
					new String[] { "branchIds" }, new Object[] { dicBranchList });

			Byte statusByte = Byte.parseByte("5");
			boolean isReleased = false;
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
						
						if(!isReleased || releaseId==-1){
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
									"SELECT property from Property property where property.name = '" + propKey + "' ");
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
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId = "
									+ branchId + ") output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '"
									+ GENERAL_TYPE + "' ");
					
					gBranchList.add((Integer)branchId);
					Query branchUpdateQuery = entityManager
							.createQuery("update Branch set status = :status where branchId in (:branchId)");
					branchUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
					branchUpdateQuery.setParameter("branchId", gBranchList);
					Integer isUpdated = branchUpdateQuery.executeUpdate();

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
	public JSONObject getAll(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> variantList = new ArrayList<JSONObject>();

		// Prep work
		try {
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			if ("varString".equals(json.getString("key"))) {
				query = "	select v.id as variantId, vs.value as varString, d.name as dictionary from tblVariant v"
						+ "	  INNER JOIN tblDictionary d on v.fkDictionaryId = d.id"
						+ "	  INNER JOIN tblVariantString vs on v.fkVariantStringId = vs.id and lower(vs.value) like lower('%"
						+ json.getString("value") + "%')"
						+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId" + "   WHERE v.status != 2"
						+ "	  group by v.id" + "	  order by v.id asc" + " limit " + offset + ", " + (limit + 5)
						+ ";";

			} else {

				query = "	select v.id, v.fkVariantStringId, v.fkDictionaryId from tblVariant v"
						+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId"
						+ "   INNER JOIN tblPropertyValue pv on vp.fkPropertyValueId = pv.id and lower(pv.value) = lower('"
						+ json.getString("value") + "')"
						+ "   INNER JOIN tblProperty p on pv.fkPropertyId = p.id and p.name= '" + json.getString("key")
						+ "'" + "   WHERE v.status != 2" + "	  group by v.id" + "	  order by v.id asc" + " limit "
						+ offset + ", " + (limit + 10) + ";";

			}
			List<Object> cuiPropList = getNativeQueryOutput(query);
			// pagination
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = cuiPropList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();
				String varString = "";
				String dictionary = "";

				if (!"varString".equals(json.getString("key"))) {
					List<Object> varList = getQueryOutput("from VariantString where id = :variantStringId",
							new String[] { "variantStringId" }, new Object[] { (Integer) object[1] });
					varString = ((VariantString) varList.get(0)).getValue();
					List<Object> dictList = getQueryOutput("from Dictionary where id = :dictionaryId",
							new String[] { "dictionaryId" }, new Object[] { (Integer) object[2] });
					dictionary = ((Dictionary) dictList.get(0)).getName();

				} else {
					varString = (String) object[1];
					dictionary = (String) object[2];

				}
				Integer variantId = (Integer) object[0];
				List<Object> entryPropVarList = getQueryOutput(
						"select propertyValue from VariantPropertyValue where fkVariantId = :fkVariantId ",
						new String[] { "fkVariantId" }, new Object[] { variantId });
				List<JSONObject> props = new ArrayList<JSONObject>();
				JSONObject propjson = new JSONObject();
				boolean isDiffCui = false;
				for (Object actualpropvalue : entryPropVarList) {
					String propName = ((PropertyValue) actualpropvalue).getProperty().getName();
					if (!"varString".equals(json.getString("key")) && propName.equals("cui") && !(json
							.getString("value").equalsIgnoreCase(((PropertyValue) actualpropvalue).getValue()))) {
						isDiffCui = true;
						break;
					}
					props.add(new JSONObject().put("name", propName).put("value",
							((PropertyValue) actualpropvalue).getValue()));
					propjson.put(propName, ((PropertyValue) actualpropvalue).getValue());

				}
				if (isDiffCui) {
					continue;
				}

				List<String> actions = new ArrayList<String>();
				actions.add("Add");
				actions.add("Delete");

				variantList.add(new JSONObject().put("varString", varString).put("dictionary", dictionary)
						.put("variantId", variantId).put("props", props).put("actions", actions));
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Variant").put("count", variantList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("variants", new JSONArray(variantList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);
	}

	public JSONObject loadEdit(JSONObject json) {
		JSONObject output = new JSONObject();
		long startTime = System.currentTimeMillis();
		List<JSONObject> editList = new ArrayList<JSONObject>();
		try {
			List<Object> editPropList = getQueryOutput("from Edit where fkBranchId = :branchId ",
					new String[] { "branchId" }, new Object[] { json.getInt("branchId") });
			String lastUpdatedOn = "";
			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Edit edit = (Edit) editItr.next();
				lastUpdatedOn = "";
				if (edit.getLastUpdatedOn() != null) {
					lastUpdatedOn = edit.getLastUpdatedOn().toString();
				}
				JSONObject editJson = new JSONObject();
				editJson.put("editId", edit.getEditId());
				editJson.put("metadata", new JSONObject(edit.getMetadata()));
				editJson.put("createdOn", edit.getCreatedOn().toString());
				editJson.put("lastUpdatedOn", lastUpdatedOn);
				editJson.put("branchId", edit.getBranch().getBranchId());
				editJson.put("testId", edit.getTestId());
				editJson.put("status", getEditStatus(edit.getStatus()));
				editJson.put("action", getEditedActionStr(edit.getAction()));
				editList.add(editJson);

			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			output.put("returnType", "Edit").put("count", editList.size()).put("totalMetric", elapsedTime + " seconds.")
					.put("edits", new JSONArray(editList));

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject loadWorkspace(JSONObject json) {

		JSONObject output = new JSONObject();
		long startTime = System.currentTimeMillis();

		List<JSONObject> editList = new ArrayList<JSONObject>();
		try {
			List<Object> editPropList = getQueryOutput("from Edit where editId in (:editIds)",
					new String[] { "editIds" }, new Object[] { json.getJSONArray("editIds").toList() });
			// editQuery.setByte("status", getStatusCode("Active"));

			String lastUpdatedOn = "";

			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Edit edit = (Edit) editItr.next();
				lastUpdatedOn = "";
				if (edit.getLastUpdatedOn() != null) {
					lastUpdatedOn = edit.getLastUpdatedOn().toString();
				}
				JSONObject editJson = new JSONObject();
				editJson.put("workspaceId", edit.getEditId());
				editJson.put("metadata", new JSONObject(edit.getMetadata()));
				editJson.put("createdOn", edit.getCreatedOn().toString());
				editJson.put("lastUpdatedOn", lastUpdatedOn);
				editJson.put("branchId", edit.getBranch().getBranchId());
				editJson.put("testId", edit.getTestId());
				editJson.put("status", getEditStatus(edit.getStatus()));
				editList.add(editJson);

			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			output.put("returnType", "Edit").put("count", editList.size()).put("totalMetric", elapsedTime + " seconds.")
					.put("workspaces", new JSONArray(editList));

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject deleteWorkspaceById(JSONObject json) {

		JSONObject output = new JSONObject();

		try {
			Query editQuery = entityManager.createQuery("delete from Edit where editId = :workspaceId");
			editQuery.setParameter("workspaceId", json.getInt("workspaceId"));
			Integer isUpdated = editQuery.executeUpdate();
			if (isUpdated > 0) {
				output.put("message", DELETED_SUCCESS);
			} else {
				output.put("message", NO_DATA_FOUND);
			}
		} catch (Exception e) {
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject updateWorkspaceById(JSONObject json) {

		JSONObject output = new JSONObject();

		try {
			Integer workspaceId = json.getInt("workspaceId");
			Byte status = getEditStatusCode(json.getString("status"));
			json.remove("workspaceId");
			json.remove("status");

			Query editQuery = entityManager.createQuery(
					"update Edit set metadata = :metadata, status = :status where editId = :workspaceId and status = 1");
			editQuery.setParameter("metadata", json.toString());
			editQuery.setParameter("status", status);
			editQuery.setParameter("workspaceId", workspaceId);

			Integer isUpdated = editQuery.executeUpdate();
			if (isUpdated > 0) {
				output.put("message", UPDATED_SUCCESS);
			} else {
				output.put("message", NO_DATA_FOUND);
			}

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject updateBranchById(JSONObject json) {

		JSONObject output = new JSONObject();

		try {
			Integer branchId = json.getInt("branchId");

			List<Object> editPropList = getQueryOutput(
					"select status from Edit where fkBranchId = :branchId group by status", new String[] { "branchId" },
					new Object[] { json.getInt("branchId") });
			String testmsg = "Not Tested";
			int totCount = editPropList.size();
			int testedCount = 0;
			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Byte statusByte = (Byte) editItr.next();
				if (STATUS_TESTED.equals(getEditStatus(statusByte))) {
					testedCount++;
				}
			}
			if (totCount != 0 && testedCount == totCount) {
				testmsg = "Tested";
			} else if (testedCount > 0 && testedCount < totCount) {
				testmsg = "Partially Tested";
			}
			Byte status = getStatusCode(json.getString("status"));

			Query branchUpdateQuery = entityManager
					.createQuery("update Branch set status = :status where branchId = :branchId");
			branchUpdateQuery.setParameter("status", status);
			branchUpdateQuery.setParameter("branchId", branchId);

			Integer isUpdated = branchUpdateQuery.executeUpdate();
			if (isUpdated > 0) {
				output.put("message", UPDATED_SUCCESS);
			} else {
				output.put("message", NO_DATA_FOUND);
			}
			output.put("test_message", testmsg);

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject updateTestById(JSONObject json) {

		JSONObject output = new JSONObject();

		/*
		 * String type = json.getString("type"); json.remove("type");
		 */
		try {
			Integer testId = json.getInt("testId");
			List workspaceIds = json.getJSONArray("workspaceIds").toList();
			json.remove("workspaceIds");

			String testResult = "{}";// engine.testConstraintPath(json.toString());

			Query testUpdateQuery = entityManager.createQuery(
					"update Test set testInput = :testInput, testResult = :testResult where testId = :testId");
			testUpdateQuery.setParameter("testInput", json.toString());

			testUpdateQuery.setParameter("testResult", testResult);
			testUpdateQuery.setParameter("testId", testId);
			json.put("id", testId);
			int isUpdated = testUpdateQuery.executeUpdate();
			if (isUpdated > 0) {
				Query editQuery = entityManager.createQuery(
						"update Edit set lastUpdatedOn = :lastUpdatedOn , status = :status where editId in (:workspaceIds)");

				Date createdDate = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gmt = new Date(sdf.format(createdDate));

				editQuery.setParameter("lastUpdatedOn", gmt);
				editQuery.setParameter("status", getEditStatusCode(STATUS_TESTED));
				editQuery.setParameter("workspaceIds", workspaceIds);

				isUpdated = editQuery.executeUpdate();
				output.put("message", UPDATED_SUCCESS);
				if (isUpdated > 0) {
					output.put("message", UPDATED_SUCCESS);
				} else {
					output.put("message", NO_DATA_FOUND + ", but Test was updated.");
				}

				/*
				 * if ("text".equals(type)) {
				 * 
				 * }
				 */
				String testSumbitUrl = env.getProperty("test.submitUrl");
				RestClient restClient = new RestClient();
				System.out.println(json.toString());
				json.remove("testId");
				json.remove("type");
				try {
					testResult = restClient.post(testSumbitUrl, json.toString());
					testResult = restClient.get(testSumbitUrl + "/" + testId);
				} catch (Exception e) {

				}
				if (testResult == null) {
					testResult = "{}";
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
			e.printStackTrace();
			output.put("message", e.getMessage());
		}

		return (output);
	}

	public JSONObject loadVariants(JSONObject json) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		ObjectMapper objectMapper = new ObjectMapper();

		List<JSONObject> variantList = new ArrayList<JSONObject>();

		// Prep work
		try {
			Query propvalueQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			if ("varString".equals(json.getString("key"))) {
				query = "	select v.id as variantId, vs.value as varString, d.name as dictionary from tblVariant v"
						+ "	  INNER JOIN tblDictionary d on v.fkDictionaryId = d.id"
						+ "	  INNER JOIN tblVariantString vs on v.fkVariantStringId = vs.id and lower(vs.value) like lower('%"
						+ json.getString("value") + "%')"
						+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId" + "   WHERE v.status != 2"
						+ "	  group by v.id" + "	  order by v.id asc" + " limit " + offset + ", " + (limit + 5)
						+ ";";

			} else {

				query = "	select v.id as variantId, v.fkVariantStringId, v.fkDictionaryId from tblVariant v"
						+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId"
						+ "   INNER JOIN tblPropertyValue pv on vp.fkPropertyValueId = pv.id and lower(pv.value) = lower('"
						+ json.getString("value") + "')"
						+ "   INNER JOIN tblProperty p on pv.fkPropertyId = p.id and p.name= '" + json.getString("key")
						+ "'" + "   WHERE v.status != 2" + "	  group by v.id" + "	  order by v.id asc"
						+ " limit " + offset + ", " + (limit + 10) + ";";

			}
			List<Object> cuiPropList = getNativeQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = cuiPropList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();
				String varString = "";
				String dictionary = "";

				if (!"varString".equals(json.getString("key"))) {
					List<Object> varList = getQueryOutput("from VariantString where id = :variantStringId",
							new String[] { "variantStringId" }, new Object[] { (Integer) object[1] });
					varString = ((VariantString) varList.get(0)).getValue();
					List<Object> dictList = getQueryOutput("from Dictionary where id = :dictionaryId",
							new String[] { "dictionaryId" }, new Object[] { (Integer) object[2] });
					dictionary = ((Dictionary) dictList.get(0)).getName();

				} else {
					varString = (String) object[1];
					dictionary = (String) object[2];

				}
				Integer variantId = (Integer) object[0];
				List<Object> entryPropVarList = getQueryOutput(
						"select propertyValue from VariantPropertyValue where fkVariantId = :fkVariantId ",
						new String[] { "fkVariantId" }, new Object[] { variantId });
				List<JSONObject> props = new ArrayList<JSONObject>();
				JSONObject propjson = new JSONObject();
				boolean isDiffCui = false;
				for (Object actualpropvalue : entryPropVarList) {
					String propName = ((PropertyValue) actualpropvalue).getProperty().getName();
					if (!"varString".equals(json.getString("key")) && propName.equals("cui") && !(json
							.getString("value").equalsIgnoreCase(((PropertyValue) actualpropvalue).getValue()))) {
						isDiffCui = true;
						break;
					}
					props.add(new JSONObject().put("name", propName).put("value",
							((PropertyValue) actualpropvalue).getValue()));
					propjson.put(propName, ((PropertyValue) actualpropvalue).getValue());

				}
				if (isDiffCui) {
					continue;
				}

				List<String> actions = new ArrayList<String>();
				actions.add("Add");
				actions.add("Delete");

				variantList.add(new JSONObject().put("varString", varString).put("dictionary", dictionary)
						.put("variantId", variantId).put("props", props).put("actions", actions));
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Variant").put("count", variantList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("variants", new JSONArray(variantList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);

	}

	public JSONObject loadVariantsByIds(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> variantList = new ArrayList<JSONObject>();

		// Prep work
		try {
			Query propvalueQuery = null;
			String query = "";
			List variantIdList = json.getJSONArray("variantIds").toList();
			String variantIds = variantIdList.toString().replace("[", "(").replace("]", ")");

			query = "	select v.id as variantId, vs.value, di.name , v.fkVariantStringId, v.fkDictionaryId from tblVariant v"
					+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId"
					+ "	  INNER JOIN tblVariantString vs on vs.id = v.fkVariantStringId"
					+ "   INNER JOIN tblDictionary di on di.id = v.fkDictionaryId"
					+ "   INNER JOIN tblPropertyValue pv on vp.fkPropertyValueId = pv.id"
					+ "   INNER JOIN tblProperty p on pv.fkPropertyId = p.id " + "   WHERE v.status != 2 and v.id in "
					+ variantIds + "	  group by v.id" + "	  order by v.id asc;";

			List<Object> variantRecordList = getNativeQueryOutput(query);
			// pagination

			for (Iterator searchItr = variantRecordList.iterator(); searchItr.hasNext();) {

				Object[] object = (Object[]) searchItr.next();

				String varString = (String) object[1];
				String dictionary = (String) object[2];

				Integer variantId = (Integer) object[0];
				List<Object> entryPropVarList = getQueryOutput(
						"select propertyValue from VariantPropertyValue where fkVariantId = :fkVariantId ",
						new String[] { "fkVariantId" }, new Object[] { variantId });

				List<JSONObject> props = new ArrayList<JSONObject>();
				JSONObject propjson = new JSONObject();
				for (Object actualpropvalue : entryPropVarList) {
					String propName = ((PropertyValue) actualpropvalue).getProperty().getName();
					props.add(new JSONObject().put("name", propName).put("value",
							((PropertyValue) actualpropvalue).getValue()));
					propjson.put(propName, ((PropertyValue) actualpropvalue).getValue());

				}

				List<String> actions = new ArrayList<String>();
				actions.add("Add");
				actions.add("Delete");

				variantList.add(new JSONObject().put("varString", varString).put("dictionary", dictionary)
						.put("variantId", variantId).put("props", props).put("actions", actions));
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Variant").put("count", variantList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("variants", new JSONArray(variantList));
		} catch (Exception e) {
			e.getMessage();
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);

	}

	public JSONObject loadWorkspaceVariantsByIds(JSONArray jsonArr) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();
		List<JSONObject> variantList = new ArrayList<JSONObject>();

		// Prep work
		try {

			String query = "";
			Map<Integer, String> variantIdActionMap = new HashMap<Integer, String>();
			for (Object obj : jsonArr) {
				JSONObject editObj = (JSONObject) obj;
				variantIdActionMap.put(editObj.getInt("variantId"), editObj.getString("action"));
			}

			boolean isVariantFound = false;

			JSONObject myjson = (org.json.JSONObject) jsonArr.get(0);
			int inVariantId = myjson.getInt("variantId");

			List<Object> editPropList = getQueryOutput("from Edit where fkBranchId = :branchId",
					new String[] { "branchId" }, new Object[] { myjson.getInt("branchId") });
			System.out.println("branchId: " + myjson.getInt("branchId"));
			String lastUpdatedOn = "";
			for (Iterator editItr = editPropList.iterator(); editItr.hasNext();) {
				Edit edit = (Edit) editItr.next();
				JSONObject metaJson = new JSONObject(edit.getMetadata());
				if (!metaJson.isNull("variantId") && inVariantId == metaJson.getInt("variantId")
						&& metaJson.getInt("variantId") != -1) {
					isVariantFound = true;
				}
				lastUpdatedOn = "";
				if (edit.getLastUpdatedOn() != null) {
					lastUpdatedOn = edit.getLastUpdatedOn().toString();
				}
				JSONObject editJson = new JSONObject();

				editJson.put("editId", edit.getEditId());
				if (!metaJson.isNull("variantId") && metaJson.getInt("variantId") != -1) {
					editJson.put("variantId", metaJson.getInt("variantId"));
				}
				editJson.put("varString", metaJson.getString("varString"));
				editJson.put("dictionary", metaJson.getString("dictionary"));
				editJson.put("action", metaJson.getString("action"));
				metaJson.remove("variantId");
				metaJson.remove("varString");
				metaJson.remove("dictionary");
				metaJson.remove("action");
				metaJson.remove("status");

				List<JSONObject> props = new ArrayList<JSONObject>();
				Set metaFields = metaJson.keySet();
				for (Iterator metaItr = metaFields.iterator(); metaItr.hasNext();) {
					String fieldName = (String) metaItr.next();
					props.add(new JSONObject().put("name", fieldName).put("value", metaJson.get(fieldName)));

				}

				editJson.put("createdOn", edit.getCreatedOn().toString());
				editJson.put("lastUpdatedOn", lastUpdatedOn);
				editJson.put("branchId", myjson.getInt("branchId"));
				editJson.put("testId", edit.getTestId());
				editJson.put("status", getEditStatus(edit.getStatus()));
				editJson.put("props", props);
				// JsonArray actions = new JsonArray();
				// actions.add("Add");
				// actions.add("Delete");
				// actions.add(json.getString("action"));

				variantList.add(editJson);

			}

			if (!isVariantFound) {

				String variantIds = variantIdActionMap.keySet().toString().replace("[", "(").replace("]", ")");
				query = "	select v.id as variantId, vs.value, di.name , v.fkVariantStringId, v.fkDictionaryId from tblVariant v"
						+ "	  INNER JOIN tblVariantPropertyValue vp on v.id = vp.fkVariantId"
						+ "	  INNER JOIN tblVariantString vs on vs.id = v.fkVariantStringId"
						+ "   INNER JOIN tblDictionary di on di.id = v.fkDictionaryId"
						+ "   INNER JOIN tblPropertyValue pv on vp.fkPropertyValueId = pv.id"
						+ "   INNER JOIN tblProperty p on pv.fkPropertyId = p.id "
						+ "   WHERE v.status != 2 and v.id in " + variantIds + "	  group by v.id"
						+ "	  order by v.id asc;";

				List<Object> variantRecordList = getNativeQueryOutput(query);
				// pagination

				for (Iterator searchItr = variantRecordList.iterator(); searchItr.hasNext();) {

					Object[] object = (Object[]) searchItr.next();

					String varString = (String) object[1];
					String dictionary = (String) object[2];

					Integer variantId = (Integer) object[0];
					List<Object> entryPropVarList = getQueryOutput(
							"select propertyValue from VariantPropertyValue where fkVariantId = :fkVariantId ",
							new String[] { "fkVariantId" }, new Object[] { variantId });

					List<JSONObject> props = new ArrayList<JSONObject>();
					JSONObject propjson = new JSONObject();
					for (Object actualpropvalue : entryPropVarList) {
						String propName = ((PropertyValue) actualpropvalue).getProperty().getName();
						props.add(new JSONObject().put("name", propName).put("value",
								((PropertyValue) actualpropvalue).getValue()));
						propjson.put(propName, ((PropertyValue) actualpropvalue).getValue());

					}

					List<String> actions = new ArrayList<String>();
					actions.add(variantIdActionMap.get(variantId));

					variantList.add(0, new JSONObject().put("varString", varString).put("dictionary", dictionary)
							.put("variantId", variantId).put("props", props).put("actions", actions));
				}
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Variant").put("count", variantList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("variants", new JSONArray(variantList));
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);

	}

	public JSONObject cmpGenerate() {
		CmpFileBuilder cmpBuilder = null;
		JSONObject output = new JSONObject();

		try {
			/*
			 * String cmpFileLocation =
			 * config.getString("m3h.cmp.file.location");
			 * 
			 * cmpBuilder = new CmpFileBuilder(config);
			 * cmpBuilder.compileCmpFiles(cmpFileLocation);
			 * 
			 * BundleCMPFiles packageCmpFiles = new BundleCMPFiles(config);
			 * packageCmpFiles.generateBundle();
			 */ output.put("message", "Successfully created");
		} catch (Exception e) {
			output.put("message", e.getMessage());
		}
		return (output);

	}

	/**
	 * Return the branch having the passed id.
	 */
	public Branch getById(long id) {
		return entityManager.find(Branch.class, id);
	}

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

	private String getEditedActionStr(int editAction) {
		String action = "Add";
		if (editAction == 2) {
			action = "Edit";

		} else if (editAction == 3) {
			action = "Delete";

		}
		return action;
	}

	private List<Object> getQueryOutput(String query, String[] params, Object[] values) {
		Query regionTitleQuery = entityManager.createQuery(query);
		for (int qIdx = 0; qIdx < params.length; qIdx++) {
			regionTitleQuery.setParameter(params[qIdx], values[qIdx]);
		}

		return regionTitleQuery.getResultList();
	}

	private List<Object> getNativeQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createNativeQuery(query);

		return regionTitleQuery.getResultList();
	}

	private List<Object> getQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createQuery(query);

		return regionTitleQuery.getResultList();
	}

	FilenameFilter fileNameFilter = new FilenameFilter() {

		@Override
		public boolean accept(File dir, String name) {
			if (name.lastIndexOf("CMApproaches.cmp") > 0) {
				return true;
			}
			return false;
		}
	};

	@Autowired
	private Environment env;

} // class TblBranchDao
