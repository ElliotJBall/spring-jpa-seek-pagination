package org.spring.seek.sample.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.spring.seek.annotation.SeekPaginationField;

@Entity
public class Customer {

  @Id
  @SeekPaginationField
  private Long id;

  private String firstName;

  private String lastName;

  private BigDecimal salary;

  public Customer() {

  }

  public Customer(final String firstName, final String lastName, final BigDecimal salary) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(final BigDecimal salary) {
    this.salary = salary;
  }

  @Override
  public String toString() {
    return "Customer{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
        + '\'' +
        ", salary=" + salary + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
