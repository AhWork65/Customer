package com.heydari.customer.service;

import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.Customer;
import com.heydari.customer.model.CustomerChangeStatus;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ChangeCustomerStatusTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;
//==============================================================================

    @Test
    void change_Customer_Status_Test_Happy_Scenario() throws CustomerInternalException {
        CustomerChangeStatus customer = new CustomerChangeStatus(1l, CustomerSatus.Inactive);
        when(customerRepository.findById(Matchers.any())).thenAnswer(t -> {
            Optional<Customer> customerOptional =Optional.of(new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Active, CustomerType.Legal,"0912",null));
            return  customerOptional;
        });
        when(customerRepository.save(Matchers.any())).thenAnswer(t -> { return (Customer)t.getArgument(0);});
        Customer retCustomer = customerService.changeCustomerStatus(customer);
        assertEquals(CustomerSatus.Inactive, retCustomer.getStatus() );
    }
//==============================================================================
@Test
void change_Customer_Status_Test_ByNull_Paramet() throws CustomerInternalException {
    assertThrows(CustomerInternalException.class, () -> customerService.changeCustomerStatus(null));
}
//==============================================================================
@Test
void change_Customer_Status_Test_ByNull_Customer_State() throws CustomerInternalException {
    assertThrows(CustomerInternalException.class, () -> customerService.changeCustomerStatus(new CustomerChangeStatus()));
}
}
