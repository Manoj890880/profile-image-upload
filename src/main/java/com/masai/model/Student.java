package com.masai.model;

import java.math.BigDecimal;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private BigDecimal rollNo;
	private Date dateOfBirth;
	private String address;
	private String profilePic;

	public Student() {
	}

	public Student(Long id, String name, BigDecimal rollNo, Date dateOfBirth, String address, String profilePic) {
		this.id = id;
		this.name = name;
		this.rollNo = rollNo;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.profilePic = profilePic;
	}

	public Student(String name, BigDecimal rollNo, Date dateOfBirth, String address, String profilePic) {
		this.name = name;
		this.rollNo = rollNo;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.profilePic = profilePic;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRollNo() {
		return rollNo;
	}

	public void setRollNo(BigDecimal rollNo) {
		this.rollNo = rollNo;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				", rollNo=" + rollNo +
				", dateOfBirth=" + dateOfBirth +
				", address='" + address + '\'' +
				", profilePic='" + profilePic + '\'' +
				'}';
	}
}









