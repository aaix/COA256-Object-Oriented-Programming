package com.aidan.coursework.bookshop.database;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.ArrayList;


import com.aidan.coursework.bookshop.errors.InvalidDatabaseEntry;

import java.util.HashMap;
import java.util.Map;

// If we can store data in a text file we can afford to store it all in memory
public class FileHandler<T extends DatabaseCompatible> {
	
	File file;
	HashMap<Integer, T> map;

	public FileHandler(String file) throws FileNotFoundException {
		this.file = new File(file);
		if (!this.file.exists()) {
			throw new FileNotFoundException(file);
		};
		this.map = new HashMap<Integer, T>();
		read_from_db();
	};
	
	private void read_from_db() {
		
		// because generics are not properly implemented into the language
		// "to ensure backwards compatibility" there is no way to access
		// the should-be static deseraliser using the generic, completely
		// defeating the whole point of the oop style 
		// this is purely an arbitary restriction of java as a language >:(

		
		Scanner reader;
		try {
			reader = new Scanner(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		while (reader.hasNextLine()) {
			String line = reader.nextLine().trim();
			T obj;
			try {
				obj = (T) Deserialiser.deseralise(line);
			} catch (InvalidDatabaseEntry e) {
				e.printStackTrace();
				continue;
			};
			
			this.map.put(obj.id(), obj);
		}
		reader.close();
	};
	
	private void write_to_db() {

		FileWriter writer;
		try {
			writer = new FileWriter(this.file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		};
		
		try {
			// for each entry in the map we get the values serialiser
			// and serialise each entry to a string then write it to the file
			for (Map.Entry<?, T> entry : map.entrySet()) {
				T obj = entry.getValue();
				writer.write(obj.serialise() + "\n");
			};
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {writer.close();} catch (IOException e) {}; // WHY CAN THIS EVEN THROW
		}

	};
	
	public T get_by_id(int id) {
		return this.map.get(id);
	}

	// This can be used to add aswell as modify
	public void set_value_by_id(int id, T value) {		
		map.put(id, value);
		write_to_db();
	}
	
	// If we have modified write to the database
	public void update() {
		write_to_db();
	}
	
	public ArrayList<T> iterate() {
		return new ArrayList<T>(this.map.values());
	}
}

