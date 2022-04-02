package com.heydari.customer.service;

import com.heydari.customer.exception.CustomerCreateException;
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
public class CreateCustomerTest {

    @Mock
    private CustomerRepository customerRepository;


    @InjectMocks
    private CustomerService customerService;

//==============================================================================
@Test
    void Create_Customer_By_Null_Paramet() {
        assertThrows(CustomerCreateException.class, () -> customerService.createCustomer(null));
    }
    //==============================================================================
 @Test
   void Create_Customer_Happy_Scenario() throws CustomerInternalException, CustomerCreateException {
    Customer customer = new Customer();
    customer.setCode("10");
    customer.setNationalcode("1234567890");

     when(customerRepository.findCustomerByNationalcode(Matchers.any())).thenReturn(null);

     when(customerRepository.save(Matchers.any())).thenAnswer(t -> {
        Customer retCustomer2= new Customer();
         retCustomer2.setCode("10");
         retCustomer2.setNationalcode("1234567890");
        return retCustomer2;
        });

        Customer retCustomer = customerService.createCustomer(customer);
        assertEquals(retCustomer.getNationalcode(), "1234567890");
    }


}
