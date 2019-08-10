import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;
import com.google.common.collect.*;

public class WriteParsedKS 
{
	ReadKSFile rks;
	SplitCondition sp;
	
	public WriteParsedKS ()
	{
		rks = new ReadKSFile();
		sp = new SplitCondition();
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	
	//public void generateDynamic (String filename, String startFrom, String endAt){}
	
	public void deleteFromFile(String filename,	String startFrom, String endAt) throws IOException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();  
        gsonBuilder.setLenient();  
        Gson gson = gsonBuilder.create();
		
		ArrayList<KeystrokeData> ksData = rks.getKsData(rks.getFiles(filename), gson);
		ArrayList<KeystrokeData> fixedData = sp.deleteFrom(ksData, startFrom, endAt);
		
		LinkedHashMap<String, ArrayList<KeystrokeData>> conditions = new LinkedHashMap<String, ArrayList<KeystrokeData>>();
		conditions.put("ksData", fixedData);
		writer(conditions, filename.replace(".json","-MOD.json"), gson);
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public void modifyParsedKS(String filename, String startFrom, String endAt, String toCondition) throws IOException
	{
		GsonBuilder gsonBuilder = new GsonBuilder();  
        gsonBuilder.setLenient();  
        Gson gson = gsonBuilder.create();
		
		LinkedHashMap<String, ArrayList<KeystrokeData>> conditions = sp.fromCondition(rks.getFiles(filename), gson);
		conditions.put(toCondition, sp.getCond(startFrom, endAt, rks.getFiles(filename)));
		
		writer(conditions, filename.replace("kspattern","-MOD.json"), gson);
	}
	
	
	/**
	* 
	* @param 
	* @return 
	**/
    public void createParsedKS(String originalPath, String destinationPath) throws FileNotFoundException
	{
		try 
		{
			GsonBuilder gsonBuilder = new GsonBuilder();  
            gsonBuilder.setLenient();  
            Gson gson = gsonBuilder.create();
			
			for(int i=0; i< filesForFolder(originalPath).size(); i++)
		    {
				String ppsNumber = "s" + i;
				String originalName = filesForFolder(originalPath).get(i); 
				String ppsFolder = makePPSFolder(ppsNumber, destinationPath, rks.getFiles(originalName), gson);
				String fileName = ppsFolder + "-";
				
				LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition = sp.fromCondition(rks.getFiles(originalName), gson);
				LinkedHashMap<String, ArrayList<KeystrokeData>> dynamicData = sp.fromDynamic(rks.getFiles(originalName), gson);
				if(fromCondition.keySet().size() > 0) 
				{
					copyFile(originalName, fileName + originalName.replace("/", "").replace(originalPath, ""));
					
					fileName += originalName.replace("/", "").replace(originalPath, "").replace(".json", "") + ".json"; //movedhere
				
					writer(fromCondition, ppsFolder+"-static.json", gson);
					writer(dynamicData, ppsFolder+"-dynamic.json", gson);
					
				} else {
					
					fileName += originalName.replace("/", "").replace(originalPath, "").replace(".json", "") + "-dynamic.json";
				    copyFile(originalName, fileName);
				}
				System.out.println("parsed file at: " + fileName);
		    }
		} 
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (IOException e){e.printStackTrace();}
		catch (Exception e){e.printStackTrace();}
		
	} 
	
   /**
	* 
	* @param 
	* @return 
	**/
	public void writer(Object obj, String filename, Gson gson) 
	{
		try (Writer writer = new FileWriter(filename)) {
			gson.toJson(obj, writer);
			writer.close();
		}
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (Exception e){e.printStackTrace();}
	}
	
	
	/**
	* 
	* @param 
	* @return 
	**/
	public ArrayList<String> filesForFolder(String originalPath)
	{
		ArrayList<String> filenames = new ArrayList<String>();
		
		File folder = new File(originalPath);
		File [] listOfFiles = folder.listFiles();
		
		for(File file: listOfFiles)
		{
			if(file.isFile())
			{
				filenames.add(originalPath + "//" + file.getName().toString()); 
			}
		}
		return filenames;
	}
	
	/**
	* 
	* @param 
	* @return 
	**/
	public String makePPSFolder(String ppsNumber, String destinationPath, BufferedReader bufferedReader, Gson gson) 
	{
		File dir = new File(destinationPath + "//" + ppsNumber + sp.getAnonCode(bufferedReader, gson));
		dir.mkdir(); 
		
		String newFilename= dir.getAbsolutePath() + "//" + ppsNumber;
		return newFilename;
	}
	
	
	/**
	* 
	* @param 
	* @return 
	**/
	 public void copyFile(String from, String to) throws IOException{
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }
	
}