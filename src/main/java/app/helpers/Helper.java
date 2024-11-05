/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.helpers;

import app.dto.GuestDto;
import app.dto.InvoiceDetailDto;
import app.dto.InvoiceDto;
import app.dto.PersonDto;
import app.dto.UserDto;
import app.model.Person;
import app.model.User;
import app.dto.PartnerDto;
import app.model.Guest;
import app.model.Invoice;
import app.model.InvoiceDetail;
import app.model.Partner;

/**
 *
 * @author Camilo
 */
public abstract class Helper {

    public static PersonDto parse(Person person) {

        if (person == null) {
            return null;
        }
        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setDocument(person.getDocument());
        personDto.setName(person.getName());
        personDto.setCelPhone(person.getCelPhone());
        return personDto;
    }

    public static Person parse(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }
        Person person = new Person();
        person.setId(personDto.getId());
        person.setDocument(personDto.getDocument());
        person.setName(personDto.getName());
        person.setCelPhone(personDto.getCelPhone());
        return person;
    }

    public static UserDto parse(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPersonId(parse(user.getPersonId()));
        userDto.setRole(user.getRole());
        userDto.setUserName(user.getUserName());
        return userDto;
    }

    public static User parse(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setPersonId(parse(userDto.getPersonId()));
        user.setRole(userDto.getRole());
        user.setUserName(userDto.getUserName());
        return user;
    }

    public static Partner parse(PartnerDto partnerDto) {

        if (partnerDto == null) {
            return null;
        }
        Partner partner = new Partner();
        partner.setId(partnerDto.getId());
        partner.setUserId(parse(partnerDto.getUserId()));
        partner.setMoney(partnerDto.getMoney());
        partner.setType(partnerDto.getType());
        partner.setDateCreated(partnerDto.getDateCreated());
        return partner;
    }

    public static PartnerDto parse(Partner partner) {
        if (partner == null) {
            return null;
        }
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.setId(partner.getId());
        partnerDto.setUserId(parse(partner.getUserId()));
        partnerDto.setMoney(partner.getMoney());
        partnerDto.setType(partner.getType());
        partnerDto.setDateCreated(partner.getDateCreated());
        return partnerDto;
    }

    public static Guest parse(GuestDto guestDto) {
        if (guestDto == null) {
            return null;
        }
        Guest guest = new Guest();
        guest.setId(guestDto.getId());
        guest.setUserId(parse(guestDto.getUserId()));
        guest.setPartnerId(parse(guestDto.getPartnerId()));
        guest.setStatus(guestDto.getStatus());

        return guest;
    }

    public static GuestDto parse(Guest guest) {
        if (guest == null) {
            return null;
        }
        GuestDto guestDto = new GuestDto();
        guestDto.setId(guest.getId());
        guestDto.setUserId(parse(guest.getUserId()));
        guestDto.setPartnerId(parse(guest.getPartnerId()));
        guestDto.setStatus(guest.getStatus());

        return guestDto;
    }
     public static InvoiceDto parse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setPersonId(parse(invoice.getPersonId()));
        invoiceDto.setPartnerId(parse(invoice.getPartnerId()));
        invoiceDto.setDateCreated(invoice.getDateCreate());
        invoiceDto.setAmount(invoice.getAmount());
        invoiceDto.setStatus(invoice.getStatus());
        

        return invoiceDto;
    }
     public static Invoice parse(InvoiceDto invoiceDto) {
        if (invoiceDto == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.getId());
        invoice.setPersonId(parse(invoiceDto.getPersonId()));
        invoice.setPartnerId(parse(invoiceDto.getPartnerId()));
        invoice.setDateCreate(invoiceDto.getDateCreated());
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setStatus(invoiceDto.getStatus());
        

        return invoice;
    }
      public static InvoiceDetailDto parse(InvoiceDetail invoiceDetail) {
        if (invoiceDetail == null) {
            return null;
        }
        InvoiceDetailDto invoiceDetailDto = new InvoiceDetailDto();
        invoiceDetailDto.setId(invoiceDetail.getId());
        invoiceDetailDto.setInvoiceId(parse(invoiceDetail.getInvoiceId()));
        invoiceDetailDto.setItem(invoiceDetail.getItem());
        invoiceDetailDto.setDescription(invoiceDetail.getDescription());
        invoiceDetailDto.setAmount(invoiceDetail.getAmount());
        

        return invoiceDetailDto;
}
        public static InvoiceDetail parse(InvoiceDetailDto invoiceDetailDto) {
        if (invoiceDetailDto == null) {
            return null;
        }
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setId(invoiceDetailDto.getId());
        invoiceDetail.setInvoiceId(parse(invoiceDetailDto.getInvoiceId()));
        invoiceDetail.setItem(invoiceDetailDto.getItem());
        invoiceDetail.setDescription(invoiceDetailDto.getDescription());
        invoiceDetail.setAmount(invoiceDetailDto.getAmount());
        

        return invoiceDetail;
}
}
