package com.controledecomandas.models;

import com.controledecomandas.utils.GenerateHash;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class User {
    private int id;
    private String email;
    private String password;
    private boolean access;
    private String firstName;
    private String lastName;
    private String telephone;
    private BigDecimal salary;
    private String address;
    private String zipcode;

    public int getId() {
        return id;
    }

    public String getFullName() {

        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1) + " " +
                lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = GenerateHash.generate(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean getAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
