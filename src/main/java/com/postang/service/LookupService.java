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

	List<Lookup> getLookupList();

	List<Lookup> getLookupListByDefinition(String lookupDefinitionName);

	Lookup saveLookup(Lookup lookup);

	String uploadLookupExcel(MultipartFile multipartFile);

	List<String> getLookupDefinitions();

	String toggleDelete(Lookup lookup);

}
