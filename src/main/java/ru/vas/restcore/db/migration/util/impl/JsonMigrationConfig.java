package ru.vas.restcore.db.migration.util.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import ru.vas.restcore.db.migration.util.MigrationConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

@Slf4j
public class JsonMigrationConfig<T> implements MigrationConfig<T> {
    @Getter
    private T mappedResource;
    @Getter
    private Integer checksum;

    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeReference<T> typeReference;

    public JsonMigrationConfig(String filePath, TypeReference<T> typeReference) throws IOException {
        this.typeReference = typeReference;
        setChecksumAndResource(filePath);
    }

    private void setChecksumAndResource(String filePath) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
        try (CheckedInputStream checkedInputStream = new CheckedInputStream(classPathResource.getInputStream(), new CRC32())) {
            setMappedResource(checkedInputStream);
            setChecksum(checkedInputStream);
        } catch (Exception e) {
            log.error(String.format("Ошибка при миграции файла: '%s'", filePath), e);
            throw e;
        }
    }

    private void setChecksum(CheckedInputStream checkedInputStream) {
        this.checksum = new Long(checkedInputStream.getChecksum().getValue()).intValue();
    }

    private void setMappedResource(InputStream inputStream) throws IOException {
        this.mappedResource = objectMapper.readValue(inputStream, this.typeReference);
    }
}
