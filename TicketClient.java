/**
 * File:TicketClient.java
 * Programmer: Ryan Admire
 * Description: One half of a client/server program that I wrote
 *              in Spring 2016. The idea is the client requests ticket data from
 *              the server and displays that data back to the user.
 */

import java.io.*;  //for input and output
import java.net.*; //for cookies, sockets, etc
import java.util.ArrayList;

public class Client {
	public static void main(String[] args) throws Exception {
		
		//inital setup of boolean and file writing variables
		boolean log = false;
		boolean screen = false;
		File logFile = new File("Client.log");      //create a new log file
		FileWriter logFW = new FileWriter(logFile); //create a FileWriter to write to the log

		//check if there was an argument passed in
		if(args.length > 0){
			if(args[0].equals("-v")){  //if it is lowercase we will need a log file
				log = true;
				screen = false;
			}
			else if(args[0].equals("-V")){  //if it is uppercase we will need a log file and screen output
				log = true;
				screen = true;
			}		
		}

		//block of code to set up the socket and the data input/output streams

		Socket client_sock = new Socket ("hoare.cs.umsl.edu", 13039); //set up the client socket the socket we were given is 13039
		boolean connected = true;                                     //value will be true while the user wants to keep the connection open
		DataOutputStream to = new DataOutputStream(client_sock.getOutputStream()); //set up the data that will be sent to the server
		BufferedReader fromServer = new BufferedReader (new InputStreamReader(client_sock.getInputStream())); //set up the reader from the server
		BufferedReader fromKeybrd = new BufferedReader (new InputStreamReader(System.in));                    //set up stream to read user input
		String inline, recieved;
		
		//what to do after initial set up
		try{
			while (connected){

				//Pseudo menu system to inform the user of their chocies
				System.out.print("Enter a value 1-4 to query the concert database:");
				System.out.print("1 - All concerts by a particular band. Sorted by day of the week.");
				System.out.print("2 - All concerts on a day of the week, sorted by venue.");
				System.out.print("3 - A schedule of concerts spending as much money as possible.");
				System.out.print("4 - Quit the program.");
			
				//if the user wishes write all this to the log file.
				if (log){

					logFW.write("Enter a value 1-4 to query the concert database: \n");
					logFW.write("1 - All concerts by a particular band. Sorted by day of the week. \n");
					logFW.write("2 - All concerts on a day of the week, sorted by venue. \n");
					logFW.write("3 - A schedule of concerts spending as much money as possible. \n");
					logFW.write("4 - Quit the program. \n");
				}

				inline = fromKeybrd.readLine(); //read a line in
				boolean sending = true;         //we are about to be sending a value
				boolean reading = true;         //while we are reading the results in
				recieved = null;                //reset recieved
				String s_res = "";              //string of results
				
				ArrayList<String> results = new ArrayList<String>(); //the array of results
				
				if(inline.equals("4")){
					connected = false;  //this is the option to quit
					if(log){
						logFW.write("User chose to close connection. \n");
					}
					else if(log && screen){
						logFW.write("User chose to close connection. \n");
						System.out.print("User chose to lose connection. \n");
					}
					while(sending){
						to.writeBytes (inline + "\n");    //send the message to the server
						recieved = fromServer.readLine ();//check for response
						if(recieved != null){
							//if the response is what we want stop sending
							sending = false;
						}
					}
				}
				
				if (!connected) break;
				
				//send our initial selection over
				while(sending){
					to.writeBytes(inline + "\n");     //send the message to the server
					recieved = fromServer.readLine ();
					if(recieved != null){
						sending = false;
						System.out.print(recieved + "\n");
						
						if (log){
							logFW.write(recieved + "\n");
						}
						
					}
				}
				
				sending = true; //reset sending to true
			
				if (log && screen){
					//inline should only be one of 4 values. Anything beyond those will be ignored.
					switch(inline){
						case "1": 
							inline = fromKeybrd.readLine(); //read a line in
	
							//while we wait for a response from the server
							while(sending){
								logFW.write("Sending value to server \n");
								System.out.print("Sending value to server \n");
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
									logFW.write("Response recieved. Halt Sending \n");
									System.out.print("Response recieved. Halt Sending \n");
								}
							}
							
							//while we read values in from the server
							while(reading){
								logFW.write("Reading values from server. \n");
								System.out.print("Reeading values from server\n");
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;              //stop reading
									logFW.write("All values read\n");
									System.out.print("All values read\n");
								}
								else{
									results.add(recieved);  //add to results array
								}	
							}
							
							logFW.write("\n");
							System.out.print("\n");
							
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								logFW.write(results.get(i) + "\n");
								System.out.print(results.get(i) + "\n");
							}
							logFW.write("\n\n");
							System.out.print("\n\n");
							break;
						case "2": 
						inline = fromKeybrd.readLine(); //read a line in

						//while we wait for a response from the server
						while(sending){
							logFW.write("Sending value to server \n");
							System.out.print("Sending value to server \n");
							to.writeBytes (inline + "\n");    //send the message to the server
							recieved = fromServer.readLine ();//check for response
							if(recieved != null){
								//if the response is what we want stop sending
								sending = false;
								logFW.write("Response recieved. Halt Sending \n");
								System.out.print("Response recieved. Halt Sending \n");
							}
						}
						
						//while we read values in from the server
						while(reading){
							logFW.write("Reading values from server. \n");
							System.out.print("Reeading values from server \n");
							recieved = fromServer.readLine();  //read a line
							if(recieved.contains("END")){
								reading = false;              //stop reading
								logFW.write("All values read \n");
								System.out.print("All values read \n");
							}
							else{
								results.add(recieved);  //add to results array
							}	
						}
						
						logFW.write("\n");
						System.out.print("\n");
						
						//loop through the results and print
						for(int i = 0; i < results.size(); i++){
							logFW.write(results.get(i) + "\n");
							System.out.print(results.get(i) + "\n");
						}
						logFW.write("\n\n");
						System.out.print("\n\n");
						break;
						case "3": 
						inline = fromKeybrd.readLine(); //read a line in

						//while we wait for a response from the server
						while(sending){
							logFW.write("Sending value to server \n");
							System.out.print("Sending value to server \n");
							to.writeBytes (inline + "\n");    //send the message to the server
							recieved = fromServer.readLine ();//check for response
							if(recieved != null){
								//if the response is what we want stop sending
								sending = false;
								logFW.write("Response recieved. Halt Sending \n");
								System.out.print("Response recieved. Halt Sending \n");
							}
						}
						
						//while we read values in from the server
						while(reading){
							logFW.write("Reading values from server. \n");
							System.out.print("Reeading values from server \n");
							recieved = fromServer.readLine();  //read a line
							if(recieved.contains("END")){
								reading = false;              //stop reading
								logFW.write("All values read \n");
								System.out.print("All values read \n");
							}
							else{
								results.add(recieved);  //add to results array
							}	
						}
						
						logFW.write("\n");
						System.out.print("\n");
						//loop through the results and print
						for(int i = 0; i < results.size(); i++){
							logFW.write(results.get(i) + "\n");
							System.out.print(results.get(i) + "\n");
						}
						logFW.write("\n\n");
						System.out.print("\n\n");
						break;
						default: break;
					}
				}
				else if (log && !screen){
					//inline should only be one of 4 values. Anything beyond those will be ignored.
					switch(inline){
						case "1": 
							inline = fromKeybrd.readLine(); //read a line in
	
							//while we wait for a response from the server
							while(sending){
								logFW.write("Sending value to server \n");
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
									logFW.write("Response recieved. Halt Sending. \n");
								}
							}
							
							while(reading){
								logFW.write("Reading values from server. \n");
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
									logFW.write("All values read. \n");
								}
								else{
									results.add(recieved);
								}	
							}
							
							logFW.write("\n");
							
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								logFW.write(results.get(i) + "\n");
							}
							
							logFW.write("\n\n");
							break;
						case "2": 
							inline = fromKeybrd.readLine(); //read a line in
							logFW.write(inline);
	
							//while we wait for a response from the server
							while(sending){
								logFW.write("Sending value to server");
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
									logFW.write("Response recieved. Halt Sending. \n");
								}
							}
							
							while(reading){
								logFW.write("Reading values from server.");
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
									logFW.write("All values read \n");
								}
								else{
									results.add(recieved);
								}	
							}
							
							logFW.write("\n");
							
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								logFW.write(results.get(i) + "\n");
							}

							logFW.write("\n\n");							
							break;
						case "3":
							inline = fromKeybrd.readLine(); //read a line in
							logFW.write(inline);
	
							//while we wait for a response from the server
							while(sending){
								logFW.write("Sending value to server");
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
									logFW.write("Response recieved. Halt Sending \n");
								}
							}
							
							while(reading){
								logFW.write("Reading values from server.");
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
									logFW.write("All values read. \n");
								}
								else{
									results.add(recieved);
								}	
							}
							
							logFW.write("\n");
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								logFW.write(results.get(i) + "\n");
							}
							
							logFW.write("\n\n");
							break;
						default: break;
					}
				}
				else{
					//inline should only be one of 4 values. Anything beyond those will be ignored.
					switch(inline){
						case "1":
							inline = fromKeybrd.readLine(); //read a line in
	
							//while we wait for a response from the server
							while(sending){
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
								}
							}
								
							while(reading){
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
								}
								else{
									results.add(recieved);
								}
							}
							
							s_res += "\n\n";
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								s_res += results.get(i) + "\n";
							}
							
							System.out.print(s_res  + "\n\n");
							
							break;
						case "2": 
							inline = fromKeybrd.readLine(); //read a line in
	
							//while we wait for a response from the server
							while(sending){
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
								}
							}
							
							while(reading){
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
								}
								else{
									results.add(recieved);
								}
							}
							
							s_res += "\n\n";
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								s_res += results.get(i) + "\n";
							}
							
							System.out.print(s_res  + "\n\n");
							
							break;
						case "3": 
							inline = fromKeybrd.readLine(); //read a line in
	
							//while we wait for a response from the server
							while(sending){
								to.writeBytes (inline + "\n");    //send the message to the server
								recieved = fromServer.readLine ();//check for response
								if(recieved != null){
									//if the response is what we want stop sending
									sending = false;
								}
							}
							
							while(reading){
								recieved = fromServer.readLine();  //read a line
								if(recieved.contains("END")){
									reading = false;
								}
								else{
									results.add(recieved);
								}	
							}
							
							s_res += "\n\n";
							//loop through the results and print
							for(int i = 0; i < results.size(); i++){
								s_res += results.get(i) + "\n";
							}
							
							System.out.print(s_res + "\n\n");
							
							break;
						default: break;
					}
				}
			}
		}
		finally{
			client_sock.close();            //close the connection
			logFW.flush();                    //flush the writer
			logFW.close();                    //close the writer
		}

	}

}
