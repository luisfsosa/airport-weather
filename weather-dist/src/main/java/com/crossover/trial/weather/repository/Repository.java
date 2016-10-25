package com.crossover.trial.weather.repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author felipe
 *
 * @param <T>
 * @param <ID>
 */
public interface Repository<T, ID extends Serializable> {

    /**
     * Returns all instances of the type.
     * 
     * @return all entities
     */
    List<T> findAll();

    /**
     * Deletes the entity with the given id.
     * 
     * @param id
     *            id to set.
     */
    void delete(ID id);

    /**
     * Deletes a given entity.
     * 
     * @param entity
     *            entity to set.
     */
    void delete(T entity);

    /**
     * Retrieves an entity by its id.
     * 
     * @param id
     *            id to set.
     * @return the entity with the given id or null if none found
     */
    T findOne(ID id);

    /**
     * Saves all given entities.
     * 
     * @param entities
     *            entities to set.
     * @return the saved entities
     */

    Iterable<T> save(Iterable<T> entities);

    /**
     * Saves a given entity.
     * 
     * @param entity
     *            entity to set.
     * @return the saved entity
     */
    T save(T entity);

}
