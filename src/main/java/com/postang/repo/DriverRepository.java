package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Driver;

public interface DriverRepository extends CrudRepository<Driver, Long> {

}
