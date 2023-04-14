package com.andersentask.bookshop.utils.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class RepositorySerializer {
    private final ObjectMapper mapper = new ObjectMapper();

    public void serializeAndWriteToFile(List<?> list, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(mapper.writeValueAsString(list));
            log.info("Repository successfully saved");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}