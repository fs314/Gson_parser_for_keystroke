//package io.github.fs314.KsParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
	public static void main (String[] args) throws IOException
	{
		KsParser ksp = new KsParser();
		
		if(args[0].equals("-tr")){
			ksp.printContent(args[1]);
		} else if (args[0].equals("-pr")){
		    ksp.getParsedFiles(args[1], args[2]);
		} else if (args[0].equals("-sc")){
			ksp.showConditions(args[1]);
		}else if(args[0].equals("-add")){
			ksp.addCondition(args[1], args[2], args[3], args[4]); 
		} else if(args[0].equals("-hp")){
			System.out.println(" ");
			System.out.println("TRANSLATE JSON: -tr NameOfFileToTranslate");
			System.out.println("SHOW CONDITIONS: -sc NameOfFileToTranslate");
			System.out.println("PARSE JSON: -pr FromFolder  ToFolder");
			System.out.println("ADD CONDITION: -add Filename, StartFrom, EndAt, ToCondition");
			System.out.println("HELP: -hp to know how the parser works");
			System.out.println(" ");
		}  
		//ksp.test();
	}
	
	/**
	* Main method. 
	* @param 
	**/
	public void getParsedFiles(String originalPath, String destinationPath) 
	{
		try{
			wp.createParsedKS(originalPath, destinationPath);
		}
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (Exception e){e.printStackTrace();}
		
	} 
	
	 /**
	* Main method. 
	* @param 
	**/
	public void showConditions(String filename) throws FileNotFoundException
	{
		for (int i=0; i<sp.getFlags().size(); i++) 
		{
			GsonBuilder gsonBuilder = new GsonBuilder();  
            gsonBuilder.setLenient();  
            Gson gson = gsonBuilder.create();
	
			System.out.println(" ");
			System.out.println("CONDITION " + sp.getFlags().get(i));
			System.out.println(sp.flagDelimiter(sp.fromAscii(rks.getLetterCodes(rks.getKsData (rks.getFiles(filename), gson))), sp.getFlags().get(i)));
		    System.out.println(" ");
		}
	} 
	
   /**
	* Main method. 
	* @param 
	**/
	public void printContent(String filename) throws FileNotFoundException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();  
        gsonBuilder.setLenient();  
        Gson gson = gsonBuilder.create();

		System.out.println(" ");
		System.out.println(sp.fromAscii(rks.getLetterCodes(rks.getKsData (rks.getFiles(filename), gson)))); 
	} 
	
	/**
	* Main method. 
	* @param 
	**/
	public void addCondition (String filename, String startFrom, String endAt, String toCondition) throws IOException
	{
		wp.modifyParsedKS(filename, startFrom, endAt, toCondition);
	}
	
	
	/*
	public void test()throws IOException
	{
		ArrayList<KeystrokeData> ks = sp.getCond("browncfox", "dogGdue", "GDYESVD", rks.getFiles("try/s4C/s4--1659614824kspattern.json"));
		String serchString = sp.fromAscii(rks.getLetterCodes(ks));
		System.out.println(serchString);
		
	} */
	

	
}
