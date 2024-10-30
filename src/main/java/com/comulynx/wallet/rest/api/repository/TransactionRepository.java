package com.comulynx.wallet.rest.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comulynx.wallet.rest.api.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//	Optional<List<Transaction>> findTransactionsByCustomerId(String customerId);
	@Query("SELECT t FROM Transaction t WHERE t.customerId = :customerId ORDER BY t.id DESC")
	Optional<List<Transaction>> findTransactionsByCustomerId(@Param("customerId") String customerId, Pageable pageable);

	Optional<List<Transaction>> findTransactionsByTransactionId(String transactionId);

	Optional<List<Transaction>> findTransactionsByCustomerIdOrTransactionId(String transactionId, String customerId);

	// TODO : Change below Query to return the last 5 transactions
	// TODO : Change below Query to use Named Parameters instead of indexed
	// parameters
	// TODO : Change below function to return Optional<List<Transaction>>
//	@Query("SELECT t FROM Transaction t WHERE t.customerId =?1 AND  t.accountNo =?2")
//	List<Transaction> getMiniStatementUsingCustomerIdAndAccountNo(String customer_id, String account_no);
	@Query("SELECT t FROM Transaction t WHERE t.customerId = :customer_id AND t.accountNo = :account_no ORDER BY t.id DESC")
	Optional<List<Transaction>> getMiniStatementUsingCustomerIdAndAccountNo(
			@Param("customer_id") String customer_id,
			@Param("account_no") String account_no,
			Pageable pageable
	);

}
