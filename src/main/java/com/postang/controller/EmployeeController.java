package com.postang.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postang.constants.Constants;
import com.postang.constants.RequestMappings;
import com.postang.domain.Employee;
import com.postang.model.RequestDTO;
import com.postang.service.EmployeeService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Subrahmanya Vijay
 *
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping(RequestMappings.BRW)
public class EmployeeController implements RequestMappings, Constants {

	@Autowired
	EmployeeService employeeService;


	/***********************
	 * Employee Operations**
	 ***********************/

	@PostMapping(value = EMP_CREATE)
	public RequestDTO createEmployee(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("createEmployee starts...");
		try {
			Employee emp = employeeService.createEmployee(employee);
			requestDTO.setEmployee(emp);
			requestDTO.setActionStatus((emp != null && emp.getEmpId() > 0) ? EMP_CRT_SXS : EMP_CRT_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in createEmployee : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@GetMapping(value = EMP_VIEW_ALL)
	public RequestDTO getAllEmployees() {
		RequestDTO requestDTO = new RequestDTO();
		log.info("getAllEmployees starts...");
		try {
			List<Employee> empList = employeeService.getAllEmployees();
			empList.sort(Comparator.comparing(Employee::getEmpId));
			requestDTO.setEmployeesList(empList);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in getAllEmployees : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = EMPLOYEE_VIEW_DETAILS)
	public RequestDTO viewEmployeeDetails(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("viewEmployeeDetails starts...");
		try {
			Employee emp = employeeService.getEmployeeDetails(employee.getUserName());
			requestDTO.setEmployee(emp);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in viewEmployeeDetails : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}

	@PostMapping(value = EMPLOYEE_UPDATE)
	public RequestDTO udpateEmployee(@RequestBody RequestDTO requestDTO) {
		Employee employee = requestDTO.getEmployee();
		log.info("udpateEmployee starts...");
		try {
			Employee emp = employeeService.updateEmployee(employee);
			requestDTO.setEmployee(emp);
			requestDTO.setActionStatus((emp != null && emp.getEmpId() > 0) ? EMP_UPDATE_SXS : EMP_UPDATE_FAIL);
		} catch (Exception ex) {
			requestDTO.setActionStatus(EXCEPTION_OCCURED);
			log.error("Exception in udpateEmployee : " + ex.getMessage());
			ex.printStackTrace();
		}
		return requestDTO;
	}
}
