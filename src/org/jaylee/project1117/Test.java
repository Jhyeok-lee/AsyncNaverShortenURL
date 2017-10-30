package org.jaylee.project1117;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AsyncNaverShortenURL test = new AsyncNaverShortenURL();
		
		/*
		try {
			PrintWriter pw = new PrintWriter("c:/input.txt");
	        for(int i=1; i<=100; i++) {
	            String data = "https://www.naver.com/" + i;
	            pw.println(data);
	        }
	        pw.close();
		} catch(IOException e) {
			System.out.println("Error file");
		}
		*/
		
		ArrayList<String> input = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));
	        while(true) {
	            String line = br.readLine();
	            if (line==null) break;
	            input.add(line);
	        }
	        br.close();
		} catch(IOException e) {
			System.out.println("Error Read");
		}
		
		SimpleNaverShortenURL simpleTest = new SimpleNaverShortenURL();
		long start1 = System.currentTimeMillis();
		simpleTest.simpleTest(input);
		long end1 = System.currentTimeMillis();
		System.out.println("Simple Test Time : " + (end1-start1)/1000.0);
		
		long start = System.currentTimeMillis();
		//input.add("https://www.naver.com");
		test.setClientID("");
		test.setClientSecret("");
		test.setInput(input);
		test.init();
		try {
			test.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<String,String> output = new HashMap<String,String>();
		output.putAll(test.getOutput());
		long end = System.currentTimeMillis();
		for(int i=0; i<input.size(); i++) {
			if( output.containsKey(input.get(i)) )
				System.out.println(input.get(i) + " : " + output.get(input.get(i)));
		}

		System.out.println("Async Test Time : " + (end - start)/1000.0 );
	}
}
