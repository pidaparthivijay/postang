package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverRepository extends CrudRepository<Driver, Long> {

}
