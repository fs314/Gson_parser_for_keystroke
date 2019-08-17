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
	* Starts the parser upon specification of the action to be carried out. 
	* Type -hp to start to receive some guidance on how to use the parser. 
	* @param String[] specifing the action to be carried out by parser. 
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
		}else if (args[0].equals("-rm")){
			ksp.deleteFromFile(args[1], args[2], args[3]);
		} else if(args[0].equals("-hp")){
			System.out.println(" ");
			System.out.println("TRANSLATE JSON: -tr NameOfFileToTranslate");
			System.out.println("SHOW CONDITIONS: -sc NameOfFileToTranslateByCondition");
			System.out.println("PARSE JSON: -pr FromFolder  ToFolder");
			System.out.println("ADD CONDITION: -add FileToModify StartAddingFrom EndAddingAt AddToCondition");
			System.out.println("REMOVE FROM FILE: -rm FileToModify StartRemovingFrom EndRemovingAt");
			System.out.println("HELP: -hp to know how the parser works");
			System.out.println(" ");
		}  
	}
	
	/**
	* Parses specified JSON files and creates a new parsed version for each at specified location.
	* @param String the original location of the files to be parsed, String representing the destination path for the newly created parsed files.
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
	* Prints text originally typed by splitting it by the condition it belongs to. Text not belonging to any condition(dynamic) is not included.  
	* @param String filename of file to be translated.
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
	* Print text originally typed contained in JSON file by translating it from ascii.
	* @param String filename of file to be translated.
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
	* Allows to add a condition with data associated to it to a specified file. 
	* The name of the new condition has to be specified as well as the start and end "flags" which delimit the new conditon data.
	* @param String filename of file to be modified, String indicating from where to start include data to new condition, String indicating where to stop the inclusion,
    * String representing the name of the new condition. 
	**/
	public void addCondition (String filename, String startFrom, String endAt, String toCondition) throws IOException
	{
		wp.modifyParsedKS(filename, startFrom, endAt, toCondition);
	}
	
	/**
	* Allows to delete data from specified file.
	* @param String filename of file to be modified, String indicating from where to start removal of data, String indicating where to stop removal.
	**/
	public void deleteFromFile(String filename, String startFrom, String endAt) throws IOException
	{
		wp.deleteFromFile(filename, startFrom, endAt);
	}
	
}
