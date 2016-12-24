package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection connection = null;
				URL url;
				try {
					url = new URL(address);
					connection =(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in) );
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null)
						response.append(line);
					if (listener != null) {
						//回调onfinish方法
						listener.onFinish(response.toString());
					}
					
				} catch (Exception e) {
					if(listener != null)
						listener.onError(e);
					//回调error方法
				}finally {
					if(connection != null)
						connection.disconnect();
				}
				
				
			}
		}).start();
	}
	
	

}
