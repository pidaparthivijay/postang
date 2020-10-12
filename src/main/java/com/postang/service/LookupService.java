/**
 * 
 */
package com.postang.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.postang.domain.Lookup;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface LookupService {

	Lookup findLookupByLookupId(long lookupId);

	List<Lookup> getLookupList();

	List<Lookup> getLookupListByDefinition(String lookupDefinitionName);

	Lookup saveLookup(Lookup lookup);

	List<Lookup> saveLookups(List<Lookup> lookupList);

	String uploadLookupExcel(MultipartFile multipartFile);

}
