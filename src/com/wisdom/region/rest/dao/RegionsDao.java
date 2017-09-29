package com.wisdom.region.rest.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.wisdom.common.domain.Branch;
import com.wisdom.common.domain.Release;
import com.wisdom.common.domain.Test;
import com.wisdom.common.httpclient.RestClient;
import com.wisdom.dictionaries.rest.domain.Edit;
import com.wisdom.region.rest.domain.BoilerplateRegionBranch;
import com.wisdom.region.rest.domain.NormalizedRegionTitle;
import com.wisdom.region.rest.domain.NormalizedRegionTitleBranch;
import com.wisdom.region.rest.domain.NormalizedRegionTitleUse;
import com.wisdom.region.rest.domain.NormalizedRegionTitleUseBranch;
import com.wisdom.region.rest.domain.NormalizedRegionTitleUseBranchId;
import com.wisdom.region.rest.domain.RegionEdit;
import com.wisdom.region.rest.domain.RegionEditMapping;
import com.wisdom.region.rest.domain.RegionEditMappingId;
import com.wisdom.region.rest.domain.RegionTitle;
import com.wisdom.region.rest.domain.RegionTitleBranch;
import com.wisdom.region.rest.domain.RegionUse;
import com.wisdom.region.rest.domain.RegionUseBranch;

@Repository
@Transactional
public class RegionsDao {

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

	@Autowired
	private Environment env;

	/**
	 * Save the branch in the database.
	 */
	public JSONObject create(JSONObject regionObject) {
		// TODO Auto-generated method stub
		JSONObject output = new JSONObject().put("message", ADDED_SUCCESS);
		JSONArray regionArray = (JSONArray) regionObject.getJSONArray("regions");

		try {

			for (Iterator regionArItr = regionArray.iterator(); regionArItr.hasNext();) {
				JSONObject regionJson = (JSONObject) regionArItr.next();

				JSONObject regionTitleJson = ((JSONObject) regionJson).getJSONObject("regionTitle");
				JSONObject nRegionTitleJson = ((JSONObject) regionJson).getJSONObject("normalizedRegionTitle");
				int regionTitleId = regionTitleJson.getInt("regionTitleId");
				int normalizedTitleId = nRegionTitleJson.getInt("normalizedRegionTitleId");
				int generatedRegionTitleId = -1;
				String normalizedTitle = nRegionTitleJson.getString("title");
				String regionTitle = regionTitleJson.getString("title");
				int editId = -1;
				boolean isNormalizedSame = false;

				if (regionTitleId > 0 && !"Add".equals(((JSONObject) regionJson).getString("action"))) {
					String query = "from RegionTitle where id =:regionTitleId ";
					List<Object> regionResultList = getQueryOutput(query, new String[] { "regionTitleId" },
							new Object[] { regionTitleId });

					if (regionResultList.size() == 0) {
						throw new Exception("Invalid RegionTitle");
					}
				} else if (regionTitleId <= 0) {
					String query = "from RegionTitle where title =:title ";
					List<Object> regionResultList = getQueryOutput(query, new String[] { "title" },
							new Object[] { regionTitle });

					if (regionResultList.size() > 0) {
						throw new Exception("RegionTitle exists");
					}

				} else if (!"Add".equals(((JSONObject) regionJson).getString("action"))) {
					throw new Exception("Invalid RegionTitleId");
				}

				if (normalizedTitleId > 0 && !"Add".equals(((JSONObject) regionJson).getString("action"))) {
					String query = "from NormalizedRegionTitle where id =:normalizedRegionTitleId ";
					List<Object> nRegionResultList = getQueryOutput(query, new String[] { "normalizedRegionTitleId" },
							new Object[] { normalizedTitleId });

					if (nRegionResultList.size() == 0) {
						throw new Exception("Invalid NormalizedRegionTitle");
					}

					if (((NormalizedRegionTitle) nRegionResultList.get(0)).getTitle().equals(normalizedTitle)) {
						isNormalizedSame = true;
					}

				} else if (normalizedTitleId <= 0) {
					String query = "from NormalizedRegionTitle where title =:title ";
					List<Object> nRegionResultList = getQueryOutput(query, new String[] { "title" },
							new Object[] { normalizedTitle });

					if (nRegionResultList.size() > 0) {
						throw new Exception("NormalizedRegionTitle exists");
					}

				} else if (!"Add".equals(((JSONObject) regionJson).getString("action"))) {

					throw new Exception("Invalid NormalizedRegionTitle");
				}

				JSONArray regionUseListTmp = ((JSONObject) regionJson).getJSONArray("regionUses");
				List<Object> regionUseList = regionUseListTmp.toList();
				/*
				 * if (isNormalizedSame) {
				 * 
				 * // Overwrite regionUseList in case NormalizedId is same
				 * regionUseList = new JSONArray(); Query nregionUseQuery =
				 * entityManager.createSQLQuery(
				 * "SELECT tru.regionUseId, tru.ncid, tru.description FROM tblNormalizedRegionTitleUse tnru "
				 * +
				 * "inner join tblRegionUse tru on tru.regionUseId = tnru.fkRegionUseId "
				 * + "where tnru.fkNormalizedRegionTitleId = " +
				 * normalizedTitleId); List<Object> nrUseList =
				 * nregionUseQuery.list(); for (Iterator searchItr =
				 * nrUseList.iterator(); searchItr.hasNext();) { Object[]
				 * nrObjectIds = (Object[]) searchItr.next();
				 * 
				 * regionUseList.add( new JSONObject().put("regionUseId",
				 * nrObjectIds[0]).put("ncid",
				 * nrObjectIds[1]).put("description", nrObjectIds[2])); } }
				 */
				if (regionUseList.size() == 0) {
					regionUseList.add(new JSONObject().put("regionUseId", -1).put("description", "Default"));
				}

				NormalizedRegionTitleBranch tnrtBranch = new NormalizedRegionTitleBranch(normalizedTitle);
				entityManager.persist(tnrtBranch);
				entityManager.flush();

				normalizedTitleId = tnrtBranch.getNormalizedRegionTitleId();
				RegionTitleBranch trBranch = new RegionTitleBranch();
				trBranch.setFkNormalizedRegionTitleId(normalizedTitleId);
				trBranch.setTitle(regionTitle);

				if (regionTitleId > 0) {
					trBranch.setMainRegionTitleId(regionTitleId);
				}

				entityManager.persist(trBranch);
				entityManager.flush();
				generatedRegionTitleId = trBranch.getRegionTitleId();
				for (Object regionUse : regionUseList) {
					int regionUseId = (Integer) ((Map) regionUse).get("regionUseId");
					String description = (String) ((Map) regionUse).get("description");
					String ncid = ((Map) regionUse).containsKey("ncid")?(String)((Map) regionUse).get("ncid"):null;
					if (regionUseId > 0) {
						String query = "from RegionUse where id =:regionUseId ";
						List<Object> regionUseResultList = getQueryOutput(query, new String[] { "regionUseId" },
								new Object[] { regionUseId });

						if (regionUseResultList.size() == 0
								&& !"Add".equals(((JSONObject) regionJson).getString("action"))) {
							throw new Exception("No RegionUse");
						}
						description = ((RegionUse) regionUseResultList.get(0)).getDescription();
						ncid = ((RegionUse) regionUseResultList.get(0)).getNcid();
					}

					RegionUseBranch tregionuse = new RegionUseBranch();
					tregionuse.setDescription(description);
					tregionuse.setNcid(ncid);

					entityManager.persist(tregionuse);
					entityManager.flush();

					regionUseId = tregionuse.getRegionUseId();
					NormalizedRegionTitleUseBranch tnrtuBranch = new NormalizedRegionTitleUseBranch(
							new NormalizedRegionTitleUseBranchId(normalizedTitleId, regionUseId));
					entityManager.persist(tnrtuBranch);
					entityManager.flush();

					if (editId == -1) {
						RegionEdit trEdit = new RegionEdit();
						trEdit.setStatus(getStatusCode(STATUS_ACTIVE));

						if ("Edit".equalsIgnoreCase(((JSONObject) regionJson).getString("action"))) {
							trEdit.setAction(Byte.parseByte("2"));
							output.put("message", UPDATED_SUCCESS);
						} else if ("Delete".equalsIgnoreCase(((JSONObject) regionJson).getString("action"))) {
							trEdit.setAction(Byte.parseByte("3"));
							output.put("message", DELETED_SUCCESS);
						}

						trEdit.setFkBranchId(((JSONObject) regionJson).getInt("branchId"));
						
						
						
						Query query = entityManager
								.createNativeQuery("SELECT fkTestId FROM tblRegionEdit WHERE fkBranchId =" + ((JSONObject) regionJson).getInt("branchId"));
						List<Object> testIdObj = query.getResultList();
						int testId = -1;

						if (testIdObj.size() > 0) {
							testId = Integer.parseInt(testIdObj.get(0).toString());
						}
						
						
						if (testId == -1) {
							List<Object> regBranchList = getNativeQueryOutput("select tbm.branchId from "
									+ "(select tbm.branchMapId from tblBranch tb "
									+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId = " + ((JSONObject) regionJson).getInt("branchId")
									+ " ) output "
									+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
									+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + DICTIONARIES_TYPE + "' ");
							// System.out.println(dicBranchList.get(0));
							List<Object> editPropList = getQueryOutput(
									"from Edit where fkBranchId in (:branchIds) ",
									new String[] { "branchIds"},
									new Object[] { regBranchList });

							if(editPropList.size() > 0) {
								testId  = ((Edit)editPropList.get(0)).getTestId();
							}
						}
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
						trEdit.setCreatedOn(gmt);
						trEdit.setFkTestId(testId);
						entityManager.persist(trEdit);
						entityManager.flush();
						editId = trEdit.getEditId();
						RegionEditMapping treMapping = new RegionEditMapping(
								new RegionEditMappingId(editId, (int) generatedRegionTitleId));

						entityManager.persist(treMapping);
						entityManager.flush();
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			output.put("message", e.getMessage());
		}
		return output;

	}

	public JSONObject createRegionTitles(JSONObject json) {
		JSONObject output = new JSONObject().put("message", ADDED_SUCCESS);

		EntityTransaction tx = entityManager.getTransaction();
		try {
			// HQL example - Create Branch
			// regionTitle, normalregiontitle, ncid, description

			Query regionTitleQuery = entityManager.createQuery("from RegionTitleBranch where title =:title ");
			regionTitleQuery.setParameter("title", json.getString("regionTitle"));

			List<RegionTitleBranch> branchResultList = regionTitleQuery.getResultList();
			if (branchResultList.size() == 0) {

				RegionTitleBranch regionTitleBranch = new RegionTitleBranch();
				regionTitleBranch.setTitle(json.getString("regionTitle"));
				entityManager.persist(regionTitleBranch);
				entityManager.flush();

				RegionEdit trEdit = new RegionEdit();
				// trEdit.setFkBranchId(json.getInt("branchId"));
				trEdit.setStatus(getStatusCode(STATUS_ACTIVE));
				if ("Add".equalsIgnoreCase(json.getString("action"))) {
					trEdit.setAction(Byte.parseByte("1"));
				} /*
					 * else { trEdit.setAction(Byte.parseByte("2")); }
					 */

				Date createdDate = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gmt = new Date(sdf.format(createdDate));
				trEdit.setCreatedOn(gmt);
				entityManager.persist(trEdit);
				entityManager.flush();
				int editId = trEdit.getEditId();

				output.put("regionTitleId", regionTitleBranch.getRegionTitleId());
				output.put("editId", editId);

			} else {
				output.put("message", "RegionTitleBranch name is already exists");
			}

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}
		tx.commit();
		return output;

	}

	public JSONObject createRegionUses(JSONObject json) {
		JSONObject output = new JSONObject().put("message", ADDED_SUCCESS);

		EntityTransaction tx = entityManager.getTransaction();
		try {

			String query = "from RegionUseBranch where ncid =:ncid ";
			List<Object> branchResultList = getQueryOutput(query, new String[] { "ncid" },
					new Object[] { json.getString("ncid") });

			if (branchResultList.size() == 0) {

				RegionUseBranch regionUseBranch = new RegionUseBranch();
				regionUseBranch.setNcid(json.getString("ncid"));
				regionUseBranch.setDescription(json.getString("description"));

				entityManager.persist(regionUseBranch);
				entityManager.flush();

				RegionEdit trEdit = new RegionEdit();
				// trEdit.setFkBranchId(json.getInt("branchId"));
				trEdit.setStatus(getStatusCode(STATUS_ACTIVE));
				if ("Add".equalsIgnoreCase(json.getString("action"))) {
					trEdit.setAction(Byte.parseByte("1"));
				} /*
					 * else { trEdit.setAction(Byte.parseByte("2")); }
					 */

				Date createdDate = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gmt = new Date(sdf.format(createdDate));
				trEdit.setCreatedOn(gmt);
				entityManager.persist(trEdit);
				entityManager.flush();
				int editId = trEdit.getEditId();

				output.put("regionUseId", regionUseBranch.getRegionUseId());
				output.put("editId", editId);

			} else {
				output.put("message", "RegionUse name is already exists");
			}

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}
		tx.commit();
		return output;

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
	 * Delete the branch from the database.
	 */
	public JSONObject deleteEdit(int editId) {
		JSONObject output = new JSONObject().put("message", DELETED_SUCCESS);
		Query editMappingDelQuery = entityManager
				.createNativeQuery("delete from tblRegionEditMapping where fkEditId =:editId ");
		editMappingDelQuery.setParameter("editId", editId);
		editMappingDelQuery.executeUpdate();
		entityManager.flush();

		editMappingDelQuery = entityManager.createNativeQuery("delete from tblRegionEdit where editId =:editId ");
		editMappingDelQuery.setParameter("editId", editId);
		editMappingDelQuery.executeUpdate();
		entityManager.flush();
		return output;

	}

	/**
	 * Return all the branches stored in the database.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getAll(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query titleQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			String rtWhereClause = "";
			String nrtWhereClause = "";
			String ruWhereClause = "";
			String ncidWhereClause = "";
			if ("ncid".equals(json.getString("key"))) {
				ncidWhereClause = " and ru.ncid = '" + json.getString("value") + "' ";
			} else if ("regiontitle".equals(json.getString("key"))) {
				rtWhereClause = " and lower(rt.title) like lower('%" + json.getString("value") + "%') ";
			} else if ("normalizedregiontitle".equals(json.getString("key"))) {
				nrtWhereClause = " where lower(nrt.title) like lower('%" + json.getString("value") + "%') ";
			} else if ("description".equals(json.getString("key"))) {
				ruWhereClause = " and lower(ru.description) like lower('%" + json.getString("value") + "%') ";
			} else {
				throw new Exception("Invalid Key search");
			}
			query = "select nrt.title as nrt_title, rt.title as rt_title, ru.ncid, ru.description, rt.id as regionTitleId, nrt.id as normalizedRegionTitleId, ru.id as regionUseId "
					+ "from tblNormalizedRegionTitle nrt "
					+ "inner join tblRegionTitle rt on nrt.id = rt.fkNormalizedRegionTitleId and rt.status = 1 " + rtWhereClause
					+ (ncidWhereClause=="" && ruWhereClause =="" ? query + "left outer": query + "inner ") + " join tblNormalizedRegionTitleUse mnru on rt.fkNormalizedRegionTitleId = mnru.fkNormalizedRegionTitleId  "
					+ (ncidWhereClause=="" && ruWhereClause =="" ? query + "left outer": query + "inner " ) + " join tblRegionUse ru on mnru.fkRegionUseId = ru.id  " + ruWhereClause + ncidWhereClause
					+ nrtWhereClause + " order by rt_title asc" + " limit " + offset + ", " + (limit + 10) + ";";

			List<Object> titleList = getQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList
						.add(new JSONObject().put("regionTitle", object[1]).put("normalizedRegionTitle", object[0])
								.put("ncid", object[2]).put("description", object[3]).put("regionTitleId", object[4])
								.put("normalizedRegionTitleId", object[5]).put("regionUseId", object[6]));
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Region").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;
	}

	
	public JSONObject getAllMapTitles(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query titleQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			String rtWhereClause = "";
			String nrtWhereClause = "";
			String ruWhereClause = "";
			String ncidWhereClause = "";
			if ("regiontitle".equals(json.getString("key"))) {
				rtWhereClause = " and lower(rt.title) like lower('" + json.getString("value") + "') ";
			} else if ("normalizedregiontitle".equals(json.getString("key"))) {
				nrtWhereClause = " where lower(nrt.title) like lower('" + json.getString("value") + "') ";
			} else if ("description".equals(json.getString("key"))) {
				ruWhereClause = " and lower(ru.description) like lower('" + json.getString("value") + "') ";
			} else {
				throw new Exception("Invalid Key search");
			}
			query = "select nrt.title as nrt_title, rt.title as rt_title, ru.ncid, ru.description, rt.id as regionTitleId, nrt.id as normalizedRegionTitleId, ru.id as regionUseId "
					+ "from tblNormalizedRegionTitle nrt "
					+ "inner join tblRegionTitle rt on nrt.id = rt.fkNormalizedRegionTitleId and rt.status = 1 " + rtWhereClause
					+ (ncidWhereClause=="" && ruWhereClause =="" ? query + "left outer": query + "inner ") + " join tblNormalizedRegionTitleUse mnru on rt.fkNormalizedRegionTitleId = mnru.fkNormalizedRegionTitleId  "
					+ (ncidWhereClause=="" && ruWhereClause =="" ? query + "left outer": query + "inner " ) + " join tblRegionUse ru on mnru.fkRegionUseId = ru.id  " + ruWhereClause + ncidWhereClause
					+ nrtWhereClause + " order by rt_title asc" + " limit " + offset + ", " + (limit + 10) + ";";

			List<Object> titleList = getQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList
						.add(new JSONObject().put("regionTitle", object[1]).put("normalizedRegionTitle", object[0])
								.put("ncid", object[2]).put("description", object[3]).put("regionTitleId", object[4])
								.put("normalizedRegionTitleId", object[5]).put("regionUseId", object[6]));
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Region").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;
	}

	/**
	 * Return all the branches stored in the database.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getLoadRegionTitles(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query titleQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
			String rtWhereClause = "where rt.status = 1 ";
			if ("regiontitle".equals(json.getString("key"))) {
				rtWhereClause += " and lower(rt.title) like lower('%" + json.getString("value") + "%') ";
			} else {
				throw new Exception("Invalid Search Key");

			}
			query = "SELECT rt.id as regionTitleId,rt.title, rt.fkNormalizedRegionTitleId normalizedTitleId FROM tblRegionTitle rt "
					+ rtWhereClause + "order by rt.title" + " asc limit " + offset + ", " + (limit + 10) + ";";

			List<Object> titleList = getQueryOutput(query);

			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList.add(new JSONObject().put("regionTitleId", object[0]).put("title", object[1])
						.put("normalizedTitleId", object[2]));
				limitIdx++;
			}

			responseJson.put("returnType", "RegionTitle").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;
	}

	/**
	 * Return all the branches stored in the database.
	 */
	@SuppressWarnings("unchecked")
	public JSONObject loadNormalizedRegionTitles(JSONObject json) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query titleQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
			String nrtWhereClause = "";
			if (!json.isNull("key") && "normalizedRegionTitle".equals(json.getString("key"))) {
				nrtWhereClause = " where lower(nrt.title) like lower('%" + json.getString("value") + "%') ";
			} else if (!json.isNull("normalizedRegionId")) {
				nrtWhereClause = " where nrt.id = " + json.getInt("normalizedRegionId") + " ";
			} else {
				nrtWhereClause = "";
			}

			query = "SELECT id as normalizedRegionTitleId, title FROM tblNormalizedRegionTitle nrt " + nrtWhereClause
					+ " order by nrt.id" + " limit " + offset + ", " + (limit + 10) + ";";

			List<Object> titleList = getQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList.add(new JSONObject().put("normalizedRegionTitleId", object[0]).put("title", object[1]));
				limitIdx++;
			}

			responseJson.put("returnType", "NormalizedRegionTitle").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;
	}

	@SuppressWarnings("unchecked")
	public JSONObject loadRegionUses(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query regionUseQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
			String ruWhereClause = "";
			if ("description".equals(json.getString("key"))) {
				ruWhereClause = " where lower(ru.description) like lower('%" + json.getString("value") + "%') ";
			} else if ("ncid".equals(json.getString("key"))) {
				ruWhereClause = " where lower(ru.ncid) like lower('%" + json.getString("value") + "%') ";
			}

			query = "SELECT id as regionUseId, ncid, description FROM tblRegionUse ru " + ruWhereClause + "order by id"
					+ " limit " + offset + ", " + (limit + 10) + ";";
			List<Object> regionUseList = getQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = regionUseList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList.add(new JSONObject().put("regionUseId", object[0]).put("ncid", object[1])
						.put("description", object[2]));
				limitIdx++;
			}

			responseJson.put("returnType", "RegionUse").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;
	}

	public JSONObject loadEdit(JSONObject json) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();
		List<JSONObject> editList = new ArrayList<JSONObject>();

		// HQL example - Get All Branches
		try {
			String lastUpdatedOn = "";
			String query = "from RegionEdit where fkBranchId = :branchId order by createdOn desc";
			List<Object> editResultList = getQueryOutput(query, new String[] { "branchId" },
					new Object[] { json.getInt("branchId") });
			Integer entrySize = editResultList.size();
			for (Iterator editItr = editResultList.iterator(); editItr.hasNext();) {
				RegionEdit edit = (RegionEdit) editItr.next();
				lastUpdatedOn = "";
				if (edit.getLastUpdatedOn() != null) {
					lastUpdatedOn = edit.getLastUpdatedOn().toString();
				}

				// RegionEditMapping
				int editId = edit.getEditId();
				byte editAction = edit.getAction();
				String action = "Add";
				if (editAction == 2) {
					action = "Edit";
				} else if (editAction == 3) {
					action = "Delete";
				}

				query = "from RegionEditMapping where id.fkEditId = :editId ";
				List<Object> editMapResultList = getQueryOutput(query, new String[] { "editId" },
						new Object[] { editId });

				for (Iterator editMapItr = editMapResultList.iterator(); editMapItr.hasNext();) {
					RegionEditMapping editMap = (RegionEditMapping) editMapItr.next();

					query = "select distinct nrt.title as nrt_title, rt.title as rt_title, ru.ncid, ru.description "
							+ "from tblNormalizedRegionTitleBranch nrt "
							+ "inner join tblRegionTitleBranch rt on rt.fkNormalizedRegionTitleId = nrt.normalizedRegionTitleId "
							+ "and rt.regionTitleId = " + editMap.getId().getFkRegionTitleId() + " "
							+ "inner join tblNormalizedRegionTitleUseBranch mnru on mnru.fkNormalizedRegionTitleId = rt.fkNormalizedRegionTitleId "
							+ "inner join tblRegionUseBranch ru on ru.regionUseId = mnru.fkRegionUseId "
							+ "order by rt_title,nrt_title,ru.description asc";
					List<Object> titleSearchList = getQueryOutput(query);
					// pagination end

					for (Iterator searchItr = titleSearchList.iterator(); searchItr.hasNext();) {
						Object[] object = (Object[]) searchItr.next();

						editList.add(new JSONObject().put("editId", editId).put("action", action)
								.put("regionTitle", object[1]).put("normalizedRegionTitle", object[0])
								.put("ncid", object[2]).put("description", object[3]));

					}
				}
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Edit").put("count", entrySize).put("totalMetric", elapsedTime + " seconds.")
					.put("edits", new JSONArray(editList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}

		return responseJson;
	}

	public JSONObject loadEditByBranch(JSONObject json) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> editList = new ArrayList<JSONObject>();

		try {

			Integer branchId = json.getInt("branchId");

			String query = "select regTitle.regionTitleId , regTitle.title regionTitle, norRegTitle.title norRegionTitle, "
					+ "group_concat(distinct(regUse.description)) regUse ,"
					+ " regionEdit.action action, regionEdit.editId editId, norRegTitle.normalizedRegionTitleId normalizedRegionTitleId, "
					+ " group_concat(distinct (regUse.regionUseId)) regUsesId, regionEdit.status, regionEdit.fkTestId " + "FROM tblRegionTitleBranch regTitle "
					+ "JOIN tblNormalizedRegionTitleBranch norRegTitle on regTitle.fkNormalizedRegionTitleId = norRegTitle.normalizedRegionTitleId "
					+ "JOIN tblNormalizedRegionTitleUseBranch map on map.fkNormalizedRegionTitleId = norRegTitle.normalizedRegionTitleId "
					+ "JOIN tblRegionUseBranch regUse on regUse.regionUseId = map.fkRegionUseId "
					+ "JOIN tblRegionEditMapping regEditMapping on regEditMapping.fkRegionTitleId = regTitle.regionTitleId "
					+ "JOIN tblRegionEdit regionEdit on regionEdit.editId = regEditMapping.fkEditId "
					+ "JOIN tblBranch branch on branch.branchId = regionEdit.fkBranchId " + "WHERE branch.branchId = '"
					+ branchId + "' group by regTitle.regionTitleId , regTitle.title , norRegTitle.title , "
					+ "regionEdit.action , regionEdit.editId , norRegTitle.normalizedRegionTitleId,regionEdit.status,regionEdit.fkTestId ";

			List<Object> editedTitleList = getQueryOutput(query);
			// pagination end
			for (Iterator searchItr = editedTitleList.iterator(); searchItr.hasNext();) {
				Object[] object = (Object[]) searchItr.next();

				List<JSONObject> useArr = new ArrayList<JSONObject>();
				Object regionUses = object[3];
				Object regionUseIds = object[7];
				StringTokenizer st = new StringTokenizer(regionUses.toString(), ",");
				StringTokenizer reguseIds = new StringTokenizer(regionUseIds.toString(), ",");

				while (st.hasMoreElements()) {
					String regionUse = (String) st.nextElement();
					Integer reId = Integer.parseInt((String) reguseIds.nextElement());
					useArr.add(new JSONObject().put("regionUseId", reId).put("regionUse", regionUse));
				}
				int editAction = Integer.parseInt(object[4].toString());

				String actionStr = getEditedActionStr(editAction);
				String status = getEditStatus(Byte.valueOf(object[8].toString()));

				editList.add(new JSONObject().put("regionTitleId", object[0]).put("regionTitle", object[1])
						.put("normalizedRegionTitle", object[2]).put("regionUseArr", useArr).put("action", actionStr).put("status", status)
						.put("testId",Integer.parseInt(object[9].toString()))
						.put("editId", object[5]).put("normalizedRegionTitleId", object[6]));

			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Edit").put("count", editList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("edits", new JSONArray(editList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;

	}

	public JSONObject loadRegionUsesById(JSONObject json) {
		// TODO Auto-generated method stub

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		// Prep work

		try {
			Query regionUseMapQuery = null;
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			query = "SELECT tnrtu.fkNormalizedRegionTitleId as normalizedRegionTitleId, tnrt.title from tblNormalizedRegionTitleUse tnrtu "
					+ " inner join tblNormalizedRegionTitle tnrt on tnrtu.fkNormalizedRegionTitleId = tnrt.id "
					+ " where tnrtu.fkRegionUseId = " + json.getInt("regionUseId")
					+ " order by tnrtu.fkNormalizedRegionTitleId" + " limit " + offset + ", " + (limit + 10) + ";";
			List<Object> regionUseList = getQueryOutput(query);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = regionUseList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] nrObjectIds = (Object[]) searchItr.next();

				titleOutputList.add(new JSONObject().put("normalizedRegionTitle", nrObjectIds[1])
						.put("normalizedRegionTitleId", nrObjectIds[0]));
				limitIdx++;
			}

			responseJson.put("returnType", "NormalizedRegionUse").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return responseJson;

	}

	public JSONObject loadEditByIds(JSONArray editIdArr) {
		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> editList = new ArrayList<JSONObject>();

		try {

			String editIdStr = "";
			for (Object editIdObj : editIdArr) {

				int editId = (Integer) editIdObj;
				if (editIdStr != "") {
					editIdStr = editIdStr + ",";
				}
				editIdStr += "'" + editId + "'";
			}

			editIdStr = "(" + editIdStr + ")";

			String query = "select regTitle.regionTitleId titleId, regTitle.title regionTitle, norRegTitle.title norRegionTitle, "
					+ "group_concat(distinct(regUse.description)) regUse , regionEdit.action action, regionEdit.editId editId, norRegTitle.normalizedRegionTitleId normalizedRegionTitleId "
					+ ", group_concat(distinct (regUse.regionUseId)) regUsesId " + "from tblRegionTitleBranch regTitle "
					+ "JOIN tblNormalizedRegionTitleBranch norRegTitle on regTitle.fkNormalizedRegionTitleId = norRegTitle.normalizedRegionTitleId "
					+ "JOIN tblNormalizedRegionTitleUseBranch map on map.fkNormalizedRegionTitleId = norRegTitle.NormalizedRegionTitleId "
					+ "JOIN tblRegionUseBranch regUse on regUse.regionUseId = map.fkRegionUseId "
					+ "JOIN tblRegionEditMapping regEditMapping on regEditMapping.fkRegionTitleId = regTitle.regionTitleId "
					+ "JOIN tblRegionEdit regionEdit on regionEdit.editId = regEditMapping.fkEditId "
					+ "JOIN tblBranch branch on branch.branchId = regionEdit.fkBranchId "
					+ "where regionEdit.editId in " + editIdStr
					+ " group by regTitle.regionTitleId,regTitle.title,norRegTitle.title,"
					+ "regionEdit.action, regionEdit.editId , norRegTitle.normalizedRegionTitleId ";

			List<Object> editedTitleList = getQueryOutput(query);
			// pagination end
			for (Iterator searchItr = editedTitleList.iterator(); searchItr.hasNext();) {
				Object[] object = (Object[]) searchItr.next();

				List<JSONObject> useArr = new ArrayList<JSONObject>();
				Object regionUses = object[3];
				Object regionUseIds = object[7];
				StringTokenizer st = new StringTokenizer(regionUses.toString(), ",");
				StringTokenizer reguseIds = new StringTokenizer(regionUseIds.toString(), ",");

				while (st.hasMoreElements()) {
					String regionUse = (String) st.nextElement();
					Integer reId = Integer.parseInt((String) reguseIds.nextElement());
					useArr.add(new JSONObject().put("regionUseId", reId).put("regionUse", regionUse));
				}

				int editAction = Integer.parseInt(object[4].toString());
				String actionStr = getEditedActionStr(editAction);

				editList.add(new JSONObject().put("regionTitleId", object[0]).put("regionTitle", object[1])
						.put("normalizedRegionTitle", object[2]).put("regionUseArr", useArr).put("action", actionStr)
						.put("editId", object[5]).put("normalizedRegionTitleId", object[6]));

			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			responseJson.put("returnType", "Edit").put("count", editList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("edits", new JSONArray(editList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}

		return responseJson;

	}

	public JSONObject createBranchBoilerplate(JSONArray boilerplateRequestArr, Integer branchId) {

		JSONObject responseJson = new JSONObject();
		try {
			for (Object reqObject : boilerplateRequestArr) {
				BoilerplateRegionBranch boilerPlateRegionObj = null;
				JSONObject reqJsonObj = (JSONObject) reqObject;
				String start = reqJsonObj.getString("start");
				boolean startRegex = reqJsonObj.getBoolean("startRegex");
				String end = reqJsonObj.getString("end");
				boolean endRegex = reqJsonObj.getBoolean("endRegex");
				String wholePhrase = reqJsonObj.getString("wholePhrase");
				boolean wholePhraseRegex = reqJsonObj.getBoolean("wholePhraseRegex");
				boolean exactWhitespace = reqJsonObj.getBoolean("exactWhitespace");
				byte action = getEditedActionByte(reqJsonObj.getString("action"));
				byte status = getEditStatusCode(STATUS_NOTTESTED);

				Query query = entityManager
						.createNativeQuery("SELECT fkTestId FROM tblRegionEdit WHERE fkBranchId =" + branchId);
				List<Object> testIdObj = query.getResultList();
				int testId = -1;

				if (testIdObj.size() > 0) {
					testId = Integer.parseInt(testIdObj.get(0).toString());
				}
				
				
				if (testId == -1) {
					List<Object> regBranchList = getNativeQueryOutput("select tbm.branchId from "
							+ "(select tbm.branchMapId from tblBranch tb "
							+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId = " + branchId
							+ " ) output "
							+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
							+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + DICTIONARIES_TYPE + "' ");
					// System.out.println(dicBranchList.get(0));
					List<Object> editPropList = getQueryOutput(
							"from Edit where fkBranchId in (:branchIds) ",
							new String[] { "branchIds"},
							new Object[] { regBranchList });

					if(editPropList.size() > 0) {
						testId  = ((Edit)editPropList.get(0)).getTestId();
					}
				}
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


				}


				Integer parentBoilerplateId = null;
				if (reqJsonObj.has("parentBoilerplateId")) {
					parentBoilerplateId = reqJsonObj.getInt("parentBoilerplateId");
				}

				if (reqJsonObj.has("boilerplateRegionId")) {
					int boilerplateRegionId = reqJsonObj.getInt("boilerplateRegionId");
					boilerPlateRegionObj = entityManager.find(BoilerplateRegionBranch.class, boilerplateRegionId);
					boilerPlateRegionObj.setAllBoilerplateValues(start, end, startRegex, endRegex, wholePhrase,
							wholePhraseRegex, exactWhitespace, parentBoilerplateId, branchId, action,testId,status);

				} else {
					boilerPlateRegionObj = new BoilerplateRegionBranch();
					boilerPlateRegionObj.setAllBoilerplateValues(start, end, startRegex, endRegex, wholePhrase,
							wholePhraseRegex, exactWhitespace, parentBoilerplateId, branchId, action,testId,status);
					entityManager.persist(boilerPlateRegionObj);
				}

			}
			entityManager.flush();
			responseJson.put("message", "Created Successfully");
		} catch (Exception ex) {
			throw ex;
		}

		return responseJson;
	}

	public JSONObject createNormalizedRegionTitles(JSONObject json) {
		JSONObject output = new JSONObject().put("message", ADDED_SUCCESS);

		EntityTransaction tx = entityManager.getTransaction();
		try {
			// HQL example - Create Branch
			// regionTitle, normalregiontitle, ncid, description

			String query = "from NormalizedRegionTitleBranch where title =:title ";
			List<Object> branchResultList = getQueryOutput(query, new String[] { "title" },
					new Object[] { json.getString("normalizedRegionTitle") });
			if (branchResultList.size() == 0) {

				NormalizedRegionTitleBranch regionTitleBranch = new NormalizedRegionTitleBranch();
				regionTitleBranch.setTitle(json.getString("normalizedRegionTitle"));
				entityManager.persist(regionTitleBranch);
				entityManager.flush();

				RegionEdit trEdit = new RegionEdit();
				// trEdit.setFkBranchId(json.getInt("branchId"));
				trEdit.setStatus(getStatusCode(STATUS_ACTIVE));
				if ("Add".equalsIgnoreCase(json.getString("action"))) {
					trEdit.setAction(Byte.parseByte("1"));
				}

				Date createdDate = new Date();
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date gmt = new Date(sdf.format(createdDate));
				trEdit.setCreatedOn(gmt);
				entityManager.persist(trEdit);
				entityManager.flush();
				int editId = trEdit.getEditId();

				output.put("normalizedRegionTitleId", regionTitleBranch.getNormalizedRegionTitleId());
				output.put("editId", editId);

			} else {
				output.put("message", "NoramalizedRegion Title name is already exists");
			}

		} catch (Exception e) {
			output.put("message", e.getMessage());
		}
		tx.commit();
		return output;

	}

	public JSONObject loadRegionUsesByNormalizedId(JSONObject json) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		try {
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			String query = "SELECT ru.id, ru.ncid, ru.description FROM tblRegionUse ru"
					+ " inner join tblNormalizedRegionTitleUse nrtu on nrtu.fkRegionUseId  = ru.id "
					+ " and nrtu.fkNormalizedRegionTitleId = " + json.getInt("normalizedRegionId")
					+ " order by ru.id asc " + " limit " + offset + ", " + (limit + 10) + ";";
			List<Object> regionUseList = getQueryOutput(query);
			// pagination
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = regionUseList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				titleOutputList.add(new JSONObject().put("regionUseId", object[0]).put("ncid", object[1])
						.put("description", object[2]));
				limitIdx++;
			}

			responseJson.put("returnType", "RegionUse").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);
	}

	public JSONObject updateBranchEdit(JSONObject regionJson) {
		// TODO Auto-generated method stub
		// logger.info("Start");
		JSONObject output = new JSONObject().put("message", UPDATED_SUCCESS);
		try {

			JSONObject regionTitleJson = ((JSONObject) regionJson).getJSONObject("regionTitle");
			JSONObject nRegionTitleJson = ((JSONObject) regionJson).getJSONObject("normalizedRegionTitle");
			int regionTitleId = regionTitleJson.getInt("regionTitleId");
			int normalizedTitleId = nRegionTitleJson.getInt("normalizedRegionTitleId");
			int generatedRegionTitleId = -1;
			String normalizedTitle = nRegionTitleJson.getString("title");
			String regionTitle = regionTitleJson.getString("title");
			int existRegionTitleId = -1;
			int editId = regionJson.getInt("editId");

			if ("Edit".equalsIgnoreCase(((JSONObject) regionJson).getString("action"))) {

				JSONArray regionUseListArr = ((JSONObject) regionJson).getJSONArray("regionUses");
				if (regionTitleId > 0) {

					List<Object> regionResultList = getQueryOutput("from RegionEditMapping where fkEditId =:editId",
							new String[] { "editId" }, new Object[] { editId });

					if (regionResultList.size() == 0) {
						throw new Exception("Invalid RegionTitle");
					}
					existRegionTitleId = ((RegionEditMapping) regionResultList.get(0)).getId().getFkRegionTitleId();
				}

				if (regionTitleId != existRegionTitleId) {

					if (normalizedTitleId > 0) {
						List<Object> nRegionResultList = getQueryOutput(
								"from NormalizedRegionTitle where id =:normalizedRegionTitleId ",
								new String[] { "normalizedRegionTitleId" }, new Object[] { normalizedTitleId });

						if (nRegionResultList.size() == 0) {
							throw new Exception("Invalid NormalizedRegionTitle");
						}
					}

					if (regionTitleId > 0) {
						List<Object> nRegionResultList = getQueryOutput("from RegionTitle where id =:regionTitleId ",
								new String[] { "regionTitleId" }, new Object[] { regionTitleId });

						if (nRegionResultList.size() == 0) {
							throw new Exception("Invalid RegionTitle");
						}

						if (!((RegionTitle) nRegionResultList.get(0)).getTitle().equals(regionTitle)) {

							nRegionResultList = getQueryOutput("from RegionTitle where title =:title ",
									new String[] { "title" }, new Object[] { regionTitle });

							if (nRegionResultList.size() > 0) {
								throw new Exception("RegionTitle Already Exists");
							}
						}
					}

					NormalizedRegionTitleBranch tnrtBranch = new NormalizedRegionTitleBranch(normalizedTitle);
					entityManager.persist(tnrtBranch);
					entityManager.flush();
					normalizedTitleId = tnrtBranch.getNormalizedRegionTitleId();

					RegionTitleBranch trBranch = new RegionTitleBranch();
					trBranch.setFkNormalizedRegionTitleId(normalizedTitleId);
					trBranch.setTitle(regionTitle);

					entityManager.persist(trBranch);
					entityManager.flush();
					generatedRegionTitleId = trBranch.getRegionTitleId();
				} else {

					if (nRegionTitleJson.getInt("normalizedRegionTitleId") > 0) {
						List<Object> branchNResultList = getQueryOutput(
								"from RegionTitleBranch where regionTitleId =:regionTitleId ",
								new String[] { "regionTitleId" }, new Object[] { regionTitleId });

						if (branchNResultList.size() == 0) {
							throw new Exception("Invalid RegionTitleBranch");
						} else {

							if (normalizedTitleId != ((RegionTitleBranch) branchNResultList.get(0))
									.getFkNormalizedRegionTitleId()) {
								List<Object> nRegionResultList = getQueryOutput(
										"from NormalizedRegionTitle where id =:normalizedRegionTitleId ",
										new String[] { "normalizedRegionTitleId" }, new Object[] { normalizedTitleId });

								if (nRegionResultList.size() == 0) {
									throw new Exception("Invalid NormalizedRegionTitle");
								}

								NormalizedRegionTitleBranch tnrtBranch = new NormalizedRegionTitleBranch(
										normalizedTitle);
								entityManager.persist(tnrtBranch);
								entityManager.flush();
								normalizedTitleId = tnrtBranch.getNormalizedRegionTitleId();

							}
						}
					} else {
						NormalizedRegionTitleBranch tnrtBranch = new NormalizedRegionTitleBranch(normalizedTitle);
						entityManager.persist(tnrtBranch);
						entityManager.flush();
						normalizedTitleId = tnrtBranch.getNormalizedRegionTitleId();

					}

					generatedRegionTitleId = existRegionTitleId;

					Query editMappingDelQuery = entityManager.createNativeQuery(
							"update tblRegionTitleBranch set fkNormalizedRegionTitleId=:fkNormalizedRegionTitleId, title=:title where regionTitleId =:regionTitleId ");
					editMappingDelQuery.setParameter("fkNormalizedRegionTitleId", normalizedTitleId);
					editMappingDelQuery.setParameter("title", regionTitle);
					editMappingDelQuery.setParameter("regionTitleId", generatedRegionTitleId);
					editMappingDelQuery.executeUpdate();
				}

				if (regionUseListArr.length() == 0) {
					regionUseListArr.put(new JSONObject().put("regionUseId", -1).put("description", "Default"));
				}
				for (Object regionUse : regionUseListArr) {
					int regionUseId = ((JSONObject) regionUse).getInt("regionUseId");
					String description = ((JSONObject) regionUse).getString("description");

					/*
					 * if (regionUseId > 0) { Query regionUseQuery = session
					 * .createQuery("from RegionUse where regionUseId =:regionUseId "
					 * ); regionUseQuery.setInteger("regionUseId", regionUseId);
					 * List<RegionUse> regionUseResultList =
					 * regionUseQuery.list();
					 * 
					 * if (regionUseResultList.size() == 0) { throw new
					 * Exception("No RegionUse"); } }
					 */

					RegionUseBranch tregionuse = new RegionUseBranch();
					tregionuse.setDescription(description);
					if (!((JSONObject) regionUse).isNull("ncid")) {
						tregionuse.setNcid(((JSONObject) regionUse).getString("ncid"));
					}

					// logger.info("Going to persist RegionUseBranch "+editId);
					entityManager.persist(tregionuse);
					entityManager.flush();

					regionUseId = tregionuse.getRegionUseId();
					NormalizedRegionTitleUseBranch tnrtuBranch = new NormalizedRegionTitleUseBranch(
							new NormalizedRegionTitleUseBranchId(normalizedTitleId, regionUseId));
					entityManager.persist(tnrtuBranch);
					entityManager.flush();

				}

				List<Object> regionResultList = getQueryOutput("from RegionEditMapping where fkEditId =:editId",
						new String[] { "editId" }, new Object[] { editId });

				if (regionResultList.size() > 0) {

					for (Iterator iterator = regionResultList.iterator(); iterator.hasNext();) {
						RegionEditMapping tblRegionEditMapping = (RegionEditMapping) iterator.next();
						// logger.info(tblRegionEditMapping.getId().getFkRegionTitleId()+
						// " "+tblRegionEditMapping.getId().getFkEditId());
						entityManager.remove(tblRegionEditMapping);
						entityManager.flush();
					}

				}

				RegionEditMapping treMapping = new RegionEditMapping(
						new RegionEditMappingId(editId, generatedRegionTitleId));
				entityManager.persist(treMapping);
				entityManager.flush();

			} else {
				throw new Exception("Invalid Action");
			}

		} catch (Exception e) {
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

			List<Object> regBranchList = getNativeQueryOutput("select tbm.branchId from "
					+ "(select tbm.branchMapId from tblBranch tb "
					+ "inner join tblBranchMap tbm ON tb.branchId = tbm.branchId " + "where tb.branchId in " + branchIds
					+ " and tb.status = 3) output "
					+ "inner join tblBranchMap tbm ON output.branchMapId = tbm.branchMapId "
					+ "inner join tblBranch tb on tbm.branchId = tb.branchId and tb.type = '" + REGIONS_TYPE + "' ");
			// System.out.println(dicBranchList.get(0));
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
			boolean isReleased = false;

			if (branchPropList.size() > 0) {

				for (Iterator branchItr = branchPropList.iterator(); branchItr.hasNext();) {
					Branch branch = (Branch) branchItr.next();

					int branchId = branch.getBranchId();
					editPropList = getQueryOutput("from RegionEdit where fkBranchId = :branchId and status = :status ",
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
							for (Iterator regionBPropItr = regionBraPropList.iterator(); regionBPropItr.hasNext();) {
								RegionTitleBranch regionTitleBranch = (RegionTitleBranch) regionBPropItr.next();
								Integer normId = regionTitleBranch.getFkNormalizedRegionTitleId();
								String title = regionTitleBranch.getTitle();
								if (ACTION_DELETE.equals(getEditedActionStr(edit.getAction()))) {
									Integer orginalRegionTitleId = regionTitleBranch.getMainRegionTitleId();
									isReleased = true;
									continue;
								}
								List<Object> regionPropList = null;
								if (ACTION_EDIT.equals(getEditedActionStr(edit.getAction()))) {
									Integer orginalRegionTitleId = regionTitleBranch.getMainRegionTitleId();
									regionPropList = getQueryOutput("from RegionTitle where  id = :regionTitleId ",
											new String[] { "regionTitleId" }, new Object[] { orginalRegionTitleId });
								} else {
									regionPropList = new ArrayList<Object>();
									regionPropList.add(new RegionTitle());
								}

								{
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
						Query branchUpdateQuery = entityManager
								.createQuery("update Branch set status = :status where branchId = :branchId");
						branchUpdateQuery.setParameter("status", getStatusCode(STATUS_MERGED));
						branchUpdateQuery.setParameter("branchId", branchId);
						Integer isUpdated = branchUpdateQuery.executeUpdate();
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

	public JSONObject loadBoilerplates(JSONObject json) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		try {
			String query = "";
			Integer page = json.optInt("page", 1);
			Integer limit = json.optInt("limit", 10);// default limit 10;
			Integer offset = (page - 1) * limit;
			String rtWhereClause = "";
			if (!json.isNull("value")) {
				rtWhereClause = " where lower(bo.start) like lower('%" + json.getString("value") + "%') "
						+ "OR lower(bo.end) like lower('%" + json.getString("value") + "%') ";
			} else {
				throw new Exception("Invalid value search");
			}
			query = "select id as boilerplateRegionId, start, startRegex, end, endRegex, wholePhrase, wholePhraseRegex, exactWhitespace from tblBoilerplateRegion bo "
					+ rtWhereClause + " order by bo.start, bo.end" + " limit " + offset + ", " + (limit + 10) + ";";

			// logger.info(query);

			List<Object> titleList = getQueryOutput(query);

			// logger.info("titleList : " + titleList);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				// logger.info("object : " + object);

				titleOutputList.add(new JSONObject().put("boilerplateRegionId", object[0]).put("start", object[1])
						.put("startRegex", object[2]).put("end", object[3]).put("endRegex", object[4])
						.put("wholePhrase", object[5]).put("wholePhraseRegex", object[6])
						.put("exactWhitespace", object[7]));
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			// logger.info("titleOutputList : " + titleOutputList);

			responseJson.put("returnType", "BoilerplateRegion").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("regions", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);

	}

	/**
	 * Return the branch having the passed id.
	 */
	public Branch getById(long id) {
		return entityManager.find(Branch.class, id);
	}

	// An EntityManager will be automatically injected from entityManagerFactory
	// setup on DatabaseConfig class.
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

	private String getEditedActionStr(int editAction) {
		String action = "Add";
		if (editAction == 2) {
			action = "Edit";

		} else if (editAction == 3) {
			action = "Delete";

		}
		return action;
	}

	private byte getEditedActionByte(String editAction) {
		byte action = 1;
		if (editAction == "Edit") {
			action = 2;

		} else if (editAction == "Delete") {
			action = 3;

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

	private List<Object> getQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createNativeQuery(query);

		return regionTitleQuery.getResultList();
	}

	public String postDataForTest(String testRequestJson) {

		String testSumbitUrl = env.getProperty("test.submitUrl");
		RestClient restClient = new RestClient();
		String response = restClient.post(testSumbitUrl, testRequestJson);

		return response;
	}

	public JSONObject getBranchBoilerplates(Integer branchId, Integer pageVal, Integer limitVal) {

		long startTime = System.currentTimeMillis();
		JSONObject responseJson = new JSONObject();

		List<JSONObject> titleOutputList = new ArrayList<JSONObject>();

		try {
			String query = "";
			Integer page = 1;
			Integer limit = 10;
			if (page != null) {
				page = pageVal;
			}
			if (limit != null) {
				limit = limitVal;
			}

			Integer offset = (page - 1) * limit;
			String rtWhereClause = "";

			query = "select boilerplateRegionId, start, startRegex, end, endRegex, wholePhrase, wholePhraseRegex, exactWhitespace, action"
					+ ", fkBoilerplateRegionId, fkTestId " + ", status " + "from tblBoilerplateRegionBranch bo "
					+ "where fkBranchId = " + branchId + " order by bo.start, bo.end" + " limit " + offset + ", "
					+ (limit + 10) + ";";

			// logger.info(query);

			List<Object> titleList = getQueryOutput(query);

			// logger.info("titleList : " + titleList);
			// pagination end
			int limitIdx = 0;
			responseJson.put("next", false);
			for (Iterator searchItr = titleList.iterator(); searchItr.hasNext();) {

				if (limitIdx == limit) {
					responseJson.put("next", true);
					break;
				}
				Object[] object = (Object[]) searchItr.next();

				// logger.info("object : " + object);

				String value = getEditedActionStr((Byte) object[8]);
				titleOutputList.add(new JSONObject().put("boilerplateRegionId", object[0]).put("start", object[1])
						.put("startRegex", object[2]).put("end", object[3]).put("endRegex", object[4])
						.put("wholePhrase", object[5]).put("wholePhraseRegex", object[6])
						.put("exactWhitespace", object[7]).put("action", value).put("parentBoilerplateId", object[9])
						.put("testId", object[10]).put("status", getEditStatus(Byte.valueOf(object[11].toString())))

				);
				limitIdx++;
			}
			long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

			// logger.info("titleOutputList : " + titleOutputList);

			responseJson.put("returnType", "BoilerplateRegion").put("count", titleOutputList.size())
					.put("totalMetric", elapsedTime + " seconds.").put("boilerplates", new JSONArray(titleOutputList));
		} catch (Exception e) {
			responseJson.put("message", e.getMessage());
		}
		return (responseJson);

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
						"update RegionEdit set lastUpdatedOn = :lastUpdatedOn , status = :status where editId in (:workspaceIds)");

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


	private List<Object> getNativeQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createNativeQuery(query);

		return regionTitleQuery.getResultList();
	}

	public JSONObject deleteBoilerplatesRegion(int boilerplateRegionId) {
		JSONObject output = new JSONObject().put("message", DELETED_SUCCESS);
		Query editMappingDelQuery = entityManager
				.createNativeQuery("delete from tblBoilerplateRegionBranch where boilerplateRegionId =:boilerplateRegionId ");
		editMappingDelQuery.setParameter("boilerplateRegionId", boilerplateRegionId);
		editMappingDelQuery.executeUpdate();
		entityManager.flush();

		return output;

	}

}
