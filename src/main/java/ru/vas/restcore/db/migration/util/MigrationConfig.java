package ru.vas.restcore.db.migration.util;

public interface MigrationConfig<T> {
    T getMappedResource();
    Integer getChecksum();
}
