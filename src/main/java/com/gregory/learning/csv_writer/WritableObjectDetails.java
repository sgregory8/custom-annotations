package com.gregory.learning.csv_writer;

import com.gregory.learning.csv_writer.CustomCSVMapper;
import java.lang.reflect.Field;

public class WritableObjectDetails {

  public WritableObjectDetails(Field field, CustomCSVMapper mapper) {
    this.field = field;
    this.mapper = mapper;
  }

  private Field field;
  private CustomCSVMapper mapper;

  public Field getField() {
    return field;
  }

  public CustomCSVMapper getMapper() {
    return mapper;
  }
}
