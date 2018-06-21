package chatClient.Interface;

import java.net.*;
import java.io.*;
import java.util.*;

public class ChatHandler extends Thread {
	
	protected Socket s;
	protected DataInputStream is;
	protected DataOutputStream os;
	
	public ChatHandler(Socket s) throws IOException {
		
		this.s = s;
		is = new DataInputStream (new BufferedInputStream(s.getInputStream()));
		os = new DataOutputStream (new BufferedOutputStream(s.getOutputStream()));
	}
	
	protected static Vector handlers = new Vector();
	public void run(){
		try{
			handlers.addElement(this);
			while(true){
				String msg = is.readUTF();
				broadcast(msg);
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}finally{
			handlers.removeElement(this);
			try{
				s.close();
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		
	}
	
	protected static void broadcast(String message){
		synchronized(handlers){
			Enumeration e = handlers.elements();
			while (e.hasMoreElements()){
				ChatHandler c = (ChatHandler) e.nextElement();
				try{
					synchronized(c.os){
						c.os.writeUTF(message);
					}
					c.os.flush();
				}catch (IOException ex){
					c.interrupt();
				}
			}
		}
	}

}
