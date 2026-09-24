package ma.youcode.lineperm;

import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.db.DBConnection;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setErr(new PrintStream(System.err, true, "UTF-8"));

       try {
            ConsoleApp app = new ConsoleApp();
            app.demarrer();
        } finally {
            DBConnection.getInstance().close();
        }
    }
}