package com.heydari.customer.controller;


import com.heydari.customer.exception.CustomerBadRequestException;
import com.heydari.customer.exception.CustomerNotFoundException;
import com.heydari.customer.model.Customer;
import com.heydari.customer.model.CustomerChangeStatus;
import com.heydari.customer.model.Deposit;
import com.heydari.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;
import java.util.List;


@RestController
@RequestMapping(value = "/customerservice")
public class CustomerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private  CustomerService customerService;

//===============================================================================
@PostMapping("/create")
    public Customer createCustomer(@RequestBody Customer customer){
    LOGGER.info("CreateCustomer INPUT PARAMET IS {} ",customer.toString());
    Customer returnCustomer ;
    try {
        returnCustomer = customerService.createCustomer(customer);
        LOGGER.info("CreateCustomer OUTPUT IS ",returnCustomer);
    } catch (Exception e) {
        LOGGER.error("CreateCustomer OUTPUT IS {} ",e.getMessage());
        throw new CustomerBadRequestException(e.getMessage());
    }
    return returnCustomer;
}
//===============================================================================
@PostMapping("/getallcustomer")
   public List<Customer> getAllCustomerByFilter(@RequestBody Customer customer)  {
    LOGGER.info("getAllCustomerByFilter INPUT PARAMET IS  {} ",customer.toString());
    try {
       return customerService.getAllCustomerByFilter(customer);
    } catch (Exception e) {
        LOGGER.error("getAllCustomerByFilter OUTPUT IS {} ",e.getMessage());
        throw new CustomerNotFoundException(e.getMessage());
    }
}
//===============================================================================
@PutMapping("/changestatus")
    public void changeCustomerStatus(@RequestBody CustomerChangeStatus customer){
    LOGGER.info("changeCustomerStatus INPUT PARAMET IS  {} ",customer.toString());
    try {
        customerService.changeCustomerStatus(customer);
    } catch (Exception e) {
        LOGGER.error("changeCustomerStatus OUTPUT IS {} ",e.getMessage());
        throw new CustomerBadRequestException(e.getMessage());
    }
}
//===============================================================================
@GetMapping("/getbyid/{id}")
public Customer getCustomerById(@PathVariable("id") Long id){
    LOGGER.info("getCustomerById INPUT PARAMET IS {}",id.toString());
    Customer returnCustomer ;
    try {
        returnCustomer = customerService.getCustomerById(id);
        LOGGER.info("getCustomerById OUTPUT IS {}",returnCustomer.toString());
    } catch (Exception e) {
        LOGGER.error("getCustomerById OUTPUT IS {}",e.getMessage());
        throw new CustomerBadRequestException(e.getMessage());
    }
    return returnCustomer;
}
//===============================================================================
@GetMapping("/getdepositsbycustomerid/{id}")
    public List<Deposit> getCustomerDeposits (@PathVariable("id") Long id) {
    LOGGER.info("getCustomerDeposits INPUT PARAMET IS  {} ",id.toString());
    try {
        return customerService.getCustomerDeposits(id);
    } catch (Exception e) {
        LOGGER.error("getCustomerDeposits OUTPUT IS {} ",e.getMessage());
        throw new CustomerBadRequestException(e.getMessage());
    }
}
//===============================================================================
@GetMapping("/removecustomerbyid/{id}")
public void removeCustomerById (@PathVariable("id")Long id) {
    LOGGER.info("removeCustomerById INPUT PARAMET IS  {} ",id.toString());
    try {
         customerService.removeCustomer(id);
    } catch (Exception e) {
        LOGGER.error("removeCustomerById OUTPUT IS {} ",e.getMessage());
        throw new CustomerBadRequestException(e.getMessage());
    }
}
//===============================================================================
    @PostMapping("/getcustomersbydeposit")
    public List<Customer> getcustomersbydeposit(@RequestBody Deposit deposit)  {
        LOGGER.info("getCustomersByDeposit INPUT PARAMET IS  {} ",deposit.toString());
        return customerService.getCustomersByDeposit(deposit);
    }
//===============================================================================
}

