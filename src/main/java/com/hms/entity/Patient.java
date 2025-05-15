package com.hms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "aadhar_number", unique = true)
    private String aadharNumber;

    @Column(unique = true)
    private String mobile;

    private String address;

    private int age;

    @Column(name = "blood_group")
    private String bloodGroup;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Patient() {}

    // Getters and setters...

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
