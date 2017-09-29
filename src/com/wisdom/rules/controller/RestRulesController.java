package com.wisdom.rules.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wisdom.rules.dao.RulesDao;
import com.wisdom.rules.model.MappingRule;
import com.wisdom.rules.model.VerbFrameRule;

/**
 * Class RestBranchController
 */
@RestController
@RequestMapping("/rules")
@EnableAutoConfiguration
public class RestRulesController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/verbframeRules/{verbframeId}", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	public @ResponseBody List<VerbFrameRule> getVerbframeRulesById(@PathVariable("verbframeId") int verbframeId,
			@RequestParam(required = false, value = "page") int page) throws JsonParseException, IOException {
		List<VerbFrameRule> verbFrameRules = new ArrayList<>();
		VerbFrameRule verbFrameRule = rulesDao.getVerbFrameRuleById(verbframeId, page);

		verbFrameRules.add(verbFrameRule);

		return verbFrameRules;
	}

	@RequestMapping(value = "/verbframeRulesByCriteria", method = RequestMethod.GET, produces = {
			"application/json" }, consumes = MediaType.ALL_VALUE)
	public @ResponseBody List<VerbFrameRule> getVerbframeRulesByCriteria(@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "value") String value,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) throws JsonParseException, IOException {

		List<VerbFrameRule> verbFrameRules = rulesDao.getVerbFrameRules(key, value, page);

		return verbFrameRules;
	}

	@RequestMapping(value = "/mappingRulesByCriteria", method = RequestMethod.GET, produces = { "application/json" }, consumes = MediaType.ALL_VALUE)
	public @ResponseBody List<MappingRule> getMappingRulesByCriteria(@RequestParam(required = false, value = "key") String key,
			@RequestParam(required = false, value = "value") String value,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) throws JsonParseException, IOException {

		List<MappingRule> mappingRules = rulesDao.getMappingRules(key, value, page);

		return mappingRules;
	}

	@Autowired
	private RulesDao rulesDao;

}
