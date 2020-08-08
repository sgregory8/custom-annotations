package com.gregory.learning;

import com.gregory.learning.csv_writer.CSVMetaData;
import com.gregory.learning.csv_writer.CSVMetaData.CSVId;
import com.gregory.learning.csv_writer.CSVMetaData.CSVIgnore;
import com.gregory.learning.csv_writer.CSVWritable;

@CSVWritable(delimiter = "|", textQualifier = "'")
public class Person {

  public Person(String firstName, String secondName, int age, int height, String id) {
    this.firstName = firstName;
    this.secondName = secondName;
    this.age = age;
    this.height = height;
    this.id = id;
  }

  @CSVMetaData(headerValue = "FULL_NAME", customMapping = NameMapper.class)
  private String firstName;

  @CSVIgnore
  private String secondName;

  private int age;

  @CSVMetaData(headerValue = "HEIGHT")
  private int height;

  @CSVId
  private String id;

  public String getFirstName() {
    return firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public int getAge() {
    return age;
  }

  public int getHeight() {
    return height;
  }

  public String getId() {
    return id;
  }
}
