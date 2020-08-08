package com.gregory.learning.csv_writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVWriter {

  private final AnnotationProcessor annotationProcessor = new AnnotationProcessor();

  public void writeToCsv(List<?> objects, String fileName) {

    Map<String, WritableObjectDetails> headerToWriterDetailsMap = annotationProcessor
        .processAnnotations(objects.get(0));

    String delimiter = annotationProcessor.getDelimiter();
    String textQualifier = annotationProcessor.getTextQualifier();

    List<String> csvLines = new ArrayList<>();

    // traverse the map to write the header line
    String headerLine;
    StringBuilder headerBuilder = new StringBuilder();
    headerToWriterDetailsMap.forEach((key, value) -> {
      headerBuilder.append(textQualifier);
      headerBuilder.append(key);
      headerBuilder.append(textQualifier);
      headerBuilder.append(delimiter);
    });
    headerBuilder.setLength(headerBuilder.length() - 1);
    headerLine = headerBuilder.toString();
    csvLines.add(headerLine);
    // traverse the map to write the lines

    for (Object object : objects) {
      String csvLine;
      StringBuilder lineBuilder = new StringBuilder();
      headerToWriterDetailsMap.forEach((key, value) -> {
        String csvValue;
        try {
          Field field = object.getClass().getDeclaredField(value.getField().getName());
          field.setAccessible(true);
          csvValue = field.get(object).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new RuntimeException(e);
        }
        // get the custom mapper if there is one
        CustomCSVMapper mapper = value.getMapper();

        // if there is overwrite the value with the mapper provided value
        if (mapper != null) {
          csvValue = mapper.map(object);
        }
        lineBuilder.append(textQualifier);
        lineBuilder.append(csvValue);
        lineBuilder.append(textQualifier);
        lineBuilder.append(delimiter);
      });
      lineBuilder.setLength(lineBuilder.length() - 1);
      csvLine = lineBuilder.toString();
      csvLines.add(csvLine);
    }
    try {
      writeToCsvFile(csvLines, fileName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void writeToCsvFile(List<String> csvLines, String fileName) throws IOException {
    String tmpDir = System.getProperty("java.io.tmpdir");
    BufferedWriter writer = new BufferedWriter(
        new FileWriter(tmpDir + File.separator + fileName + ".csv"));

    csvLines.forEach(line -> {
      try {
        writer.append(line);
        writer.newLine();
      } catch (IOException e) {
        throw new RuntimeException("Error occurred with writer", e);
      }
    });
    writer.close();

  }

}
