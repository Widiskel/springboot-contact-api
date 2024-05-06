package com.widiskel.rest.controller;

import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.ApiRes;
import com.widiskel.rest.model.contact.ContactCreateRequest;
import com.widiskel.rest.model.contact.ContactResponse;
import com.widiskel.rest.model.contact.ContactUpdateRequest;
import com.widiskel.rest.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;


    @GetMapping(
            path = "api/contact",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<List<ContactResponse>> getContacts(User user) {
        List<ContactResponse> res = contactService.getContacts(user);

        return ApiRes.<List<ContactResponse>>builder().rc("00").msg("Success").data(res).build();
    }
    @PostMapping(
            path = "api/contact",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<ContactResponse> createContact(User user, @RequestBody ContactCreateRequest request) {
        ContactResponse res = contactService.createContact(user,request);

        return ApiRes.<ContactResponse>builder().rc("00").msg("Success").data(res).build();
    }

    @GetMapping(
            path = "api/contact/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<ContactResponse> getContactById(User user, @PathVariable int id) {
        ContactResponse res = contactService.getContactById(user,id);

        return ApiRes.<ContactResponse>builder().rc("00").msg("Success").data(res).build();
    }

    @PostMapping(
            path = "api/contact/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ApiRes<ContactResponse> updateContact(User user, @PathVariable int id, @RequestBody ContactUpdateRequest request) {
        ContactResponse res = contactService.updateContact(user,id,request);

        return ApiRes.<ContactResponse>builder().rc("00").msg("Success").data(res).build();
    }


}
