//package io.github.fs314.KsParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.io.IOException;

import java.io.BufferedReader;

public class SplitCondition 
{
	ReadKSFile rks;
	
	/**
	* Constructor
	**/
	public SplitCondition()
	{
		rks = new ReadKSFile();
	}
	
	/**
	* 
	* @param 
	
	* @return 
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition(BufferedReader bufferedReader, Gson gson) throws IOException 
	{
		LinkedHashMap<String, ArrayList<KeystrokeData>> conditions = new LinkedHashMap<String, ArrayList<KeystrokeData>>(8);
		ArrayList<KeystrokeData> ksData = rks.getKsData(bufferedReader, gson);
		String serchString = fromAscii(rks.getLetterCodes(ksData));
		
		for(int i=0; i<getFlags().size(); i++) 
		{
			ArrayList<KeystrokeData> ksByCondition = new ArrayList<KeystrokeData>();
			
			int maxIndex = flagMaxIndex(serchString, getFlags().get(i));
		    int minIndex = flagMinIndex(serchString, getFlags().get(i)); 
			
			if (maxIndex !=0 && minIndex+7 < maxIndex)
			{
				for(int index = minIndex; index< maxIndex; index++)
				{
					ksByCondition.add(ksData.get(index));
				}
				conditions.put(getFlags().get(i), ksByCondition);
			} 
		}
		return conditions;
	} 
	
	
   /**
	* 
	* @param 
	* @return 
	**/
	public String getAnonCode(BufferedReader bufferedReader, Gson gson)
	{
		String serchString = fromAscii(rks.getLetterCodes(rks.getKsData(bufferedReader, gson)));
		
		String anonCode = "C";
		int maxIndex = flagMaxIndex(serchString, "code");
		int minIndex = flagMinIndex(serchString, "code");
		
		
		if(minIndex != -1 && maxIndex != -1)
		{
			anonCode += serchString.substring(minIndex, maxIndex);
		} else if (minIndex !=-1 && maxIndex == -1)
		{
			anonCode += serchString.substring(minIndex, minIndex+9);
		}
		
		Pattern p = Pattern.compile("code", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(anonCode);
		anonCode= m.replaceAll("");
		
		return anonCode;
	}
	
	
    /**
	* 
	* @param 
	* @return 
	**/
	public String flagDelimiter(String fromAscii, String currFlag)
	{
		String conditionString = " ";
		int maxIndex = flagMaxIndex(fromAscii, currFlag);
		int minIndex = flagMinIndex(fromAscii, currFlag);
		
		if(maxIndex !=0 && minIndex+7 < maxIndex) 
		{
			conditionString = fromAscii.substring(minIndex, maxIndex);
		}
		
		return conditionString;
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public int flagMinIndex(String fromAscii, String currFlag) 
	{
		ArrayList<Integer> flagIndexes = new ArrayList<Integer>();
		int minIndex=0;
		
		Matcher m = Pattern.compile(currFlag, Pattern.CASE_INSENSITIVE).matcher(fromAscii);
		while(m.find()) 
		{
			flagIndexes.add(m.start());
		}
		
		//loops over array of indexes for given flag to find maximum index of flag or end index of condition
		for(int i=0; i<flagIndexes.size(); i++) 
		{
			int currIndex = flagIndexes.get(i);
			if(i==0)
			{
			  minIndex = flagIndexes.get(i);	
			} else if (currIndex < minIndex)
			{
				minIndex = currIndex;
			}
		}
		return minIndex;
	}
	
	
	/**
	* 
	* @param 
	* @return 
	**/
	public int flagMaxIndex(String fromAscii, String currFlag) 
	{
		ArrayList<Integer> flagIndexes = new ArrayList<Integer>();
		int maxIndex=0;
		
		Matcher m = Pattern.compile(currFlag, Pattern.CASE_INSENSITIVE).matcher(fromAscii);
		while(m.find()) 
		{
			flagIndexes.add(m.end());
		}
		
		//loops over array of indexes for given flag to find maximum index of flag or end index of condition
		for(int i=0; i<flagIndexes.size(); i++) 
		{
			int currIndex = flagIndexes.get(i);
			if(i==0)
			{
			  maxIndex = flagIndexes.get(i);	
			} else if (currIndex > maxIndex)
			{
				maxIndex = currIndex;
			}
		}
		return maxIndex;
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public int flagOccurence(String fromAscii, String currFlag) 
	{
		int occ = 0;
		
		Matcher m = Pattern.compile(currFlag, Pattern.CASE_INSENSITIVE).matcher(fromAscii);
		while(m.find())
		{
			occ++;
		}
		
		return occ;
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public String fromAscii(ArrayList<Integer> letterCodes) 
	{
		StringBuilder finalStr = new StringBuilder();
		
		for(int i=0; i<letterCodes.size(); i++) 
		{
			int asciiCode = letterCodes.get(i);
			char asciiChar = (char) asciiCode;
			String translated = String.valueOf(asciiChar);
			finalStr.append(translated);
		}
		
		return finalStr.toString();
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public ArrayList<String> getFlags() 
	{
		ArrayList<String> flags = new ArrayList<String>();
		
		for(Flags flag: Flags.values())
		{
			flags.add(flag.toString());
		}
		return flags;
	}

}