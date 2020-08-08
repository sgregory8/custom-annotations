package com.gregory.learning.csv_writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CSVMetaData {

  String headerValue() default "";

  Class<?> customMapping() default Object.class;

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  @interface CSVIgnore {

  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  @interface CSVId {

  }
}
