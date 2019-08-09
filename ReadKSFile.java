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
	* extracts primary code(ascii) of each keystroke data and returns them into an ArrayList
	* @param ArrayList<KeystrokeData> 
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
	* 
	* @param 
	* @return 
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
	* 
	* @param 
	* @return 
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> accessKsLabels(BufferedReader bufferedReader, Gson gson) 
	{
		 Type mapType = new TypeToken<LinkedHashMap<String, ArrayList<KeystrokeData>> >() {}.getType();
         LinkedHashMap<String, ArrayList<KeystrokeData>> ksLabels = gson.fromJson(bufferedReader, mapType); 
         
		
		return ksLabels;
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public BufferedReader getFiles(String filename) throws FileNotFoundException
	{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
		
		return bufferedReader;
	}
}