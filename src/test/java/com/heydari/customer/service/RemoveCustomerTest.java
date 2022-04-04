package com.heydari.customer.service;
import com.heydari.customer.exception.CustomerInternalException;
import com.heydari.customer.model.*;
import com.heydari.customer.repository.CustomerRepository;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class RemoveCustomerTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private WebClient webClientMock;

    @Mock
    private CustomerRepository customerRepository;

   @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;

    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;





//==============================================================================
    @Test
    void remove_Customer_Test_ByNull_Paramet() throws CustomerInternalException {
        assertThrows(CustomerInternalException.class, () -> customerService.removeCustomer(null));
    }
//==============================================================================
@SuppressWarnings("unchecked")
@Test
void remove_Customer_Test_Happy_Scenario() throws CustomerInternalException {
    Customer customer = new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Inactive, CustomerType.Legal,"0912",null);
    when(customerRepository.findById(any())).thenAnswer(t -> {
        Optional<Customer> customerOptional =Optional.of(new Customer(1l,"1","test","test_family","1234567890",null ,null, CustomerSatus.Active, CustomerType.Legal,"0912",null));
        return  customerOptional;
    });


    List<Deposit> depositList = new ArrayList<>();

    when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
    when(requestBodyUriSpecMock.uri(Matchers.anyString())).
            thenReturn(requestBodySpecMock);
   when(requestBodySpecMock.bodyValue(Matchers.any()))
            .thenReturn(requestHeadersSpecMock);
    when(requestHeadersSpecMock.retrieve())
            .thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(new ParameterizedTypeReference<List<Deposit>>() {}))
            .thenReturn(Mono.just(depositList));


    Customer retCustomer = customerService.removeCustomer(customer.getId());
    assertEquals(retCustomer.getId(), 1L);
    }
}
