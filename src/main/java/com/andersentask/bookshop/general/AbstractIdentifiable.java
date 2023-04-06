package com.andersentask.bookshop.general;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class used to avoid code duplication for entities that require a unique identifier.
 * This class provides the unique identifier(id field) for the class instances.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}
