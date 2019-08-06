import java.io.*;
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

				BufferedReader bufferedReader = rks.getFiles(originalName);;
				
				gson.toJson(sp.fromCondition(bufferedReader, gson), new FileWriter(makePPSFolder(ppsNumber, destinationPath)+"-static.json"));
				//gson.toJson(rks.accessKsLabels(bufferedReader, gson), new FileWriter(originalName));
				
				//gson.toJson(sp.fromCondition(bufferedReader, gson), new FileWriter(ppsFolder+"-static.json"));
				//gson.toJson(rks.accessKsLabels(bufferedReader, gson), new FileWriter(ppsFolder + "-" + originalName));
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
	public String makePPSFolder(String ppsNumber, String destinationPath) 
	{
		File dir = new File(destinationPath + "//" + ppsNumber);
		dir.mkdir(); 
		
		String newFilename= dir.getAbsolutePath() + "//" + ppsNumber;
		return newFilename;
	}
}