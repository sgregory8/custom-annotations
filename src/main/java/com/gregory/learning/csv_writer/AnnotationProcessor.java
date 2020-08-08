package com.gregory.learning.csv_writer;

import com.gregory.learning.csv_writer.CSVMetaData.CSVId;
import com.gregory.learning.csv_writer.CSVMetaData.CSVIgnore;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationProcessor {

  public String getDelimiter() {
    return delimiter;
  }

  public String getTextQualifier() {
    return textQualifier;
  }

  private String delimiter = ",";
  private String textQualifier = "\"";

  protected Map<String, WritableObjectDetails> processAnnotations(Object object) {

    Annotation[] annotations = object.getClass().getDeclaredAnnotations();

    boolean isWritable = false;
    for (Annotation annotation : annotations) {
      if (annotation instanceof CSVWritable) {
        isWritable = true;
        delimiter = ((CSVWritable) annotation).delimiter();
        textQualifier = ((CSVWritable) annotation).textQualifier();
        break;
      }
    }
    if (!isWritable) {
      throw new RuntimeException("Not annotated with @CSVWritable");
    }

    return processObject(object);
  }

  private Map<String, WritableObjectDetails> processObject(Object object) {

    Map<String, WritableObjectDetails> headerToWriterObjectMap;

    List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields())
        // remove synthetic fields
        .filter(field -> !field.isSynthetic())
        // remove any field that has @CSVIgnore attached to any of it's annotations
        .filter(field -> !containsCsvIgnore(field))
        .collect(Collectors.toList());

    // validate the use of @CSVId and sort the fields in that specific order
    List<Field> sortedFields = validateCsvId(fields);

    // process the fields
    return headerToWriterObjectMap = processCsvMetaData(sortedFields);
  }

  private List<Field> validateCsvId(List<Field> fields) {
    List<Field> sortedFields = new ArrayList<>();
    int count = 0;
    Field fieldToRemove = null;
    for (Field field : fields) {
      for (Annotation annotation : field.getDeclaredAnnotations()) {
        if (annotation instanceof CSVId) {
          count = count + 1;
          sortedFields.add(field);
          fieldToRemove = field;
        }
      }
    }
    if (count > 1) {
      throw new RuntimeException("Can't have multiple @CSVId annotations");
    }
    if (fieldToRemove != null) {
      fields.remove(fieldToRemove);
    }
    sortedFields.addAll(fields);

    return sortedFields;
  }

  private Map<String, WritableObjectDetails> processCsvMetaData(List<Field> fields) {

    Map<String, WritableObjectDetails> headerToWriterObject = new LinkedHashMap<>();

    for (Field field : fields) {
      String headerValue = field.getName();
      CustomCSVMapper mapper = null;
      for (Annotation annotation : field.getDeclaredAnnotations()) {
        if (annotation instanceof CSVMetaData) {
          headerValue =
              !((CSVMetaData) annotation).headerValue().equals("") ? ((CSVMetaData) annotation)
                  .headerValue() : field.getName();
          Class<?> clazz = ((CSVMetaData) annotation).customMapping() == Object.class ? null
              : ((CSVMetaData) annotation).customMapping();
          if (clazz != null) {
            try {
              mapper = (CustomCSVMapper) clazz.getConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
              throw new RuntimeException(e);
            }
          }
        }
      }
      headerToWriterObject.put(headerValue, new WritableObjectDetails(field, mapper));
    }
    return headerToWriterObject;
  }

  private boolean containsCsvIgnore(Field field) {
    boolean containsAnnotation = false;
    for (Annotation annotation : field.getDeclaredAnnotations()) {
      if (annotation instanceof CSVIgnore) {
        containsAnnotation = true;
      }
    }
    return containsAnnotation;
  }
}
