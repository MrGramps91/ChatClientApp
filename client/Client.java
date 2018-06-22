package client;

import java.io.*;
import java.net.*;

public class Client {
	
	public static void main(String[] args){
		
		Socket socket = null;
		int portNumber = 1707;
		try{
			socket = new Socket("localhost",portNumber);
		}
		catch(IOException e){
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
		}
	}

}
