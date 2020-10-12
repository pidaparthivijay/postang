package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.domain.User;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface UserRepository extends CrudRepository<User, Long> {

	public User findByUserName(String userName);
	
	public User findByUserId(long userId);

	public User findByUserMail(String userMail);
}
