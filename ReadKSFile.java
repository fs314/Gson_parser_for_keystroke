//package io.github.fs314.KsParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.BufferedReader;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.*;
import java.util.*;

public class ReadKSFile  
{
	/**
	* Extracts primary code(ascii) for each keystroke data and returns them into an ArrayList<Integer>.
	* @param ArrayList<KeystrokeData> containing the details for each keystroke
	* @return ArrayList<Integer> containing the primary code value of each keystroke in the original JSON file
	**/
	public ArrayList<Integer> getLetterCodes(ArrayList<KeystrokeData> ksData)
	{
		ArrayList<Integer> letterCodes = new ArrayList<Integer>();
		for (int i=0; i<ksData.size(); i++) 
		{
			letterCodes.add(ksData.get(i).getPrimaryCode());
		}
		return letterCodes;
	}
	
	/**
	* Returns an ArrayList of information associated to each keystroke in the JSON file being parsed.
	* @param BufferedReader buffered file to be parsed and Gson gson object to parse a JSON to java class and begin deserialization of json file.
	* @return ArrayList<KeystrokeData> containing information associated to each keystroke in the JSON file being parsed
	**/
	public ArrayList<KeystrokeData> getKsData (BufferedReader bufferedReader, Gson gson) 
	{
		LinkedHashMap<String, ArrayList<KeystrokeData>> ksLabels = accessKsLabels(bufferedReader, gson);  
		
		ArrayList<KeystrokeData>  ksData = new ArrayList<KeystrokeData>();
		
		for (String key: ksLabels.keySet()) 
		{
			for(int i=0; i<ksLabels.get(key).size(); i++)
			{
				ksData.add(ksLabels.get(key).get(i));
			} 
		}
		return ksData;
	}
	
	
	/**
	* Returns a LinkedHashMap<String, ArrayList<KeystrokeData>> which specifies the correct parameterized type for the dynamic fields in the data 
	* thus allowing Gson to parse the data. 
	* @param BufferedReader buffered file to be parsed and Gson gson object to parse a JSON to java class and begin deserialization of json file.
	* @return LinkedHashMap<String, ArrayList<KeystrokeData>> associating a label (often dyamically generated) to an ArrayList of keystrokeData
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> accessKsLabels(BufferedReader bufferedReader, Gson gson) 
	{
		 Type mapType = new TypeToken<LinkedHashMap<String, ArrayList<KeystrokeData>> >() {}.getType();
         LinkedHashMap<String, ArrayList<KeystrokeData>> ksLabels = gson.fromJson(bufferedReader, mapType); 
         
		
		return ksLabels;
	}
	
	/**
	* Reads json file specified, buffering so as to provide efficient reading.
	* @param String the name of the file to read
	* @return BufferedReader buffered file to be parsed
	**/
	public BufferedReader getFiles(String filename) throws FileNotFoundException
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		
		return bufferedReader;
	}
}