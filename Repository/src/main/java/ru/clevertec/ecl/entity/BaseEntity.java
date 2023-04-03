package ru.clevertec.ecl.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> {
    K getId();
}
