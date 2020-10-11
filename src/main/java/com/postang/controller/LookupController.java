package com.postang.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import com.postang.model.Lookup;
import com.postang.model.RequestDTO;
import com.postang.service.common.LookupService;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
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
		log.info("uploadLookupExcel starts..." + multipartFile);
		try {
			List<Lookup> lookupList = util.generateLookupListFromExcelFile(multipartFile);
			Iterable<Lookup> lookupIterables = lookupService.saveLookups(lookupList);
			requestDTO.setLookupList(
					StreamSupport.stream(lookupIterables.spliterator(), false).collect(Collectors.toList()));
			requestDTO.setActionStatus(LOOKUP_EXCEL_SXS);
			return viewLookupList(LOOKUP_EXCEL_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in uploadLookupExcel" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_CREATE)
	public RequestDTO createLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup = requestDTO.getLookup();
		log.info("createLookup starts..." + lookup);
		try {
			lookup.setDeleted(NO);
			lookup.setCreatedDate(new Date());
			Lookup savedLookup = lookupService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(savedLookup.getLookupId() > 0 ? SUCCESS : FAILURE);
		} catch (Exception e) {
			log.error("Exception in createLookup" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_UPDATE)
	public RequestDTO updateLookup(@RequestBody RequestDTO requestDTO) {
		Lookup lookup = requestDTO.getLookup();
		log.info("updateLookup starts..." + lookup);
		try {
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = lookupService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			requestDTO.setActionStatus(UPDATE_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in updateLookup" + e);
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_DELETE_TOGGLE)
	public RequestDTO toggleDelete(@RequestBody RequestDTO requestDTO) {
		long lookupId = requestDTO.getLookup().getLookupId();
		log.info("toggleDelete starts..." + lookupId);
		try {
			Lookup lookup = lookupService.findLookupByLookupId(lookupId);
			lookup.setDeleted(YES.equals(lookup.getDeleted()) ? NO : YES);
			lookup.setUpdateDate(new Date());
			Lookup savedLookup = lookupService.saveLookup(lookup);
			requestDTO.setLookup(savedLookup);
			return viewLookupList(YES.equals(savedLookup.getDeleted()) ? DEL_SXS : UN_DEL_SXS);
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in toggleDelete" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_DEF)
	public RequestDTO getLookupDefs() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getLookupDefs starts...");
		try {
			Iterable<Lookup> lookupIterables = lookupService.getLookupList();
			List<String> lookupDefsList = StreamSupport.stream(lookupIterables.spliterator(), false)
					.collect(Collectors.toList()).stream().distinct().map(Lookup::getLookupDefName)
					.collect(Collectors.toList());
			requestDTO.setLookupDefsList(lookupDefsList.stream().distinct().collect(Collectors.toList()));
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getLookupDefs" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = LOOKUP_VIEW_BY_DEF)
	public RequestDTO getLookupListByDefinition(@RequestBody RequestDTO requestDTO) {
		String lookupDefinitionName = requestDTO.getLookupDefinitionName();
		log.info("getLookupListByDefinition starts..." + lookupDefinitionName);
		try {
			Iterable<Lookup> lookupList = lookupService.getLookupListByDefinition(lookupDefinitionName);
			requestDTO
					.setLookupList(StreamSupport.stream(lookupList.spliterator(), false).collect(Collectors.toList()));
		} catch (Exception e) {
			log.error("Exception in getLookupListByDefinition..." + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = LOOKUP_VIEW_ALL)
	public RequestDTO viewLookupList(String status) {
		RequestDTO requestDTO = new RequestDTO();
		log.info("viewLookupList starts...");
		try {
			Iterable<Lookup> lookupIterables = lookupService.getLookupList();
			requestDTO.setLookupList(
					StreamSupport.stream(lookupIterables.spliterator(), false).collect(Collectors.toList()));
			if (!StringUtils.isEmpty(status)) {
				requestDTO.setActionStatus(status);
			}
		} catch (Exception e) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewLookupList" + e);
			e.printStackTrace();
		}
		return requestDTO;
	}

}
