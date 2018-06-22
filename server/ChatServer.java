package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ChatServer {
	
	static ServerSocket serverSocket = null;
	private static int portNumber = 1707;
	static ArrayList<ClientThread> clients;
	
	public ChatServer(){
		
	}
	
	public static void main(String[] args){		
		try{
			 serverSocket = new ServerSocket(portNumber);
			 acceptClients();
		}
		catch(IOException e){
			System.err.println("Could not listen on port: "+portNumber);
			System.exit(1);
		}
	}
	
	public static void acceptClients(){
		clients = new ArrayList<ClientThread>();
		while(true){
			try{
				Socket socket = serverSocket.accept();
				ClientThread client = new ClientThread(socket);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
			}
			catch(IOException e){
				System.out.println("Accept failed on: "+portNumber);
			}
		}
	}

}
