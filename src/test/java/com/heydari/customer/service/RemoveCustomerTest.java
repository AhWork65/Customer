package com.heydari.customer.service;
import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.*;
import com.heydari.customer.repository.CustomerRepository;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import okhttp3.mockwebserver.MockResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class RemoveCustomerTest {




    @BeforeEach
    void setupMockWebServer() {

    }

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private CustomerService customerService;


//==============================================================================
    @Test
    void remove_Customer_Test_ByNull_Paramet() throws CustomerInternalException {
        assertThrows(CustomerInternalException.class, () -> customerService.removeCustomer(null));
    }
//==============================================================================
@Test
void remove_Customer_Test_Happy_Scenario() throws CustomerInternalException {
    Customer customer = new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Inactive, CustomerType.Legal,"0912",null);
    when(customerRepository.findById(Matchers.any())).thenAnswer(t -> {
        Optional<Customer> customerOptional =Optional.of(new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Active, CustomerType.Legal,"0912",null));
        return  customerOptional;
    });





    Customer retCustomer = customerService.removeCustomer(customer.getId());
    assertEquals(retCustomer, customer);
    }
}
