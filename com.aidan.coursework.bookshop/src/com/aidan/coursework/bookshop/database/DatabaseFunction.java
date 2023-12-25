package com.aidan.coursework.bookshop.database;

import com.aidan.coursework.bookshop.errors.InvalidDatabaseEntry;

// We create this to typehint a method that
// returns a function that can throw database exceptions
@FunctionalInterface
public interface DatabaseFunction<T, R> {
	R apply(T variable) throws InvalidDatabaseEntry;

}
