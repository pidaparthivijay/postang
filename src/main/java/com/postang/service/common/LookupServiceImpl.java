/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.constants.Constants;
import com.postang.model.Lookup;
import com.postang.repo.LookupRepository;

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


	@Override
	public Lookup findLookupByLookupId(long lookupId) {
		return lookupRepository.findByLookupId(lookupId);
	}

	@Override
	public Iterable<Lookup> getLookupList() {
		return lookupRepository.findAll();
	}

	@Override
	public Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupRepository.findByLookupDefName(lookupDefinitionName);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupRepository.save(lookup);
	}

	@Override
	public Iterable<Lookup> saveLookups(List<Lookup> lookupList) {
		return lookupRepository.saveAll(lookupList);
	}

}
