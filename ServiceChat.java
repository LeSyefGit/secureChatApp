import java.io.*;
import java.net.*;

public class ServiceChat extends Thread {
    final static int NBMAXUSER = 3;
    static PrintStream[] outputs = new PrintStream[NBMAXUSER];
    static int nbUsers = 0;
    InputStreamReader inputStream;
    String str = "";
    Socket socket;

    ServiceChat(Socket socket) {
        this.socket = socket;
        start();
    }

    public void mainloop() {

        try {
            BufferedReader input = new BufferedReader(inputStream);
            while (this.str != "exit") {
                this.str = input.readLine();
                System.out.println(str);

                for (int i = 0; i < nbUsers; i++) {
                    outputs[i].println(str);
                }
            }
        } catch (Exception e) {
            System.out.println("error with the reader" + e.getMessage());
        }
    }

    public void run() {
        try {
            inputStream = new InputStreamReader(this.socket.getInputStream());
            outputs[nbUsers] = new PrintStream(this.socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("error with the socket" + e.getMessage());
        }
        nbUsers++;

        mainloop();

    }

}
