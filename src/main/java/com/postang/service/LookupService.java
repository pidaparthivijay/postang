/**
 * 
 */
package com.postang.service;

import java.util.List;

import com.postang.domain.Lookup;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface LookupService {

	Lookup findLookupByLookupId(long lookupId);

	Iterable<Lookup> getLookupList();

	Iterable<Lookup> getLookupListByDefinition(String lookupDefinitionName);

	Lookup saveLookup(Lookup lookup);

	Iterable<Lookup> saveLookups(List<Lookup> lookupList);

}
