import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;

public class UI{
    private POP3 pop3 = new POP3();
    BufferedReader bufRead = new BufferedReader(new InputStreamReader(System.in));
    private int choice = 1;
    private String host;
    private int port = 995;
    private String username = "";
    private String password = "";

    private int msgNumber;

    public void run() {
        while(choice != 0){
            try {
                loginMenu();
                System.out.print("\n> ");
                choice = parseInt(bufRead.readLine());

                switch (choice){
                    case 0:
                        break;
                    case 1:
                        connect();
                        break;
                    case 2:
                        pop3.disconnect();
                        break;
                    default:
                        System.out.println("Bad choice");
                }
            } catch (IOException e){
                System.out.println("Error occurred while reading input");
            } catch (IllegalStateException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void loginMenu(){
        System.out.println();
        System.out.println("POP3 Client:");
        System.out.println();
        System.out.println("[1] Connect to host");
        System.out.println("[2] Disconnect from host");
        System.out.println("[0] Exit program");
    }

    private void connect(){
        if(!(pop3.isConnected())){
            connectToHost();
        } else {
            loginToHost();
        }
    }

    private void connectToHost(){
        try {
            System.out.println();
            System.out.println("Enter host");
            System.out.print("\n> ");
            host = bufRead.readLine();
            pop3.connect(host, port);

            loginToHost();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void loginToHost(){
        try {
            pop3.login(username, password);
            runPOP3();
            choice = 1;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void runPOP3(){
        while(choice != 0){
            try {
                hostMenu();
                System.out.print("\n> ");
                choice = parseInt(bufRead.readLine());

                switch (choice){
                    case 0:
                        pop3.sendQUIT();
                        break;
                    case 1:
                        pop3.sendSTAT();
                        break;
                    case 2:
                        sendLIST();
                        break;
                    case 3:
                        sendAllLIST();
                        break;
                    case 4:
                        sendRETR();
                        break;
                    case 5:
                        sendDELE();
                        break;
                    case 6:
                        pop3.sendNOOP();
                        break;
                    case 7:
                        pop3.sendRSET();
                        break;
                    case 8:
                        pop3.showEmails();
                        break;
                    default:
                        System.out.println("Bad choice");
                }
            } catch (IOException e){
                System.out.println("Error occurred while reading input");
            } catch (IllegalStateException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void hostMenu(){
        System.out.println();
        System.out.println("Menu");
        System.out.println();
        System.out.println("[1] Send STAT");
        System.out.println("[2] Send LIST");
        System.out.println("[3] Show all LIST");
        System.out.println("[4] Send RETR");
        System.out.println("[5] Send DELE");
        System.out.println("[6] Send NOOP");
        System.out.println("[7] Show email");
        System.out.println("[8] List emails");
        System.out.println("[0] Sign out");
    }

    private void sendLIST(){
        try {
            System.out.println();
            System.out.println("Enter which message's size you'd like to see:");
            System.out.print("\n> ");
            msgNumber = parseInt(bufRead.readLine());
            pop3.sendLIST(msgNumber);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendAllLIST(){
        try {
            pop3.sendLIST();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendRETR(){
        try {
            System.out.println();
            System.out.println("Enter message you want to retrieve");
            System.out.print("\n> ");
            msgNumber = parseInt(bufRead.readLine());
            pop3.sendRETR(msgNumber);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendDELE(){
        try {
            System.out.println();
            System.out.println("Enter which message you'd like to delete:");
            System.out.print("\n> ");
            msgNumber = parseInt(bufRead.readLine());
            pop3.sendDELE(msgNumber);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
