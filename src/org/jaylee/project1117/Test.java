package org.jaylee.project1117;

public class Test {
	
	final static String Usage = "Usage : java test --id [Client_ID]"
			+ " --secret [Client_Secret] --file [Url File Path]";

	public static void main(String[] args) {
		
		if( args.length != 6 ) {
			System.out.println(Usage);
			return;
		}
		Settings setting = new Settings(args);
		
		AsyncNaverShortenURL asyncTest = new AsyncNaverShortenURL(setting);
		SimpleNaverShortenURL simpleTest = new SimpleNaverShortenURL(setting);
		long simpleStart = System.currentTimeMillis();
		simpleTest.run();
		long simpleEnd = System.currentTimeMillis();
		System.out.println("Simple Test Time : " + (simpleEnd - simpleStart)/1000.0);
		
		long asyncStart = System.currentTimeMillis();
		try {
			asyncTest.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long asyncEnd = System.currentTimeMillis();
		System.out.println("Async Test Time : " + (asyncEnd - asyncStart)/1000.0 );
		
	}
}
