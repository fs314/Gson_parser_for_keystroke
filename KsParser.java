//package io.github.fs314.KsParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.json.JsonReader;
import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class KsParser 
{
	ReadKSFile rks;
	SplitCondition sp;
	WriteParsedKS wp;
	
	/**
	* Constructor
	**/
	public KsParser()
	{
		rks = new ReadKSFile();
		sp = new SplitCondition();
		wp = new WriteParsedKS();
	} 
	
	/**
	* Main method. 
	* @param 
	**/
	public static void main (String[] args)  throws FileNotFoundException
	{
		KsParser ksp = new KsParser();
		
		if(args[0].equals("stringify")) 
		{
			ksp.stringify(args[1]);
		} else {
		    ksp.getParsedFiles(args[0], args[1]);
	    }
	}
	
	
	public void getParsedFiles(String originalPath, String destinationPath) 
	{
		try{
			wp.createParsedKS(originalPath, destinationPath);
		}
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (Exception e){e.printStackTrace();}
		
	} 
	
	public void stringify(String path) throws FileNotFoundException
	{
		Gson gson = new Gson();
		BufferedReader bufferedReader = rks.getFiles(path);
		
		String stringified = sp.fromAscii(rks.getLetterCodes(rks.getKsData(bufferedReader, gson)));
        System.out.println(stringified);		
	}
	
}
