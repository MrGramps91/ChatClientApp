package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerThread implements Runnable{
	
	private Socket socket;
	private String name;
	private BufferedReader serverIn;
	private BufferedReader userIn;
	private PrintWriter out;
	
	public ServerThread(Socket socket){
		this.socket = socket;
		this.name = name;
		
	}
	
	@Override
	public void run(){
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			userIn = new BufferedReader(new InputStreamReader(System.in));
			
			while(!socket.isClosed()){
				if(serverIn.ready()){
					String input = serverIn.readLine();
					if(input != null){
						System.out.println(input);
					}
				}
				if(userIn.ready()){
					out.println(name+" > "+userIn.readLine());
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Socket socket = null;
		System.out.println("Please input username");
		Scanner scan = new Scanner(System.in);
		String name = scan.nextLine();
		scan.close();
		int portNumber = 1707;
		try{
			socket = new Socket("localhost",portNumber);
			Thread.sleep(1000);
			Thread server = new Thread(new ServerThread(socket));
			server.start();
		}
		catch(IOException e){
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
		}
		catch(InterruptedException e){
			System.err.println("Fatal Connection error!");
			e.printStackTrace();
		}
	}

}
