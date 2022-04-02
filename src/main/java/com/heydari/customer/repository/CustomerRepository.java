package com.heydari.customer.repository;

import com.heydari.customer.model.Customer;
import com.heydari.customer.model.CustomerSatus;
import com.heydari.customer.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByNationalcode(String nationalcode);


    List<Customer> findAllByDeposit(Deposit deposit);

    @Query("Select Cus.status from Customer Cus Where Cus.id = ?1")
    CustomerSatus getCustomerStatus(Long id);


}
