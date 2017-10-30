package org.jaylee.project1117;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SimpleNaverShortenURL {

	public HashMap<String,String> simpleTest(ArrayList<String> input) {
        String clientId = "";
        String clientSecret = "";
        String apiURL = "https://openapi.naver.com/v1/util/shorturl";
        HashMap<String,String> output = new HashMap<String,String>();
        try {
        	for(int i=0; i<input.size(); i++) {
        		URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "url=" + input.get(i);
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                	JSONParser jsonParser = new JSONParser();
                	JSONObject jsonObj = (JSONObject)jsonParser.parse(inputLine);
                	JSONObject result = (JSONObject)jsonObj.get("result");
                	String shortenURL = result.get("url").toString();
                	String originURL = result.get("orgUrl").toString();
                	output.put(originURL, shortenURL);
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
        	} 
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return output;
    }
}
