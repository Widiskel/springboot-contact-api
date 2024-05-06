package com.widiskel.rest.model.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ContactResponse {

    private BigInteger id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String userName;
}
