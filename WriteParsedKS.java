import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.*;

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
    public void createParsedKS(String originalPath, String destinationPath) throws FileNotFoundException
	{
		try 
		{
			GsonBuilder gsonBuilder = new GsonBuilder();  
            gsonBuilder.setLenient();  
            Gson gson = gsonBuilder.create();
	        //Gson gson = new Gson();
			
			for(int i=0; i< filesForFolder(originalPath).size(); i++)
		    {
				String ppsNumber = "s" + i;
				String originalName = filesForFolder(originalPath).get(i); 
				String ppsFolder = makePPSFolder(ppsNumber, destinationPath, rks.getFiles(originalName), gson);
				String fileName = ppsFolder + "-";
				
				//copyFile(originalName, fileName + originalName.replace("/", "").replace(originalPath, ""));
				
				LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition = sp.fromCondition(rks.getFiles(originalName), gson);
				if(fromCondition.keySet().size() > 0) 
				{
					copyFile(originalName, fileName + originalName.replace("/", "").replace(originalPath, ""));
					
					try (Writer writer = new FileWriter(ppsFolder+"-static.json")) {
						gson.toJson(fromCondition, writer);
						writer.close();
					}
					
					//fileName += originalName.replace("/", "").replace(originalPath, "").replace(".json", "") + ".json";
					
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