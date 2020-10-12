package com.postang.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.OneTimePassword;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface OneTimePassRepo extends CrudRepository<OneTimePassword, Long> {
	public List<OneTimePassword> findByUserName(String userName);

}
