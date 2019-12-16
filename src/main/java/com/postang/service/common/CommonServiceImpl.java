/**
 * 
 */
package com.postang.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.postang.model.Employee;
import com.postang.repo.EmployeeRepository;
import com.postang.util.Util;

import lombok.extern.log4j.Log4j2;

/**
 * @author Vijay
 *
 */
@Log4j2
@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	EmployeeRepository empRepo;

	Util util = new Util();

	@Override
	public Employee getEmployeeByUserName(String userName) {
		List<Employee> empList = null;
		log.info("getEmployeeByUserName starts with userName: " + userName);
		try {
			empList = empRepo.findByUserName(userName);
			if (empList != null && empList.size() > 0) {
				return empList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			log.info("Exception in getEmployeeByUserName :" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

}
