package pucflix;

import pucflix.view.*;

public class Main {
    public static void main(String[] args) {
        try
        {
            Prompt prompt = new Prompt();
            prompt.start();
            prompt.close();
        } catch(Exception ex) { System.err.println(ex.getMessage()); }
    }
}
