/**
 * 
 */
package com.postang.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postang.constants.Constants;
import com.postang.dao.service.LookupDAOService;
import com.postang.domain.Lookup;
import com.postang.service.LookupService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Service
public class LookupServiceImpl implements LookupService, Constants {

	@Autowired
	LookupDAOService lookupDaoService;
	Util util = new Util();

	@Override
	public List<Lookup> getLookupList() {
		return lookupDaoService.getLookupList();
	}

	@Override
	public List<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupDaoService.getLookupListByDefinition(lookupDefinitionName);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupDaoService.saveLookup(lookup);
	}

	@Override
	public String uploadLookupExcel(MultipartFile multipartFile) {

		int existingSize = lookupDaoService.getLookupList().size();
		List<Lookup> lookupFromExcel = util.generateLookupListFromExcelFile(multipartFile);
		List<Lookup> lookupIterables = lookupDaoService.saveLookups(lookupFromExcel);
		int newSize = lookupDaoService.getLookupList().size();
		return (newSize == lookupIterables.size() + existingSize) ? LOOKUP_EXCEL_SXS : LOOKUP_EXCEL_FAIL;
	}

	@Override
	public List<String> getLookupDefinitions() {
		List<Lookup> lookupList = lookupDaoService.getLookupList();
		List<String> lookupDefsList = lookupList.stream().distinct().map(Lookup::getLookupDefName)
				.collect(Collectors.toList());
		return lookupDefsList.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public String toggleDelete(Lookup lookup) {
		Lookup existingLookup = lookupDaoService.findLookupByLookupId(lookup.getLookupId());
		lookup.setDeleted(YES.equals(existingLookup.getDeleted()) ? NO : YES);
		lookup.setUpdateDate(new Date());
		Lookup savedLookup = lookupDaoService.saveLookup(lookup);
		return YES.equals(savedLookup.getDeleted()) ? DEL_SXS : UN_DEL_SXS;
	}

}
