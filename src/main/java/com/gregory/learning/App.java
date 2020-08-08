package com.gregory.learning;

import com.gregory.learning.csv_writer.CSVWriter;
import java.util.Collections;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    CSVWriter csvWriter = new CSVWriter();
    Person person = new Person("Sam", "Gregory", 28, 170, "1");
    csvWriter.writeToCsv(Collections.singletonList(person), "names");

  }
}
