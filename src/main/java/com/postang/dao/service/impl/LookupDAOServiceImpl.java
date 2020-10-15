/**
 * 
 */
package com.postang.dao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.postang.constants.Constants;
import com.postang.dao.service.LookupDAOService;
import com.postang.domain.Lookup;
import com.postang.repo.LookupRepository;

/**
 * @author Subrahmanya Vijay
 *
 */
@Repository
public class LookupDAOServiceImpl implements LookupDAOService, Constants {

	@Autowired
	LookupRepository lookupRepository;

	@Override
	public List<Lookup> getLookupList() {
		return lookupRepository.findAll();
	}

	@Override
	public List<Lookup> getLookupListByDefinition(String lookupDefinitionName) {
		return lookupRepository.findByLookupDefName(lookupDefinitionName);
	}

	@Override
	public List<Lookup> saveLookups(List<Lookup> lookupList) {
		return lookupRepository.saveAll(lookupList);
	}

	@Override
	public Lookup findLookupByLookupId(long lookupId) {
		return lookupRepository.findByLookupId(lookupId);
	}

	@Override
	public Lookup saveLookup(Lookup lookup) {
		return lookupRepository.save(lookup);
	}
}
