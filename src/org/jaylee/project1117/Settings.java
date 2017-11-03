package org.jaylee.project1117;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Settings {
	protected String ClientID;
	protected String ClientSecret;
	protected ArrayList<String> input;
	
	public Settings(String[] args) {
		this.ClientID = args[1];
		this.ClientSecret = args[3];
		String filePath = args[5];
		
		input = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
	        while(true) {
	            String line = br.readLine();
	            if (line==null) break;
	            input.add(line);
	        }
	        br.close();
		} catch(IOException e) {
			System.out.println("Error : No file.");
		}
	}
}