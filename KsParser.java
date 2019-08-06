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
		ksp.getParsedFiles(args[0], args[1]);
		//ksp.test();
	
	}
	
	
	public void getParsedFiles(String originalPath, String destinationPath) 
	{
		try{
			wp.createParsedKS(originalPath, destinationPath);
		}
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (Exception e){e.printStackTrace();}
		
	} 
	
	public void test() throws FileNotFoundException
	{
		Gson gson = new Gson();
		BufferedReader bufferedReader = rks.getFiles("toParse//flami2.json");
	}
	
}
