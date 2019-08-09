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
	* 
	* @param 
	* @return 
	**/
	public ArrayList<KeystrokeData> deleteFrom(ArrayList<KeystrokeData> condition, String startFrom, String endAt)
	{
	   String serchString = fromAscii(rks.getLetterCodes(condition));
	   int minIndex = flagMinIndex(serchString, startFrom);
	   int maxIndex = flagMaxIndex(serchString, endAt);
	  
        	  
	   for(int index = minIndex; index<maxIndex; index++)
	   {
		   if(index < condition.size())
		   {
			    condition.remove(index);
		   }
		   //condition.remove(index);
	   }
		
		return condition;
	}

    //indicate 2 strings - startFrom and endAt - which the parser will interpret as new flags and use to delimit the space to include in 
	//condition or dynamic -RETURN ArrayList<KeystrokeData> instead???
    /**
	* 
	* @param 
	* @return 
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
	* 
	* @param 
	* @return 
	**/
	public LinkedHashMap<String, ArrayList<KeystrokeData>> fromDynamic(BufferedReader bufferedReader, Gson gson) throws IOException 
	{
		//results
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
				} else if(i==validFlags.get("maxIdx").size()-1 && maxIndexCurr<serchString.length()) {
					for(int index=maxIndexCurr; index<serchString.length(); index++)
				    {
						dynamicKS.add(ksData.get(index));
				    }
			    } else if (i!=0 && minIndexCurr!=0){
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
	* 
	* @param 
	* @return 
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
	* 
	* @param 
	* @return 
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
	* 
	* @param 
	* @return 
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