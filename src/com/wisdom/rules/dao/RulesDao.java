package com.wisdom.rules.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wisdom.common.util.DBQueryUtil;
import com.wisdom.rules.entities.TblCodes;
import com.wisdom.rules.entities.TblMappingRules;
import com.wisdom.rules.entities.TblModifiers;
import com.wisdom.rules.entities.TblRegionUseRules;
import com.wisdom.rules.entities.TblSlots;
import com.wisdom.rules.entities.TblVerbFrameRules;
import com.wisdom.rules.mapper.RulesEntityToResponseMapper;
import com.wisdom.rules.model.MappingRule;
import com.wisdom.rules.model.VerbFrameRule;

@Repository
@Transactional
public class RulesDao {

	private static final String QUERY_SLOTS_BY_VALUE = "from TblSlots where value=:value";
	private static final String QUERY_CODES_BY_VALUE = "from TblCodes where value=:value";
	private static final String QUERY_REGIONUSE_BY_VALUE = "from TblRegionUseRules where value=:value";
	private static final String QUERY_MODIFIERS_BY_VALUE = "from TblModifiers where value=:value";

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private RulesEntityToResponseMapper mapper;
	
	@Autowired
	private DBQueryUtil dbUtil;

	public VerbFrameRule getVerbFrameRuleById(int id, int page) {
		TblVerbFrameRules tblVerbFrameRules = entityManager.find(TblVerbFrameRules.class, id);
		VerbFrameRule verbFrameRule = new VerbFrameRule();

		verbFrameRule.setId(tblVerbFrameRules.getId());
		verbFrameRule.setScope(tblVerbFrameRules.getScope());
		verbFrameRule.setAnyRegionUse(tblVerbFrameRules.getAnyRegionUse() == 1 ? true : false);
		Set<TblSlots> tblSlots = tblVerbFrameRules.getTblSlots();
		List<Object> slots = new ArrayList<Object>();
		for (TblSlots tblSlot : tblSlots) {
			slots.add(tblSlot.getValue());
		}
		verbFrameRule.setSlots(slots);
		return verbFrameRule;
	}

	public List<VerbFrameRule> getVerbFrameRules(String key, String value, int page) {

		Set<TblVerbFrameRules> tblVerbframeRules = getTblRules(key, value, TblVerbFrameRules.class);

		List<VerbFrameRule> verbFrameRulesResponse = mapper.transformVerbFrameRules(tblVerbframeRules, page);
		return verbFrameRulesResponse;
	}

	
	public List<MappingRule> getMappingRules(String key, String value, int page) {

		Set<TblMappingRules> tblMappingRules = getTblRules(key, value, TblMappingRules.class);

		List<MappingRule> mappingRulesResponse = mapper.transformMappingRules(tblMappingRules, page);
		return mappingRulesResponse;
	}
	
	public <T> Set<T> getTblRules(String key, String value, Class<T> rulesType) {

		Set<T> tblVerbframeRules = null;
		switch (key) {
		case "slot":
			tblVerbframeRules = getRulesBySlot(value, rulesType);
			break;
		case "code":
			tblVerbframeRules = getRulesByCode(value, rulesType);
			break;
		case "modifier":
			tblVerbframeRules = getRulesByModifier(value, rulesType);
			break;
		case "regionUse":
			tblVerbframeRules = getRulesByRegionUse(value, rulesType);
			break;
		default:
			break;
		}

		return tblVerbframeRules;
	}

	private <T> Set<T> getRulesByModifier(String value, Class<T> rulesType) {
		List<TblModifiers> queryOutput = dbUtil.getQueryOutput(QUERY_MODIFIERS_BY_VALUE, new String[] { "value" }, new Object[] { value },
				TblModifiers.class);
		TblModifiers tblModifiers = queryOutput.get(0);

		Set<T> tblVerbframeRules = null;

		if (rulesType == TblVerbFrameRules.class) {
			tblVerbframeRules = (Set<T>) tblModifiers.getTblVerbFrameRules();
		}

		if (rulesType == TblMappingRules.class) {
			tblVerbframeRules = (Set<T>) tblModifiers.getTblMappingRules();
		}

		return tblVerbframeRules;
	}

	
	

	private <T> Set<T> getRulesByRegionUse(String value, Class<T> rulesType) {
		List<TblRegionUseRules> queryOutput = dbUtil.getQueryOutput(QUERY_REGIONUSE_BY_VALUE, new String[] { "value" }, new Object[] { value },
				TblRegionUseRules.class);
		TblRegionUseRules tblRegionuseRules = queryOutput.get(0);
		Set<T> tblVerbframeRules = null;

		if (rulesType == TblVerbFrameRules.class) {
			tblVerbframeRules = (Set<T>) tblRegionuseRules.getTblVerbFrameRules();
		}

		if (rulesType == TblMappingRules.class) {
			tblVerbframeRules = (Set<T>) tblRegionuseRules.getTblMappingRules();
		}

		return tblVerbframeRules;
	}

	private <T> Set<T> getRulesByCode(String value, Class<T> rulesType) {
		List<TblCodes> queryOutput = dbUtil.getQueryOutput(QUERY_CODES_BY_VALUE, new String[] { "value" }, new Object[] { value }, TblCodes.class);
		TblCodes tblCodes = queryOutput.get(0);

		Set<T> tblVerbframeRules = null;

		if (rulesType == TblVerbFrameRules.class) {
			tblVerbframeRules = (Set<T>) tblCodes.getTblVerbFrameRules();
		}

		if (rulesType == TblMappingRules.class) {
			tblVerbframeRules = (Set<T>) tblCodes.getTblMappingRules();
		}

		return tblVerbframeRules;
	}

	private <T> Set<T> getRulesBySlot(String value, Class<T> rulesType) {
		List<TblSlots> queryOutput = dbUtil.getQueryOutput(QUERY_SLOTS_BY_VALUE, new String[] { "value" }, new Object[] { value }, TblSlots.class);
		TblSlots tblSlots = queryOutput.get(0);
		Set<T> tblVerbframeRules = null;

		if (rulesType == TblVerbFrameRules.class) {
			tblVerbframeRules = (Set<T>) tblSlots.getTblVerbFrameRules();
		}

		if (rulesType == TblMappingRules.class) {
			tblVerbframeRules = (Set<T>) tblSlots.getTblMappingRules();
		}

		return tblVerbframeRules;
	}
}
