package com.heydari.customer.service;

import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.Customer;
import com.heydari.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class FindCustomerByNationalcodeTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;
//==============================================================================
    @Test
    void find_Customer_By_Nationalcode_Happy_Scenario() throws CustomerInternalException {
        when(customerRepository.findCustomerByNationalcode(Matchers.any())).thenAnswer(t -> {
            Customer customer = new Customer();
            customer.setId(1l);
            customer.setCode("1");
            customer.setNationalcode("1234567890");
            return customer;
        });
        Customer customer = customerService.findCustomerByNationalcode("1234567890");
        assertEquals( "1234567890", customer.getNationalcode());
    }
//==============================================================================
    @Test
    void find_Customer_By_Null_Nationalcode() {
        assertThrows(CustomerInternalException.class, () -> customerService.findCustomerByNationalcode(null));
    }
//==============================================================================
    @Test
    void find_Customer_By_Empty_Nationalcode() {
        assertThrows(CustomerInternalException.class, () -> customerService.findCustomerByNationalcode(""));
    }


}