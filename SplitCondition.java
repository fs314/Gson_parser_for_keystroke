//package io.github.fs314.KsParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.*;
import java.io.IOException;
import com.google.common.collect.*;

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
	* Removes data from an ArrayList<KeystrokeData> starting from and ending at position specified by user.
	* @param ArrayList<KeystrokeData> containing keystroke data associated with one JSON file, String indicating from where to start removal of data in ArrayList<KeystrokeData>, String indicating where to stop removal. 
	* @return ArrayList<KeystrokeData> only containing data that has not been removed.
	**/
	public ArrayList<KeystrokeData> deleteFrom(ArrayList<KeystrokeData> ksData, String startFrom, String endAt)
	{
	   ArrayList<KeystrokeData> fixedData = new ArrayList<KeystrokeData>();
	   
	   String serchString = fromAscii(rks.getLetterCodes(ksData));
	   int minIndex = flagMinIndex(serchString, startFrom);
	   int maxIndex = flagMaxIndex(serchString, endAt);
	  
   
	   if(minIndex > 0)
	   {
		    for(int i=0; i<minIndex; i++)
			{
				fixedData.add(ksData.get(i));
			}

			if(maxIndex<ksData.size())
			{
				for(int i=maxIndex; i<ksData.size(); i++)
				{
					fixedData.add(ksData.get(i));
			    }
			}
			   
	   } else if(minIndex==0){
		   for(int i=maxIndex; i<ksData.size(); i++)
		   {
			   fixedData.add(ksData.get(i));
		   }
	   }
	return fixedData;	
	}

    /**
	* Takes two user defined Strings - startFrom and endAt - which the parser will interpret as new flags and use to delimit data to be stored in an ArrayList<KeystrokeData>.
	* @param String indicating from where to start include data in ArrayList<KeystrokeData>, String indicating where to stop the inclusion, BufferedReader buffered file to be parsed.  
	* @return ArrayList<KeystrokeData> all the data contained in two user defined flags. 
	**/
	public ArrayList<KeystrokeData> getCond(String startFrom, String endAt, BufferedReader bufferedReader) throws IOException 
	{
	   GsonBuilder gsonBuilder = new GsonBuilder();  
       gsonBuilder.setLenient();  
       Gson gson = gsonBuilder.create();
	   
	   ArrayList<KeystrokeData> ksData = rks.getKsData(bufferedReader, gson);
	   String serchString = fromAscii(rks.getLetterCodes(ksData));
	   ArrayList<KeystrokeData> ksByCondition = new ArrayList<KeystrokeData>();
	   
	   int minIndex = flagMinIndex(serchString, startFrom);
	   int maxIndex = flagMaxIndex(serchString, endAt);
	   
	   for(int index = minIndex; index< maxIndex; index++)
	   {
		   ksByCondition.add(ksData.get(index));
	   }

	   return ksByCondition;
	}
	
	/**
	* Extracts all the data in JSON file that is not associated to any condition and returns it in a LinkedHashMap<String, ArrayList<KeystrokeData>>(for simplicity in serialization and deserialization)
    * where the key is the String "dynamicKS".
	* @param BufferedReader buffered file to be parsed and Gson gson object to parse a JSON to java class and begin deserialization of json file.
	* @return LinkedHashMap<String, ArrayList<KeystrokeData>> associating the String "dynamicKS" to all the data that is not associated to any condition in JSON file. 
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> fromDynamic(BufferedReader bufferedReader, Gson gson) throws IOException 
	{
		LinkedHashMap<String, ArrayList<KeystrokeData>>  dynamicData = new LinkedHashMap<String, ArrayList<KeystrokeData>>(); 
        ArrayList<KeystrokeData> dynamicKS = new  ArrayList<KeystrokeData>();
		
		
		ArrayList<KeystrokeData> ksData = rks.getKsData(bufferedReader, gson);
		String serchString = fromAscii(rks.getLetterCodes(ksData));
		LinkedHashMap<String, ArrayList<Integer>> validFlags = getValidFlags(serchString); 
		
		int maxIndexPrev = 0;
		if(validFlags.get("minIdx").size() !=0 && validFlags.get("maxIdx").size() !=0)
		{
			for(int i=0; i<validFlags.get("minIdx").size(); i++) 
		    {
			    int minIndexCurr = validFlags.get("minIdx").get(i);
			    int maxIndexCurr = validFlags.get("maxIdx").get(i);
			
			    if(i==0 && minIndexCurr>0)
			    {
					for(int index=0; index<minIndexCurr; index++)
					{
						dynamicKS.add(ksData.get(index));
				    }	
				} 
				
				if(i==validFlags.get("maxIdx").size()-1 && maxIndexCurr<serchString.length()) {
					for(int index=maxIndexCurr; index<serchString.length(); index++)
				    {
						dynamicKS.add(ksData.get(index));
				    }
			    } 
				if (i!=0 && minIndexCurr!=0){
					for(int index=maxIndexPrev; index<minIndexCurr; index++)
					{
						dynamicKS.add(ksData.get(index));
				    }
			    }
				 maxIndexPrev =  maxIndexCurr;
		    }
		}
		dynamicData.put("dynamicKS", dynamicKS);
		return dynamicData;
	} 

   /**
	* Extracts all the data from JSON file that is associated to certain study conditions 
	* and returns it all together in an ArrayList<KeystrokeData>
	* @param LinkedHashMap<String, ArrayList<KeystrokeData>> containing all the data in the JSON file that has been associated to at least one condition
	* @return ArrayList<KeystrokeData> containing all the data from JSON file that has been associated to t least one or more conditions.
	**/
     public ArrayList<KeystrokeData> getConditionData(LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition) 
	 {
		 ArrayList<KeystrokeData> conditionData = new ArrayList<KeystrokeData>();
		 
		 for(String key: fromCondition.keySet())
		 {
			for(int i=0; i<fromCondition.get(key).size(); i++)
			{
				conditionData.add(fromCondition.get(key).get(i));
			} 
		 }
		 return conditionData;
	 }
	

   /**
    * Splits the data contained in JSON file according to the condition it belongs to by looking for occurences of each flag associated to a condition 
	* and extracting all the data that is contained between the first and last occurrence as an ArrayList<KeystrokeData>. 
	* It then sorts it and returns it as an LinkedHashMap<String, ArrayList<KeystrokeData>> where the key represents the flag associated to the ArrayList<KeystrokeData>.  
	* @param ArrayList<KeystrokeData> containing all keystrokeData associated to each keystroke. 
	* @return LinkedHashMap<String, ArrayList<KeystrokeData>> associating the flag for each condition to the keystroke data relative to that condition.
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition(ArrayList<KeystrokeData> ksData) //throws IOException 
	{
		LinkedHashMap<String, ArrayList<KeystrokeData>> conditions = new LinkedHashMap<String, ArrayList<KeystrokeData>>(8);
		//ArrayList<KeystrokeData> ksData = rks.getKsData(bufferedReader, gson);
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
	* Splits the data contained in JSON file according to the condition it belongs to by looking for occurences of each flag associated to a condition 
	* and extracting all the data that is contained between the first and last occurrence as an ArrayList<KeystrokeData>. 
	* It then sorts it and returns it as an LinkedHashMap<String, ArrayList<KeystrokeData>> where the key represents the flag associated to the ArrayList<KeystrokeData>.   
	* @param BufferedReader buffered file to be parsed and Gson gson object to parse a JSON to java class and begin deserialization of json file.
	* @return LinkedHashMap<String, ArrayList<KeystrokeData>> associating the flag for each condition to the keystroke data relative to that condition.
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
	* Finds and returns the anonymous code inputed by the participant by looking for two occurences of the flag "code" 
	* and returning the substring within them. If no anonymous code is found then returns a "C".
	* @param BufferedReader buffered file to be parsed and Gson gson object to parse a JSON to java class and begin deserialization of json file.
	* @return String representing the anonymous code inputted by participant.
	**/
	public String getAnonCode(BufferedReader bufferedReader, Gson gson)
	{
		String serchString = fromAscii(rks.getLetterCodes(rks.getKsData(bufferedReader, gson)));
		
		String anonCode = "C";
		int maxIndex = flagMaxIndex(serchString, "code");
		int minIndex = flagMinIndex(serchString, "code");
		
		
		if(minIndex != -1 && maxIndex != -1)
		{
			anonCode += serchString.substring(minIndex, maxIndex).replace("?", "").replace(" ", "");
		} else if (minIndex !=-1 && maxIndex == -1)
		{
			anonCode += serchString.substring(minIndex, minIndex+9).replace("?", "").replace(" ", "");
		}
		
		Pattern p = Pattern.compile("code", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(anonCode);
		anonCode= m.replaceAll("");
		
		return anonCode;
	}
	
   /**
	* Returns a LinkedHashMap<String, ArrayList<Integer>> associates each flag to the maximum and minimum indexes at which it occurred in a given strings
	* @param String string to be searched
	* @return LinkedHashMap<String, ArrayList<Integer>> associating the name of a flag 
	* to the maximum and minimum index of occurence in the given string
	**/
	public LinkedHashMap<String, ArrayList<Integer>> getValidFlags(String fromAscii) 
	{
		LinkedHashMap<String, ArrayList<Integer>> validFlags = new LinkedHashMap<String, ArrayList<Integer>>();
		
		ArrayList<Integer> minIdxs = new ArrayList<Integer>();
		ArrayList<Integer> maxIdxs = new ArrayList<Integer>();
		for (int i=0; i< getFlags().size(); i++) 
		{
			int maxIndex = flagMaxIndex(fromAscii, getFlags().get(i));
		    int minIndex = flagMinIndex(fromAscii, getFlags().get(i));
		
		    if(maxIndex !=0) 
		    {
				minIdxs.add(minIndex);
				maxIdxs.add(maxIndex);
		    }
		}
		
		validFlags.put("minIdx", sortAscending(minIdxs));
		validFlags.put("maxIdx", sortAscending(maxIdxs));

		return validFlags;
	}
	
	/**
	* Sorts the int elements(Indexes) of a given ArrayList<Integer> with the smaller values at the smallest indexes of  the ArrayList.
	* @param ArrayList<Integer> of Integers to sort in ascending order
	* @return ArrayList<Integer> with values sorted in ascending order 
	**/
	public ArrayList<Integer> sortAscending(ArrayList<Integer> indexes)
	{
		ArrayList<Integer> ascendingIdxs = new ArrayList<Integer>();
		
		while(indexes.size() > 0)
		{
			int minIndex = 0;
			for(int i=0; i< indexes.size(); i++)
		    {
				int currIndex = indexes.get(i);
				if(i==0)
				{
					minIndex = currIndex;
				} else if (currIndex< minIndex){
					minIndex = currIndex;
			    }
		    }
			
			ascendingIdxs.add(minIndex);
			indexes.remove(Integer.valueOf(minIndex));
		}
    return ascendingIdxs;
	}
	
    /**
	* Returns the substring enclosed between the first and last occurrences of a given flag into a given string. 
	* @param String string to be searched and String string to be looked for in the other given string.
	* @return String substring enclosed between two (or more) occurrences of a string, currFlag, into a given string, fromAscii.
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
	* Finds all the occurrences of a given flag into a given string and returns the start index of the first found flag. 
	* @param String string to be searched and String string to be looked for in the other given string.
	* @return Integer of minimum index of given flag
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
	* Finds all occurrences of a given flag into a given string and returns the end index of the last found flag. 
	* @param String string to be searched and String string to be looked for in the other given string.
	* @return Integer of maximum index of given flag
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
	* Counts the time a given flag is found within a given string. 
	* @param String string to be searched and String string to be looked for in the other given string.
	* @return Integer number of time currFlag has occurred within the given string, fromAscii
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
	* Returns text originally typed translating it from ascii.
	* @param ArrayList<Integer> containing the letter codes (ascii) associated to each keystroke in JSON file being parsed.
	* @return String where the content of JSON file are decoded into text
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
	* Gets the list of flags contained in the enum class and returns it as an ArrayList<String>
	* @return an ArrayList<String> of flags. 
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