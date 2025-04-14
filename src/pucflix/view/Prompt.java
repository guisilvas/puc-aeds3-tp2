package pucflix.view;

import java.util.Scanner;

import pucflix.model.*;

/**
 * Exibe a interface
 */
public class Prompt 
{
    Scanner scanner;

    private boolean isRunning;

    private View[] views;

    private int currentBranch;
    private int depth;

    public Prompt() throws Exception
    {
        ShowFile sFile = new ShowFile();
        EpisodeFile eFile = new EpisodeFile();
    
        scanner = new Scanner(System.in);
        views = new View[] {
            new ShowView(this, sFile, eFile),
            new EpisodeView(this, eFile, sFile) 
        };
        currentBranch = -1;
        depth = 0; 
    }

    /**
     * Inicia o ciclo de vida do programa
     */
    public void start()
    {   
        isRunning = true;

        while(isRunning)
        {
            show();
            eval();
        }
    }   
    
    private void show()
    {
        printHeader();

        if(currentBranch >= 0)
        {
            try
            {
                System.out.println(views[currentBranch].getPrompt(0));
                System.out.println("0) Voltar");
                return;
            }
            catch(Exception ex)
            {
                System.out.println("Ocorreu um erro");
                ex.printStackTrace();
                currentBranch = -1;
            }
        }

        for(int i = 0; i < views.length; i++) 
            System.out.println((i + 1) + ") " + views[i].getName());

        System.out.println("0) Sair");
    }

    private void eval()
    {
        try 
        { 
            int opt = Integer.parseInt(scanner.nextLine()); 
            if (opt < 0 || (currentBranch < 0 && opt > views.length))
                throw new Exception("Opção inválida");

            if(opt == 0) 
            {
                if(currentBranch < 0)
                    isRunning = false;
                else
                {
                    views[currentBranch].exit();
                    currentBranch = -1;
                }
                return;
            }

            if(currentBranch < 0) 
            {
                currentBranch = opt - 1;
                return;
            }
        
            views[currentBranch].eval(opt, 0);
        }
        catch(Exception ex) 
        { 
            System.err.println("Entrada inválida");
            ex.printStackTrace();
        }
    }

    private void printHeader()
    {
        System.out.println("PUCFlix 1.0");
        System.out.println("-----------");
        System.out.print("> Início");
        
        if(currentBranch >= 0)
            System.out.print(" > " + views[currentBranch].getName());

        System.out.println("\n");
    }

    public String askForInput(String message)
    {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void close()
    {
        scanner.close();
    }
}
