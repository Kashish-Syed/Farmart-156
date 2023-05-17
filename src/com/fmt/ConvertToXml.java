package com.fmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

public class ConvertToXml {

	public static <T> void xml(Map<String, T> items, String path) {
		
		XStream xstream = new XStream();
		File file = new File(path);
		try {
			PrintWriter pw = new PrintWriter(file);
			String xml = xstream.toXML(items);
			pw.write(xml);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
