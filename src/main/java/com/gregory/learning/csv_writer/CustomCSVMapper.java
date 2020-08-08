package com.gregory.learning.csv_writer;

public interface CustomCSVMapper<T> {

  String map(T object);

}
