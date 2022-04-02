package com.heydari.customer.service;

import com.heydari.customer.controller.CustomerController;
import com.heydari.customer.exception.CustomerBadRequestException;
import com.heydari.customer.exception.CustomerCreateException;
import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.Customer;
import com.heydari.customer.model.Deposit;
import com.heydari.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

//==============================================================================
public Customer findCustomerByNationalcode(String nationalcode) throws CustomerInternalException {
    LOGGER.debug("Nationalcode INPUT PARAMET IS {} ",nationalcode);
    if (nationalcode == null|| nationalcode.isEmpty() ){
        LOGGER.debug("Bad Parametr...");
        throw new CustomerInternalException("Bad Parametr...");
    }

    Customer customer;
    customer = customerRepository.findCustomerByNationalcode(nationalcode);

    if (!validateCustomer(customer)){
        LOGGER.debug("Customer Not found");
        throw new CustomerInternalException("Customer Not found");
    }

    LOGGER.debug("return value",customer.toString());
    return customer;
}

//==============================================================================
public boolean validateCustomer(Customer customer) {
    if (customer == null) {
        return  false;
    }
    if (customer.getCode() == null) {
        return  false;
    }
    if (customer.getNationalcode() == null) {
        return  false;
    }

    return true;
}
//==============================================================================
@Transactional(rollbackFor = {Exception.class})
public Customer createCustomer (Customer customer) throws CustomerCreateException, CustomerInternalException {

    if (! validateCustomer( customer)) {
        LOGGER.error("createCustomer Customer Parameter  {}  Not valid  ",customer.toString());
        throw new CustomerCreateException("Wrong parameter posted");
    }

     Customer findCustomer = findCustomerByNationalcode(customer.getNationalcode());

    if (!(findCustomer == null)) {
        LOGGER.error("Exist This National Code {}",findCustomer.getNationalcode());
        throw new CustomerCreateException("Exist This National Code ");
    }
    return customerRepository.save(customer);
}
//==============================================================================
    public List<Customer> getAllCustomerByFilter(Customer customer) throws CustomerInternalException {

    List<Customer> customerList = customerRepository.findAll();
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    LOGGER.debug("getAllCustomerByFilter Filter id  {}",customer.toString());
        return  customerList.stream().
                filter(t -> { return customer.getId() == null  || t.getId().equals( customer.getId());}).
                filter(t -> { return customer.getCode() == null  || t.getCode().equals( customer.getCode());}).
                filter(t -> { return customer.getName() == null  || t.getName().equals( customer.getName());}).
                filter(t -> { return customer.getFamily() == null  || t.getFamily().equals( customer.getFamily());}).
                filter(t -> { return customer.getNationalcode() == null  || t.getNationalcode().equals( customer.getNationalcode());}).
                filter(t -> { return customer.getBirthDate() == null  || dateformat.format(t.getBirthDate()).equals(dateformat.format(customer.getBirthDate()));}).
                filter(t -> { return customer.getStatus() == null  || t.getStatus().equals( customer.getStatus());}).
                filter(t -> { return customer.getType() == null  || t.getType().equals( customer.getType());}).
                filter(t -> { return customer.getMobile() == null  || t.getMobile().equals( customer.getMobile());}).
                filter(t -> { return customer.getCreationDate() == null  || dateformat.format(t.getCreationDate()).equals(dateformat.format(customer.getCreationDate()));}).
                collect(Collectors.toList());

    }
//==============================================================================
@Transactional(rollbackFor = {Exception.class})
    public void changeCustomerStatus(Customer customer) throws CustomerInternalException {

        if (!validateCustomer(customer)){
            LOGGER.error("Wrong parameter posted  parameter : {}",customer.toString());
            throw new CustomerInternalException("Wrong parameter posted");
        }

        if (customer.getId() == null) {
            LOGGER.error("Id Is Not Valid ID :{}",customer.getId());
            throw new CustomerInternalException("Id Is Not Valid...");
        }


        Customer changeCustomer;
        try {
            changeCustomer = getCustomerById(customer.getId());
        } catch (CustomerInternalException e) {
            LOGGER.error("Customer Not Exist");
            throw new CustomerBadRequestException(e.getMessage(), e);
        }


        if (customer.getStatus().equals(changeCustomer.getStatus())){
            LOGGER.debug("Customer Status not Change");
            return;
        }

        changeCustomer.setStatus(customer.getStatus());
        customerRepository.save(changeCustomer);
        LOGGER.debug("Customer Status Change Customer :" ,changeCustomer.toString());

    }
//==============================================================================
   public Customer getCustomerById(Long id) throws CustomerInternalException {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        Customer customer = customerOptional.orElse(null);

       customerOptional.orElseThrow(()->  {  LOGGER.error("Customer Not Exist");
        return   new CustomerInternalException("Customer Not Found");});
       return customerOptional.orElse(null);

    }

//===============================================================================
public List<Deposit> getCustomerDeposits (Long id) throws CustomerInternalException {
    Customer customer = getCustomerById(id);
    LOGGER.info("getCustomerDeposits Send Customer is : ", customer.toString() );
    List<Deposit> depositList = webClientBuilder.build().
            post()
            .uri("http://127.0.0.1:8091/DepositService/getBalansByCustomer")
            .body(Mono.just(customer), Customer.class)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Deposit>>() {})
            .block();

    LOGGER.info("getCustomerDeposits Get list count  is : ", depositList.size());
    return depositList;
}
//===============================================================================
public List<Deposit> getCustomerDeposits (Customer customer) throws CustomerInternalException {
    LOGGER.info("getCustomerDeposits Send Customer is : ", customer.toString() );
    List<Deposit> depositList = webClientBuilder.build().
            post()
            .uri("http://127.0.0.1:8091/DepositService/getBalansByCustomer")
            .body(Mono.just(customer), Customer.class)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Deposit>>() {})
            .block();
    LOGGER.info("getCustomerDeposits Get list count  is : ", depositList.size());
    return depositList;
}
//===============================================================================
    public void removeCustomer(Long id) throws CustomerInternalException {

    Customer customer = getCustomerById(id);

    List<Deposit> depositList = null;
        try {
             depositList = getCustomerDeposits (customer);
        } catch (WebClientException e) {
            e.printStackTrace();
        }

        if (!(depositList == null)) {
            LOGGER.error("this Customer Have Depoit Can Not remove .customer :", customer.toString());
            throw new CustomerInternalException("Customer Have Deposit Can Not Remove This Customer ...");
        }

        if (!depositList.isEmpty()) {
            LOGGER.error("this Customer Have Depoit Can Not remove .customer :", customer.toString());
            throw new CustomerInternalException("Customer Have Deposit Can Not Remove This Customer ...");
        }


        customerRepository.delete(customer);

    }
//===============================================================================
public List<Customer> getCustomersByDeposit(Deposit deposit){
    List<Customer> customerList = customerRepository.findAllByDeposit(deposit);
    LOGGER.info("getCustomersByDeposit  get {}  customer ", customerList.size());
   return customerList;
}
}
