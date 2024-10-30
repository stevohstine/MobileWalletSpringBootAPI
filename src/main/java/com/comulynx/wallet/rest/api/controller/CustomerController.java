package com.comulynx.wallet.rest.api.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comulynx.wallet.rest.api.AppUtilities;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.model.Customer;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.comulynx.wallet.rest.api.repository.CustomerRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private Gson gson = new Gson();

	@Autowired
	private final CustomerRepository customerRepository;
	@Autowired
	private final AccountRepository accountRepository;

    public CustomerController(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/")
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	/**
	 * Fix Customer Login functionality
	 *
	 * Login
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<?> customerLogin(@RequestBody String request) {
		try {
			JsonObject response = new JsonObject();

			final JsonObject req = gson.fromJson(request, JsonObject.class);
			String customerId = req.get("customerId").getAsString();
			String customerPIN = req.get("pin").getAsString();

			// TODO : Add Customer login logic here. Login using customerId and
			// PIN
			// NB: We are using plain text password for testing Customer login
			// If customerId doesn't exists throw an error "Customer does not exist"
			// If password do not match throw an error "Invalid credentials"

			//TODO : Return a JSON object with the following after successful login
			//Customer Name, Customer ID, email and Customer Account

			Customer customer = customerRepository.findByCustomerId(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Customer does not exist"));

			if (!customer.getPin().equals(hashPin(customerPIN))) {
				return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
			}

			response.addProperty("customerName", customer.getFirstName() + " " + customer.getLastName());
			response.addProperty("customerId", customer.getCustomerId());
			response.addProperty("email", customer.getEmail());
			response.addProperty("accountNo", accountRepository.findAccountByCustomerId(customerId).get().getAccountNo());

			return ResponseEntity.status(200).body(HttpStatus.OK);

		} catch (Exception ex) {
			logger.info("Exception {}", AppUtilities.getExceptionStacktrace(ex));
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *  Add required logic
	 *
	 *  Create Customer
	 *
	 * @param customer
	 * @return
	 */
	@PostMapping("/")
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
		try {
			String customerPIN = customer.getPin();
			String email = customer.getEmail();
			String customerId = customer.getCustomerId();

			// TODO : Add logic to Hash Customer PIN here
			//  : Add logic to check if Customer with provided email, or
			// customerId exists. If exists, throw a Customer with [?] exists
			// Exception.

			if (customerRepository.findByCustomerId(customerId).isPresent()) {
				return new ResponseEntity<>("Customer with this customerId already exists", HttpStatus.CONFLICT);
			}

			if (customerRepository.findByCustomerId(customer.getCustomerId()).isPresent()) {
				return new ResponseEntity<>("Customer with this ID already exists", HttpStatus.CONFLICT);
			}

			// Hash the PIN before saving
			customer.setPin(hashPin(customerPIN));

			String accountNo = generateAccountNo(customer.getCustomerId());
			Account account = new Account();
			account.setCustomerId(customer.getCustomerId());
			account.setAccountNo(accountNo);
			account.setBalance(0.0);
			accountRepository.save(account);

			return ResponseEntity.ok().body(customerRepository.save(customer));
		} catch (Exception ex) {
			logger.info("Exception {}", AppUtilities.getExceptionStacktrace(ex));

			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *  Add required functionality
	 *
	 * generate a random but unique Account No (NB: Account No should be unique
	 * in your accounts table)
	 *
	 */

	private String hashPin(String pin) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(pin.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error hashing PIN", e);
		}
    }

	private String generateAccountNo(String customerId) {
		// Implement unique account number generation logic here
		return "ACC-" + System.currentTimeMillis(); // Placeholder logic
	}
}
