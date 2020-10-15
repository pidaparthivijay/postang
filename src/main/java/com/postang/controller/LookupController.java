package com.postang.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.Lookup;
import com.postang.model.RequestDTO;
import com.postang.service.LookupService;
import com.postang.util.Util;

/**
 * @author Subrahmanya Vijay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class LookupController implements RequestMappings, Constants {

	@Autowired
	LookupService lookupService;

	Util util = new Util();

	/********************
	 * Lookup Operations**
	 *********************/

	@PostMapping(value = LOOKUP_EXCEL_UPLOAD)
	public RequestDTO uploadLookupExcel(@RequestParam("lookupExcel") MultipartFile multipartFile) {
		RequestDTO requestDTO = new RequestDTO();
		try {
			String status = lookupService.uploadLookupExcel(multipartFile);
			requestDTO.setActionStatus(status);
			return viewLookupList(status);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_CREATE)
	public RequestDTO createLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup = requestDTO.getLookup();
		try {
			lookup.setDeleted(NO);
			lookup.setCreatedDate(new Date());
			Lookup savedLookup = lookupService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(savedLookup.getLookupId() > 0 ? SUCCESS : FAILURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_UPDATE)
	public RequestDTO updateLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup = requestDTO.getLookup();
		try {
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = lookupService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(UPDATE_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_DELETE_TOGGLE)
	public RequestDTO toggleDelete(@RequestBody RequestDTO requestDTO) {
		try {
			String toggleStatus = lookupService.toggleDelete(requestDTO.getLookup());
			return viewLookupList(toggleStatus);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_DEF)
	public RequestDTO getLookupDefs() {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setLookupDefsList(lookupService.getLookupDefinitions());
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_VIEW_BY_DEF)
	public RequestDTO getLookupListByDefinition(@RequestBody RequestDTO requestDTO) {
		String lookupDefinitionName = requestDTO.getLookupDefinitionName();
		try {
			requestDTO.setLookupList(lookupService.getLookupListByDefinition(lookupDefinitionName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_ALL)
	public RequestDTO viewLookupList(String status) {
		RequestDTO requestDTO = new RequestDTO();
		try {
			requestDTO.setLookupList(lookupService.getLookupList());
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			e.printStackTrace();
		}
		return requestDTO;
	}

}
