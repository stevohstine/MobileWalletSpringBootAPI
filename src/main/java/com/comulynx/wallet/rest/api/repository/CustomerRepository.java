package com.comulynx.wallet.rest.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comulynx.wallet.rest.api.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCustomerId(String customerId);

	// TODO : Implement the query and function below to delete a customer using Customer Id
//	 @Query("?")
//	 int deleteCustomerByCustomerId(String customer_id);
//	@Modifying
	@Query("DELETE FROM Customer c WHERE c.customerId = :customer_id")
	int deleteCustomerByCustomerId(@Param("customer_id") String customer_id);

	// TODO : Implement the query and function below to update customer firstName using Customer Id
	// @Query("?")
	// int updateCustomerByCustomerId(String firstName, String customer_id);
//	@Modifying
	@Query("UPDATE Customer c SET c.firstName = :firstName WHERE c.customerId = :customer_id")
	int updateCustomerByCustomerId(@Param("firstName") String firstName, @Param("customer_id") String customer_id);
	
	// TODO : Implement the query and function below and to return all customers whose Email contains  'gmail'
	// @Query("?")
	// List<Customer> findAllCustomersWhoseEmailContainsGmail();
	@Query("SELECT c FROM Customer c WHERE c.email LIKE %gmail%")
	List<Customer> findAllCustomersWhoseEmailContainsGmail();
//	@Query("SELECT c FROM Customer c WHERE c.email LIKE %:emailDomain%")
//	List<Customer> findAllCustomersWhoseEmailContainsGmail(@Param("emailDomain") String emailDomain);
}
