package pucflix.view;

import pucflix.entity.Actors;
import pucflix.model.ActorsFile;

public class ActorsView extends View {
    private ActorsFile aFile;
    private int actorId;
    private String name;

    public ActorsView(Prompt prompt, ActorsFile aFile) throws Exception {
        super(prompt);
        this.aFile = aFile;
        this.actorId = -1;
    }

    @Override
    public String getName() {
        return "Atores";
    }

    @Override
    public String getPrompt(int depth) throws Exception {
        if (actorId == -1) {
            String search = prompt.askForInput("Qual ator/atriz você deseja pesquisar: ");
            Actor[] actors = aFile.read(search);
            int n = 0;

            while (actors == null) {
                search = prompt.askForInput("Nenhum ator/atriz encontrado, tente novamente: ");
                actors = aFile.read(search);
            }

            if (actors.length > 1) {
                for (int i = 0; i < actors.length; i++)
                    System.out.println((i + 1) + ") " + actors[i].getName());

                boolean valid = false;
                while (!valid) {
                    try {
                        n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                        if(n < 1 || n > actors.length)
                            throw new Exception();

                        valid = true;
                        --n;
                    } catch (Exception e) {
                        System.out.println("Insira um ator/atriz válido");
                    }
                }

                actorId = actors[n].getID();
                System.out.println("Ator/Atriz escolhido(a): " + actors[n].getName());
            }

            return
                "1) Incluir\n" +
                "2) Buscar\n" +
                "3) Alterar\n" +
                "4) Excluir";
        }
    }

    @Override
    public void eval(int input, int depth) throws Exception {


    }

}
