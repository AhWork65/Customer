package com.heydari.customer.service;

import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.Customer;
import com.heydari.customer.model.CustomerSatus;
import com.heydari.customer.model.CustomerType;
import com.heydari.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class GetAllCustomerByFilterTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void GetAll_Customer_By_Filter_Test_Happy_Scenario() throws CustomerInternalException {
        Customer customer = new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Inactive, CustomerType.Legal,"0912",null);
        when(customerRepository.findAll()).thenAnswer(t -> {
            List<Customer> customerList = new ArrayList<>();
            customerList.add( new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Inactive, CustomerType.Legal,"0912",null));
            customerList.add( new Customer(2l,"2","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            customerList.add( new Customer(3l,"3","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            customerList.add( new Customer(4l,"4","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            return customerList;
        });
        List<Customer> customerList = customerService.getAllCustomerByFilter(customer);
        assertEquals(1, customerList.size());
    }
    //=====================================================================================================================================================================
    @Test
    void GetAll_Customer_By_Filter_Test() throws CustomerInternalException {
        when(customerRepository.findAll()).thenAnswer(t -> {
            List<Customer> customerList = new ArrayList<>();
            customerList.add( new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Inactive, CustomerType.Legal,"0912",null));
            customerList.add( new Customer(2l,"2","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            customerList.add( new Customer(3l,"3","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            customerList.add( new Customer(4l,"4","test","test_family","1234567890",null ,null, CustomerSatus.Inactive,CustomerType.Legal,"0912",null));
            return customerList;
        });
        List<Customer> customerList = customerService.getAllCustomerByFilter(null);
        assertEquals(4, customerList.size());
    }
}
