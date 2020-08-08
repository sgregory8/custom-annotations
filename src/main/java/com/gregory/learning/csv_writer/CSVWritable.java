package com.gregory.learning.csv_writer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CSVWritable {

  String delimiter() default ",";

  String textQualifier() default "\"";

}
