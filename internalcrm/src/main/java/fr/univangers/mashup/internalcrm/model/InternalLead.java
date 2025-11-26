package fr.univangers.mashup.internalcrm.model;

import java.util.Calendar;
import java.util.Objects;

@SuppressWarnings("unused")
public class InternalLead {
    private String firstName;
    private String lastName;
    private double annualRevenue;
    private String phone;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private Calendar creationDate;
    private String company;
    private String state;

    public InternalLead() {
    }

    public InternalLead(String firstName, String lastName, double annualRevenue, String phone, String street, String postalCode, String city, String country, Calendar creationDate, String company, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.annualRevenue = annualRevenue;
        this.phone = phone;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.creationDate = creationDate;
        this.company = company;
        this.state = state;
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

    public double getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(double annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InternalLead that = (InternalLead) o;
        return Double.compare(annualRevenue, that.annualRevenue) == 0 && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(phone, that.phone) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(country, that.country) && Objects.equals(creationDate, that.creationDate) && Objects.equals(company, that.company) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, annualRevenue, phone, street, postalCode, city, country, creationDate, company, state);
    }

    @Override
    public String toString() {
        return "InternalLead{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", annualRevenue=" + annualRevenue +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", creationDate=" + creationDate +
                ", company='" + company + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
