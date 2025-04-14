package pucflix.view;

public abstract class View 
{
    protected Prompt prompt;

    public View(Prompt prompt)
    {
        this.prompt = prompt;
    }

    public abstract String getName();
    public abstract String getPrompt(int depth) throws Exception;
    public abstract void eval(int input, int depth) throws Exception;

    public void exit() { }
}
