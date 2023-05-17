package com.fmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConvertToJson {

	/**
	 * Method that takes an ArrayList and a String to convert the csv files into
	 * Json
	 * 
	 * @param <T>
	 * @param items
	 * @param path
	 */
	public static <T> void json(Map<String, T> items, String path) {

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.setPrettyPrinting().create();

		File file = new File(path);
		try {
			PrintWriter pw = new PrintWriter(file);
			String json = gson.toJson(items);
			pw.write(json);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
