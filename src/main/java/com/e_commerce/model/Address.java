package com.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    private String street;

    @NotBlank
    private String building;

    @NotBlank
    private String city;

    @NotBlank
    private String pincode;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    public Address(String street, String building, String city, String pincode, String state, String country) {
        this.street = street;
        this.building = building;
        this.city = city;
        this.pincode = pincode;
        this.state = state;
        this.country = country;
    }

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();
}
