package com.widiskel.rest.service;

import com.widiskel.rest.entity.Contact;
import com.widiskel.rest.entity.User;
import com.widiskel.rest.model.contact.ContactCreateRequest;
import com.widiskel.rest.model.contact.ContactResponse;
import com.widiskel.rest.model.contact.ContactUpdateRequest;
import com.widiskel.rest.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validator;


    public List<ContactResponse> getContacts(User user) {
        List<Contact> contactList = contactRepository.findContactsByUser(user);

        return contactList.stream()
                .map(contact -> ContactResponse.builder()
                        .id(contact.getId())
                        .firstName(contact.getFirstName())
                        .lastName(contact.getLastName())
                        .phone(contact.getPhone())
                        .email(contact.getEmail())
                        .userName(contact.getUser().getUsername())
                        .build())
                .toList();
    }


    @Transactional
    public ContactResponse createContact(User user, ContactCreateRequest request) {
        validator.validate(request);

        if (contactRepository.existsContactByEmail(request.getEmail())) {
            log.error("Bad Request Error {}", new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact already exists").getReason());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact already exists");
        }

        Contact contact = new Contact();
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setUser(user);
        contactRepository.save(contact);

        Optional<Contact> savedContact = contactRepository.findContactByEmail(contact.getEmail());
        if (savedContact.isPresent()) {
            return ContactResponse.builder()
                    .userName(savedContact.get().getUser().getUsername())
                    .email(savedContact.get().getEmail())
                    .firstName(savedContact.get().getFirstName())
                    .lastName(savedContact.get().getLastName())
                    .phone(savedContact.get().getPhone())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create contacts");
        }
    }

    @Transactional
    public ContactResponse getContactById(User user, int id) {
        Optional<Contact> contact = contactRepository.findContactById(BigInteger.valueOf(id));

        if (contact.isPresent()) {
            if (!Objects.equals(contact.get().getUser().getUsername(), user.getUsername())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            return ContactResponse.builder()
                    .userName(contact.get().getUser().getUsername())
                    .email(contact.get().getEmail())
                    .firstName(contact.get().getFirstName())
                    .lastName(contact.get().getLastName())
                    .phone(contact.get().getPhone())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact Not Found");
        }
    }


    @Transactional
    public ContactResponse updateContact(User user, int id, ContactUpdateRequest request) {
        validator.validate(request);

        log.info("Contact update HIT");
        log.info("Contact update HIT with ID {}", id);
        Optional<Contact> contact = contactRepository.findContactById(BigInteger.valueOf(id));
        if (contact.isPresent()) {

            log.info("contact email : {}", contact.get().getEmail());
            log.info("request email : {}", request.getEmail());

            if (!Objects.equals(request.getEmail(), contact.get().getEmail()) && contactRepository.existsContactByEmail(request.getEmail())) {
                log.error("Bad Request Error {}", new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact email already exists").getReason());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact email already exists");
            }
            contact.get().setEmail(request.getEmail());
            contact.get().setPhone(request.getPhone());
            contact.get().setFirstName(request.getFirstName());
            contact.get().setLastName(request.getLastName());
            contactRepository.save(contact.get());

            return ContactResponse.builder()
                    .userName(contact.get().getUser().getUsername())
                    .email(contact.get().getEmail())
                    .firstName(contact.get().getFirstName())
                    .lastName(contact.get().getLastName())
                    .phone(contact.get().getPhone())
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact not found");
        }
    }


}
