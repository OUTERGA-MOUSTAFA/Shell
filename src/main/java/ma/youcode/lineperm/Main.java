package ma.youcode.lineperm;

import ma.youcode.lineperm.ui.ConsoleApp;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));

        ConsoleApp app = new ConsoleApp();
        app.demarrer();
    }
}