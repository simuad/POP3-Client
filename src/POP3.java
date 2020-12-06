import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class POP3 {
    private SSLSocket sslSocket;
    private BufferedReader in;
    private PrintWriter out;

    public void connect(String host, int port) throws IOException {
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sslSocket = (SSLSocket) sslsocketfactory.createSocket(host, port);
        in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        out = new PrintWriter(sslSocket.getOutputStream(), true);

        System.out.println("Connected to host");
    }

    public void login(String username, String password) throws IOException {
        sendCommand("USER " + username);
        sendCommand("PASS " + password);

        if(in.readLine().startsWith("+OK")){
            System.out.println("Login successful");
        } else {
            System.out.println("Login unsuccessful");
        }
    }

    public void sendSTAT() throws IOException {
        System.out.println(sendCommand("STAT"));
    }

    public void sendLIST() throws IOException {
        System.out.println("Number of messages: " + getNumberOfMessages());

        out.write("LIST" + "\n");
        out.flush();
        String response;

        while (!(response = in.readLine()).equals(".")) {
            System.out.println(response);
        }
    }

    public void sendLIST(int msgNumber) throws IOException {
        System.out.println(sendCommand("LIST " + msgNumber));
    }

    public void sendRETR(int msgNumber) throws IOException {
        String message;

        sendCommand("RETR " + msgNumber);
        while (!(message = in.readLine()).equals(".")) {
            System.out.println(message);
        }
    }

    public void sendDELE(int msgNumber) throws IOException {
        System.out.println(sendCommand("DELE " + msgNumber));
    }

    public void sendNOOP() throws IOException {
        System.out.println(sendCommand("NOOP"));
    }

    public void sendRSET() throws IOException {
        System.out.println(sendCommand("RSET"));
    }

    public void sendQUIT() throws IOException {
        System.out.println(sendCommand("QUIT"));
    }

    public void showEmails () throws IOException {
        System.out.println("Number of messages: " + getNumberOfMessages());

        String subject;

        for(int i = 1; i < getNumberOfMessages(); i++) {
            sendCommand("TOP " + i + " 0");
            while (!(subject = in.readLine()).equals(".")) {
                if (subject.startsWith("Subject")) {
                    System.out.println(i + " " + subject.substring(8));
                }
            }
        }
    }

    public void disconnect() throws IOException {
        if (!isConnected())
            throw new IllegalStateException("Not connected to host");
        sslSocket.close();
        sslSocket = null;
        in = null;
        out = null;
        System.out.println("Disconnected from host");
    }

    public boolean isConnected() {
        return sslSocket != null && sslSocket.isConnected();
    }

    private String sendCommand(String command) throws IOException {
        out.write(command + "\n");
        out.flush();
        return readResponseLine();
    }

    private String readResponseLine() throws IOException {
        String response = in.readLine();

        if (response.startsWith("-ERR"))
            throw new RuntimeException("Server has returned an error: " + response.replaceFirst("-ERR ", ""));
        return response;
    }

    private int getNumberOfMessages() throws IOException {
        String response = sendCommand("STAT");
        String[] contents = response.split(" ");
        int number = parseInt(contents[1]);

        return number;
    }
}
