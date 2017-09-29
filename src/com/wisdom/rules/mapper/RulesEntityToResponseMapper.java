package com.wisdom.rules.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.wisdom.rules.entities.TblCodes;
import com.wisdom.rules.entities.TblMappingRules;
import com.wisdom.rules.entities.TblModifiers;
import com.wisdom.rules.entities.TblRegionUseRules;
import com.wisdom.rules.entities.TblSlots;
import com.wisdom.rules.entities.TblVerbFrameRules;
import com.wisdom.rules.model.MappingRule;
import com.wisdom.rules.model.VerbFrameRule;

@Component
public class RulesEntityToResponseMapper {

	private static final int PAGE_SIZE = 500;

	public List<VerbFrameRule> transformVerbFrameRules(Set<TblVerbFrameRules> tblVerbframeRules, int page) {
		List<VerbFrameRule> verbFrameRulesResponse = new ArrayList<>();

		int firstRecordInex = (PAGE_SIZE * (page - 1));
		int lastRecordIndex = (PAGE_SIZE * (page - 1)) + PAGE_SIZE;
		lastRecordIndex = lastRecordIndex > tblVerbframeRules.size() ? tblVerbframeRules.size() : lastRecordIndex;

		List<TblVerbFrameRules> tblVerbframeRulesList = new ArrayList(PAGE_SIZE);
		tblVerbframeRulesList.addAll(tblVerbframeRules);

		for (int i = firstRecordInex; i < lastRecordIndex; i++) {

			VerbFrameRule verbFrameRule = new VerbFrameRule();
			TblVerbFrameRules tblVerbframeRule = tblVerbframeRulesList.get(i);
			verbFrameRule.setId(tblVerbframeRule.getId());
			verbFrameRule.setScope(tblVerbframeRule.getScope());
			verbFrameRule.setAnyRegionUse(tblVerbframeRule.getAnyRegionUse() == 1 ? true : false);

			Set<TblSlots> tblVerbframeSlots = tblVerbframeRule.getTblSlots();
			List<Object> slots = new ArrayList<Object>();
			for (TblSlots tblSlot : tblVerbframeSlots) {
				slots.add(tblSlot.getValue());
			}
			verbFrameRule.setSlots(slots);

			Set<TblCodes> tblVerbframeCodes = tblVerbframeRule.getTblCodes();
			List<Object> codes = new ArrayList<Object>();
			for (TblCodes tblcode : tblVerbframeCodes) {
				codes.add(tblcode.getValue());
			}
			verbFrameRule.setCodes(codes);

			Set<TblRegionUseRules> tblVerbframeRegionUseRules = tblVerbframeRule.getTblRegionUseRules();
			List<Object> RegionUseRules = new ArrayList<Object>();
			for (TblRegionUseRules tblRegionUse : tblVerbframeRegionUseRules) {
				RegionUseRules.add(tblRegionUse.getValue());
			}
			verbFrameRule.setRegionUseNcids(RegionUseRules);

			Set<TblModifiers> tblVerbframeModifiers = tblVerbframeRule.getTblModifiers();
			List<Object> modifiers = new ArrayList<Object>();
			for (TblModifiers tblModifier : tblVerbframeModifiers) {
				modifiers.add(tblModifier.getValue());
			}
			verbFrameRule.setModifiers(modifiers);

			verbFrameRulesResponse.add(verbFrameRule);
		}

		return verbFrameRulesResponse;
	}
	
	public List<MappingRule> transformMappingRules(Set<TblMappingRules> tblVerbframeRules, int page) {
		List<MappingRule> mappingRulesResponse = new ArrayList<>();

		int firstRecordInex = (PAGE_SIZE * (page - 1));
		int lastRecordIndex = (PAGE_SIZE * (page - 1)) + PAGE_SIZE;
		lastRecordIndex = lastRecordIndex > tblVerbframeRules.size() ? tblVerbframeRules.size() : lastRecordIndex;

		System.out.println("tblVerbframeRules.size() :: " + tblVerbframeRules.size());
		System.out.println("lastRecordIndex :: " + lastRecordIndex);
		List<TblMappingRules> tblVerbframeRulesList = new ArrayList(PAGE_SIZE);
		tblVerbframeRulesList.addAll(tblVerbframeRules);

		for (int i = firstRecordInex; i < lastRecordIndex; i++) {

			MappingRule mappingRule = new MappingRule();
			TblMappingRules tblVerbframeRule = tblVerbframeRulesList.get(i);
			mappingRule.setId(tblVerbframeRule.getId());
			mappingRule.setScope(tblVerbframeRule.getScope());
			mappingRule.setAnyRegionUse(tblVerbframeRule.getAnyRegionUse() == 1 ? true : false);

			Set<TblSlots> tblVerbframeSlots = tblVerbframeRule.getTblSlots();
			List<Object> slots = new ArrayList<Object>();
			for (TblSlots tblSlot : tblVerbframeSlots) {
				slots.add(tblSlot.getValue());
			}
			mappingRule.setSlots(slots);

			Set<TblCodes> tblVerbframeCodes = tblVerbframeRule.getTblCodes();
			List<Object> codes = new ArrayList<Object>();
			for (TblCodes tblcode : tblVerbframeCodes) {
				codes.add(tblcode.getValue());
			}
			mappingRule.setCodes(codes);

			Set<TblRegionUseRules> tblVerbframeRegionUseRules = tblVerbframeRule.getTblRegionUseRules();
			List<Object> RegionUseRules = new ArrayList<Object>();
			for (TblRegionUseRules tblRegionUse : tblVerbframeRegionUseRules) {
				RegionUseRules.add(tblRegionUse.getValue());
			}
			mappingRule.setRegionUseNcids(RegionUseRules);

			Set<TblModifiers> tblVerbframeModifiers = tblVerbframeRule.getTblModifiers();
			List<Object> modifiers = new ArrayList<Object>();
			for (TblModifiers tblModifier : tblVerbframeModifiers) {
				modifiers.add(tblModifier.getValue());
			}
			mappingRule.setModifiers(modifiers);
			mappingRule.setConceptId(tblVerbframeRule.getFkConceptId().getValue());

			mappingRulesResponse.add(mappingRule);
		}

		return mappingRulesResponse;
	}
	
}
