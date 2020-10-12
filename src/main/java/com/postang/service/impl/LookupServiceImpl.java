/**
 * 
 */
package com.postang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postang.constants.Constants;
import com.postang.domain.Lookup;
import com.postang.repo.LookupRepository;
import com.postang.service.LookupService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@Service
public class LookupServiceImpl implements LookupService, Constants {

	@Autowired
	LookupRepository lookupRepository;

	Util util = new Util();

	@Override
	public Lookup findLookupByLookupId(long lookupId) {
		return lookupRepository.findByLookupId(lookupId);
	}

	@Override
	public List<Lookup> getLookupList() {
		return lookupRepository.findAll();
	}

	@Override
	public List<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupRepository.findByLookupDefName(lookupDefinitionName);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupRepository.save(lookup);
	}

	@Override
	public List<Lookup> saveLookups(List<Lookup> lookupList) {
		return lookupRepository.saveAll(lookupList);
	}

	@Override
	public String uploadLookupExcel(MultipartFile multipartFile) {

		int existingSize = lookupRepository.findAll().size();
		List<Lookup> lookupFromExcel = util.generateLookupListFromExcelFile(multipartFile);
		List<Lookup> lookupIterables = this.saveLookups(lookupFromExcel);
		int newSize = lookupRepository.findAll().size();

		return (newSize == lookupIterables.size() + existingSize) ? LOOKUP_EXCEL_SXS : LOOKUP_EXCEL_FAIL;
	}

}
