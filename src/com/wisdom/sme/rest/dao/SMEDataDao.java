package com.wisdom.sme.rest.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import com.wisdom.common.domain.Branch;
import com.wisdom.common.domain.Release;
import com.wisdom.region.rest.domain.RegionEdit;
import com.wisdom.region.rest.domain.RegionUse;
import com.wisdom.region.test.model.Adhoc;
import com.wisdom.region.test.model.DeltaRegionRules;
import com.wisdom.region.test.model.NormalizedTitle2UseMapping;
import com.wisdom.region.test.model.RegionRules;
import com.wisdom.region.test.model.RegionRulesDelta;
import com.wisdom.region.test.model.TestData;
import com.wisdom.region.test.model.TestRequestModel;
import com.wisdom.region.test.model.TestWithRelease;
import com.wisdom.region.test.model.Title2NormalizedTitleMapping;

@Repository
@Transactional
public class SMEDataDao {

	public TestWithRelease loadEdit(JSONObject json) {

		List<Object> rEditPropList = (List<Object>) getQueryOutput("from RegionEdit where fkBranchId = :branchId",
				new String[] { "branchId" }, new Object[] { json.getInt("branchId") });

		RegionRulesDelta regionRulesDelta = new RegionRulesDelta();
		RegionRules newRules = new RegionRules();
		RegionRules retiredRules = new RegionRules();
		TestWithRelease testWithRelease = new TestWithRelease();
		TestRequestModel testRequestModel = new TestRequestModel();

		// List<Title2NormalizedTitleMapping>
		// title2NormalizedTitleMappings = new
		// ArrayList<Title2NormalizedTitleMapping>();
		Integer testId = null;
		boolean isNewRule = true;
		boolean isAvailable = false;
		
		List<Branch> branchPropList = (List<Branch>) getQueryOutput("from Branch where branchId = :branchId ",
				new String[] { "branchId" }, new Object[] { json.get("branchId") });
		testWithRelease.setStatus(404);
		testWithRelease.setMessage("BranchId not found");
		
		if(branchPropList.size() > 0) {
			testWithRelease.setStatus(200);
			testWithRelease.setMessage("Edits not found");
			
		}
		
		
		for (Iterator reditPropItr = rEditPropList.iterator(); reditPropItr.hasNext();) {
			testWithRelease.setStatus(200);
			testWithRelease.setMessage("Data Found");
			
			RegionEdit editObject = (RegionEdit) reditPropItr.next();
			if (getEditedActionStr(editObject.getAction()).toUpperCase().equals("DELETE")) {
				isNewRule = false;
			}
			testId = editObject.getFkTestId();
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
						testRequestModel.setRegionRulesDelta(regionRulesDelta);
						isAvailable = true;
					} else {
						retiredRules.getNormalizedTitle2UseMappings().add(normalizedTitle2UseMapping);
						regionRulesDelta.setRetiredRules(retiredRules);
						testRequestModel.setRegionRulesDelta(regionRulesDelta);
						isAvailable = true;

					}

				}

			}

			List<Object> boilerplateList = getNativeQueryOutput(
					"SELECT fkBoilerplateRegionId,start,startRegex,end,endRegex,wholePhrase,wholePhraseRegex,exactWhitespace,action FROM tblBoilerplateRegionBranch where fkBranchId =  "
							+ editObject.getFkBranchId());

			for (Iterator boilerplateItr = boilerplateList.iterator(); boilerplateItr.hasNext();) {
				Object[] object4 = (Object[]) boilerplateItr.next();

				com.wisdom.region.test.model.BoilerplateRegion boilerplate = new com.wisdom.region.test.model.BoilerplateRegion();
				boilerplate.setId(Integer.parseInt(String.valueOf(object4[0])));
				// boilerplate.setEnterpriseId(String.valueOf(object4[0]));
				boilerplate.setStart(String.valueOf(object4[1]));
				boilerplate.setIsStartRegex(Boolean.parseBoolean(String.valueOf(object4[2])));
				boilerplate.setEnd(String.valueOf(object4[3]));
				boilerplate.setIsEndRegex(Boolean.parseBoolean(String.valueOf(object4[4])));
				boilerplate.setWholePhrase(String.valueOf(object4[5]));
				boilerplate.setIsWholePhraseRegex(Boolean.parseBoolean(String.valueOf(object4[6])));
				boilerplate.setIsExactWhiteSpace(Boolean.parseBoolean(String.valueOf(object4[7])));
				if (getEditedActionStr(Integer.parseInt(String.valueOf(object4[7]))).toUpperCase().equals("DELETE")) {
					retiredRules.getBoilerplateRegions().add(boilerplate);
					regionRulesDelta.setRetiredRules(retiredRules);
					testRequestModel.setRegionRulesDelta(regionRulesDelta);
					isAvailable = true;
				} else {
					newRules.getBoilerplateRegions().add(boilerplate);
					regionRulesDelta.setNewRules(newRules);
					testRequestModel.setRegionRulesDelta(regionRulesDelta);
					isAvailable = true;
				}
			}

		}

		if (isAvailable) {
			TestData testData = new TestData();
			Adhoc adhoc = new Adhoc();
			adhoc.setDocument("Test Document");
			adhoc.setTestId(testId);
			List<Adhoc> adhocs = new ArrayList<Adhoc>();
			adhocs.add(adhoc);
			testData.setAdhoc(adhocs);
			testRequestModel.setTestData(testData);
			testRequestModel.setId(testId);
			testWithRelease.setTestRequestModel(testRequestModel);
			Query releaseQuery = entityManager.createQuery(
					"SELECT release FROM Release release WHERE release.releaseId = (SELECT MAX(release2.releaseId) FROM Release release2)");
			List<Release> releaseOb = releaseQuery.getResultList();

			String stableRealeaseNumber = Byte.toString(releaseOb.get(0).getMajor()) + "."
					+ Byte.toString(releaseOb.get(0).getMinor()) + "." + Byte.toString(releaseOb.get(0).getPatch());
			testWithRelease.setReleaseNumber(stableRealeaseNumber);
		}else {
			if(testWithRelease.getStatus() == 200){
				testWithRelease.setMessage("Edits are not found");
			}
			

		}
		return testWithRelease;
	}

	public DeltaRegionRules loadPreReleaseData() {
		DeltaRegionRules newRules = new DeltaRegionRules();

		// List<Title2NormalizedTitleMapping>
		// title2NormalizedTitleMappings = new
		// ArrayList<Title2NormalizedTitleMapping>();
		boolean isAvailable = false;
		List<Object> regionList = getNativeQueryOutput(
				"select trtb.id as id,  trtb.title as regionTitle, tnrtb.title as NormalizedTitle, trtb.fkNormalizedRegionTitleId from tblRegionTitle trtb "
						+ "inner join tblNormalizedRegionTitle tnrtb on trtb.fkNormalizedRegionTitleId = tnrtb.id ");

		for (Iterator iterator = regionList.iterator(); iterator.hasNext();) {
			Object[] object2 = (Object[]) iterator.next();
			Title2NormalizedTitleMapping titlenormalizedTitleMapping = new Title2NormalizedTitleMapping();
			titlenormalizedTitleMapping.setId(Integer.parseInt(String.valueOf(object2[0])));
			titlenormalizedTitleMapping.setNormalizedTitle(String.valueOf(object2[2]));
			titlenormalizedTitleMapping.setRawTitle(String.valueOf(object2[1]));
			newRules.getTitle2NormalizedTitleMappings().add(titlenormalizedTitleMapping);
		}
		List<Object> regionUList = getNativeQueryOutput(
				"select distinct tnrtub.fkNormalizedRegionTitleId, tnrtb.title,trb.ncid  from tblNormalizedRegionTitleUse tnrtub "
						+ "inner join tblNormalizedRegionTitle tnrtb on tnrtub.fkNormalizedRegionTitleId = tnrtb.id "
						+ " inner join tblRegionUse trb on tnrtub.fkRegionUseId = trb.id");

		for (Iterator regionUItr = regionUList.iterator(); regionUItr.hasNext();) {
			Object[] object3 = (Object[]) regionUItr.next();

			NormalizedTitle2UseMapping normalizedTitle2UseMapping = new NormalizedTitle2UseMapping();
			normalizedTitle2UseMapping.setId(Integer.parseInt(String.valueOf(object3[0])));
			normalizedTitle2UseMapping.setNcid(String.valueOf(object3[2]));
			normalizedTitle2UseMapping.setNormalizedTitle(String.valueOf(object3[1]));
			newRules.getNormalizedTitle2UseMappings().add(normalizedTitle2UseMapping);
			isAvailable = true;

		}

		List<Object> boilerplateList = getNativeQueryOutput(
				"SELECT id,start,startRegex,end,endRegex,wholePhrase,wholePhraseRegex,exactWhitespace FROM tblBoilerplateRegion ");

		for (Iterator boilerplateItr = boilerplateList.iterator(); boilerplateItr.hasNext();) {
			Object[] object4 = (Object[]) boilerplateItr.next();

			com.wisdom.region.test.model.BoilerplateRegion boilerplate = new com.wisdom.region.test.model.BoilerplateRegion();
			boilerplate.setId(Integer.parseInt(String.valueOf(object4[0])));
			// boilerplate.setEnterpriseId(String.valueOf(object4[0]));
			boilerplate.setStart(String.valueOf(object4[1]));
			boilerplate.setIsStartRegex(Boolean.parseBoolean(String.valueOf(object4[2])));
			boilerplate.setEnd(String.valueOf(object4[3]));
			boilerplate.setIsEndRegex(Boolean.parseBoolean(String.valueOf(object4[4])));
			boilerplate.setWholePhrase(String.valueOf(object4[5]));
			boilerplate.setIsWholePhraseRegex(Boolean.parseBoolean(String.valueOf(object4[6])));
			boilerplate.setIsExactWhiteSpace(Boolean.parseBoolean(String.valueOf(object4[7])));
			newRules.getBoilerplateRegions().add(boilerplate);
			isAvailable = true;

		}
		List<Object> consistentRegionUses = getQueryOutput(" select tru.ncid as specificNcid, tru2.ncid as generalNcid from tblConsistentRegionUse tcru "
				+ " inner join tblRegionUse tru on tcru.fkSpecificRegionUseId = tru.regionUseId "
				+ " inner join tblRegionUse tru2 on tcru.fkGeneralRegionUseId = tru2.regionUseId ");

		for (Iterator consistentRegionItr = consistentRegionUses.iterator(); consistentRegionItr.hasNext();) {
			Object[] object2 = (Object[]) consistentRegionItr.next();
			newRules.getConsistentUseMappings().put(String.valueOf(object2[1]), String.valueOf(object2[0]));
		}		

		List<RegionUse> regionUses = (List<RegionUse>) getQueryOutput(" from RegionUse ",null,null);

		for (Iterator regionUsesItr = regionUses.iterator(); regionUsesItr.hasNext();) {
			RegionUse regionsUses2 = (RegionUse) regionUsesItr.next();
			if(regionsUses2.getNcid() == null){
				regionsUses2.setNcid("");
			}
			newRules.getRegionUse2DescMapping().put(regionsUses2.getNcid(), regionsUses2.getDescription());
		}		
		
		if (isAvailable) {
			Query releaseQuery = entityManager.createQuery(
					"SELECT release FROM Release release WHERE release.releaseId = (SELECT MAX(release2.releaseId) FROM Release release2)");
			List<Release> releaseOb = releaseQuery.getResultList();

			String realeaseNumber = Byte.toString(releaseOb.get(0).getMajor()) + "."
					+ Byte.toString(releaseOb.get(0).getMinor()) + "." + Byte.toString(releaseOb.get(0).getPatch());
			newRules.setReleaseNumber(realeaseNumber);
		}
		
		return newRules;
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

	private List<Object> getQueryOutput(String query) {
		Query regionTitleQuery = entityManager.createNativeQuery(query);

		return regionTitleQuery.getResultList();
	}

	private Object getQueryOutput(String query, String[] params, Object[] values) {
		Query regionTitleQuery = entityManager.createQuery(query);
		if (params != null) {
			for (int qIdx = 0; qIdx < params.length; qIdx++) {
				regionTitleQuery.setParameter(params[qIdx], values[qIdx]);
			}
		}

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

	@PersistenceContext
	private EntityManager entityManager;

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

}
