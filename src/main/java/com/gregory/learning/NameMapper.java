package com.gregory.learning;

import com.gregory.learning.csv_writer.CustomCSVMapper;

public class NameMapper implements CustomCSVMapper<Person> {

  @Override
  public String map(Person person) {
    return person.getFirstName() + " " + person.getSecondName();
  }
}
