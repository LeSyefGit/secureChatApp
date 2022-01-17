import java.net.*;

public class ServerChat {

    public static void main(String[] args) throws Exception {
        ServerSocket serversocket = new ServerSocket(1234);
        while (true) {
            Socket socket = serversocket.accept();
            new ServiceChat(socket);
        }
        // serversocket.close();
    }
}