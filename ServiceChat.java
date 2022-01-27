import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ServiceChat extends Thread {
    final static int NBMAXUSER = 3;
    static PrintStream[] outputs = new PrintStream[NBMAXUSER];

    static Map<String, String> users = new HashMap<String, String>();
    static int nbUsers = 0;

    BufferedReader input;
    PrintStream output ;
 
    Socket socket;
    private User user;
    boolean bool= true;

    ServiceChat(Socket socket) {
        this.socket = socket;
        start();
    }

    public synchronized void mainloop() {
        try {
            String str = "";
            
            while (bool) {
                str = input.readLine();
                switch (str){
                    case "\\exit":
                        exit();
                        break;
                    case "\\list":
                        listUsers();
                        break;
                    case "\\register":
                        register();
                        break;
                    case "\\login":
                        login();
                        break;
                    default:
                        for (int i = 0; i < nbUsers; i++){
                            outputs[i].println(str);
                        }
                        break;
                }
                //System.out.println(str);
            }
        }catch (Exception e) {
            System.out.println("error with the reader" + e.getMessage());
        }
    }
    void printAll(String type, String str){
        String msg="";
        switch(type){
            case "connect":
                msg = str +"joined the Chat";
                break;
            case "disconnect":
                msg = str +"left the Chat";
                break;
        }
        for (int i = 0; i < nbUsers; i++){
            output.println("[ INFO ] " + str);
        }
    }
    void print(String str){
        output.println("[   ] " + str);
    }

    void exit() throws IOException{
        this.socket.close();
        this.bool= false;
    }

    void listUsers(){
        output.println(this.users);
    }

    void login() throws IOException{
        print(" User enter your username and password ");
        print("username:");
        String username = input.readLine();
        if(users.containsKey(username)){
            print("password:");
            String password = input.readLine();
            if(users.get(username).equals(password)){
                print("login successful");
                outputs[nbUsers] = new PrintStream(this.socket.getOutputStream());
            }
            else{
                print("login failed");
            }

        }
        output.println("password:");
    }
    void register() throws IOException{
        print("enter the username: ");
        String username = this.input.readLine();
        print("enter the password: ");
        String password = this.input.readLine();
        users.put(username, password);
        login();
    }

    public void run() {
        try {

            input = new BufferedReader( new InputStreamReader(this.socket.getInputStream()) ); // for the input of the user
            output = new PrintStream(this.socket.getOutputStream()); // for the output of the user
        } catch (Exception e) {
            System.out.println("error with the socket" + e.getMessage());
        }
        nbUsers++;

        mainloop();

    }

}
