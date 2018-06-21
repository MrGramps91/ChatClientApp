package chatClient.Interface;

/*This class implements the chat client
 * by setting up a basic UI, handling user interaction,
 * and receiving messages from the server*/

import java.net.*;
import java.io.*;
import java.awt.*;

public class ChatInterface extends Frame implements Runnable{
	private static int port = 1707;
	private static String host = "localhost";
	protected DataInputStream is;
	protected DataOutputStream os;	
	protected TextArea output;
	protected TextField input;
	protected Thread listener;
	
	public ChatInterface (String title, InputStream is, OutputStream os){
		super(title);
		this.is = new DataInputStream (new BufferedInputStream(is));
		this.os = new DataOutputStream(new BufferedOutputStream(os));
		setLayout (new BorderLayout());
		add("Center", output = new TextArea());
		output.setEditable(false);
		add("South", input = new TextField());
		pack();
		setVisible(true);
		input.requestFocus();
		listener = new Thread(this);
		listener.start();
	}
	
	@Override
	public void run() {
		try{
			while(true){
				String line = is.readUTF();
				output.append(line + "\n");
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}finally{
			listener = null;
			input.setVisible(false);
			validate();
			try{
				os.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	public boolean handleEvent(Event e){
		if((e.target == input) && (e.id == Event.ACTION_EVENT)){
			try{
				os.writeUTF ((String) e.arg);
				os.flush();
			}catch(IOException ex){
				ex.printStackTrace();
				listener.interrupt();;
			}
			input.setText("");
			return true;
		}else if((e.target == this) && (e.id == Event.WINDOW_DESTROY)){
			if(listener != null)
				listener.interrupt();
			setVisible(false);
			return true;
		}
		return super.handleEvent(e);
	}

	public static void main(String args[]) throws IOException{
		if(args.length != 2)
			throw new RuntimeException ("Syntax: ChatClient <host> <port>");
			Socket s = new Socket (args[0], Integer.parseInt(args[1]));
			new ChatInterface ("Chat " + args[0] + ":" + args[1],
				s.getInputStream(), s.getOutputStream());
		
	}
}
