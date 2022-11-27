package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.User;


// This interface is used to perform all the database operations.
// Wherever required, get its object using @Autowired annotation and call the methods based on requirement.
// There is a method save(), which can directly be called using object of this interface by passing Entity object, 
//		all of its values will be automatically inserted into table.

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);
}
