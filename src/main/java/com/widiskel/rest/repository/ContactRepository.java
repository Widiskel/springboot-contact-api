package com.widiskel.rest.repository;

import com.widiskel.rest.entity.Contact;
import com.widiskel.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    List<Contact> findContactsByUser(User user);
    Optional<Contact> findContactByUser(User user);
    Optional<Contact> findContactById(BigInteger id);
    Optional<Contact> findContactByEmail(String email);
    Boolean existsContactByEmail(String email);
}