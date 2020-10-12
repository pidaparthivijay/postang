/**
 * 
 */
package com.postang.dao.service;

import java.util.List;

import com.postang.domain.Lookup;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface LookupDAOService {

	Lookup findLookupByLookupId(long lookupId);

	List<Lookup> getLookupList();

	List<Lookup> getLookupListByDefinition(String lookupDefinitionName);

	Lookup saveLookup(Lookup lookup);

	List<Lookup> saveLookups(List<Lookup> lookupList);

}
