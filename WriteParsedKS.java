import java.io.*;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;

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
	        Gson gson = new Gson();
			
			for(int i=0; i< filesForFolder(originalPath).size(); i++)
		    {
				String ppsNumber = "s" + i;
				String originalName = filesForFolder(originalPath).get(i); 
				String ppsFolder = makePPSFolder(ppsNumber, destinationPath, rks.getFiles(originalName), gson);
				String fileName = ppsFolder;
				
				LinkedHashMap<String, ArrayList<KeystrokeData>> fromCondition = sp.fromCondition(rks.getFiles(originalName), gson);
				
				if(fromCondition.keySet().size() > 0) 
				{
					gson.toJson(fromCondition, new FileWriter(ppsFolder+"-static.json"));
					fileName += "-original.json";
				} else {
					fileName += "-dynamic.json";
				}
				
				gson.toJson(rks.accessKsLabels(rks.getFiles(originalName), gson), new FileWriter(fileName));	
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
}