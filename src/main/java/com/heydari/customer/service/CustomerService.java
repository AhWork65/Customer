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

import static java.util.Objects.requireNonNull;

@Service
public class CustomerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

//==============================================================================
public Customer findCustomerByNationalcode(String nationalcode) throws CustomerInternalException {
    LOGGER.debug("findCustomerByNationalcode : Nationalcode INPUT PARAMET IS {} ",nationalcode);

    if (nationalcode == null|| nationalcode.isEmpty() ){
        LOGGER.debug("Bad Parametr...");
        throw new CustomerInternalException("findCustomerByNationalcode : Bad Parametr...");
    }

    Customer customer;
    customer = customerRepository.findCustomerByNationalcode(nationalcode);

    LOGGER.debug("findCustomerByNationalcode : return value", (customer == null) ? " null ":customer.toString());
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

    if (! validateCustomer(customer)) {
        LOGGER.error("createCustomer : Customer Parameter Not valid ");
        throw new CustomerCreateException("createCustomer Wrong parameter posted");
    }

    Customer  findCustomer = findCustomerByNationalcode(customer.getNationalcode());

    if (!(findCustomer == null)) {
        LOGGER.error("createCustomer : Exist This National Code {}",findCustomer.getNationalcode());
        throw new CustomerCreateException("createCustomer Exist This National Code ");
    }
    return customerRepository.save(customer);
}
//==============================================================================
    public List<Customer> getAllCustomerByFilter(Customer customer) throws CustomerInternalException {

    LOGGER.debug("getAllCustomerByFilter : Filter id  {}",(customer == null) ? " null ":customer.toString());
    List<Customer> customerList = customerRepository.findAll();
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    customerList = customerList.stream().
                filter(t -> { return customer == null || customer.getId() == null  || t.getId().equals( customer.getId());}).
                filter(t -> { return customer == null || customer.getCode() == null  || t.getCode().equals( customer.getCode());}).
                filter(t -> { return customer == null || customer.getName() == null  || t.getName().equals( customer.getName());}).
                filter(t -> { return customer == null || customer.getFamily() == null  || t.getFamily().equals( customer.getFamily());}).
                filter(t -> { return customer == null || customer.getNationalcode() == null  || t.getNationalcode().equals( customer.getNationalcode());}).
                filter(t -> { return customer == null || customer.getBirthDate() == null  || dateformat.format(t.getBirthDate()).equals(dateformat.format(customer.getBirthDate()));}).
                filter(t -> { return customer == null || customer.getStatus() == null  || t.getStatus().equals( customer.getStatus());}).
                filter(t -> { return customer == null || customer.getType() == null  || t.getType().equals( customer.getType());}).
                filter(t -> { return customer == null || customer.getMobile() == null  || t.getMobile().equals( customer.getMobile());}).
                filter(t -> { return customer == null || customer.getCreationDate() == null  || dateformat.format(t.getCreationDate()).equals(dateformat.format(customer.getCreationDate()));}).
                collect(Collectors.toList());
        return customerList;

    }
//==============================================================================
@Transactional(rollbackFor = {Exception.class})
    public Customer changeCustomerStatus(Customer customer) throws CustomerInternalException {

        if (!validateCustomer(customer)){
            LOGGER.error("changeCustomerStatus : Wrong parameter posted  parameter : {}",(customer == null) ? " null ":customer.toString());
            throw new CustomerInternalException("Wrong parameter posted");
        }

        if (customer.getId() == null) {
            LOGGER.error("changeCustomerStatus : Id Is Not Valid ID :{}",customer.getId());
            throw new CustomerInternalException("Id Is Not Valid...");
        }


        Customer changeCustomer;
        try {
            changeCustomer = getCustomerById(customer.getId());
        } catch (CustomerInternalException e) {
            LOGGER.error("changeCustomerStatus : Customer Not Exist");
            throw new CustomerBadRequestException(e.getMessage(), e);
        }


        if (customer.getStatus().equals(changeCustomer.getStatus())){
            LOGGER.debug("Customer Status not Change");
            return null;
        }

        changeCustomer.setStatus(customer.getStatus());
        LOGGER.debug("changeCustomerStatus : Customer Status Change Customer :" ,changeCustomer.toString());

        return customerRepository.save(changeCustomer);

    }
//==============================================================================
   public Customer getCustomerById(Long id) throws CustomerInternalException {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        Customer customer = customerOptional.orElse(null);

       customerOptional.orElseThrow(()->  {  LOGGER.error("getCustomerById :Customer Not Exist");
        return   new CustomerInternalException("Customer Not Found");});
       return customerOptional.orElse(null);

    }

//===============================================================================
public List<Deposit> getCustomerDeposits (Long id) throws CustomerInternalException {
    Customer customer = getCustomerById(id);
    LOGGER.info("getCustomerDeposits : Send Customer is : ", (customer == null) ? " null ":customer.toString() );
    List<Deposit> depositList = webClientBuilder.build().
            post()
            .uri("http://127.0.0.1:8091/DepositService/getBalansByCustomer")
            .body(Mono.just(customer), Customer.class)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Deposit>>() {})
            .block();

    LOGGER.info("getCustomerDeposits : Get list count  is : ", depositList.size());
    return depositList;
}
//===============================================================================
public List<Deposit> getCustomerDeposits (Customer customer) throws CustomerInternalException {
    LOGGER.info("getCustomerDeposits:  Send Customer is : ", (customer == null) ? " null ":customer.toString() );
    List<Deposit> depositList = webClientBuilder
            .build()
            .post()
            .uri("http://127.0.0.1:8091/DepositService/getBalansByCustomer")
            .body(Mono.just(customer), Customer.class)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<Deposit>>() {})
            .block();
    LOGGER.info("getCustomerDeposits : Get list count  is : ", (customer == null) ? " null ":customer.toString());
    return depositList;
}
//===============================================================================
    public Customer removeCustomer(Long id) throws CustomerInternalException {
        LOGGER.debug("removeCustomer : Filter id  {}",(id == null) ? " null ":id.toString());

        if ((id == null)) {
            LOGGER.error("removeCustomer : Input Paramet Is Null");
            throw new CustomerInternalException("removeCustomer : Input Paramet Is Null");
        }
        Customer customer = getCustomerById(id);

    List<Deposit> depositList = null;
        try {
             depositList = getCustomerDeposits (customer);
        } catch (WebClientException e) {
            e.printStackTrace();
        }

        if (!(depositList == null)) {
            LOGGER.error("removeCustomer :this Customer Have Depoit Can Not remove .customer :", (customer == null) ? " null ":customer.toString());
            throw new CustomerInternalException("Customer Have Deposit Can Not Remove This Customer ...");
        }

        if (!depositList.isEmpty()) {
            LOGGER.error("removeCustomer :this Customer Have Depoit Can Not remove .customer :", (customer == null) ? " null ":customer.toString());
            throw new CustomerInternalException("Customer Have Deposit Can Not Remove This Customer ...");
        }


        customerRepository.delete(customer);
        return customer;
    }
//===============================================================================
public List<Customer> getCustomersByDeposit(Deposit deposit){
    List<Customer> customerList = customerRepository.findAllByDeposit(deposit);
    LOGGER.info("getCustomersByDeposit :  get {}  customer ", customerList.size());
   return customerList;
}
}
