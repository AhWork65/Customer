package com.heydari.customer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerChangeStatus {
    private Long id;
    private CustomerSatus Status;
}
