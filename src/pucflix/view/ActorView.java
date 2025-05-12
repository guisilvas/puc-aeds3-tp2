package pucflix.view;

import pucflix.entity.Actor;
import pucflix.entity.Show;
import pucflix.model.ActorFile;
import pucflix.model.ShowFile;

public class ActorView extends View {
    private ActorFile aFile;
    private ShowFile sFile;
    private int showID;
    private String name;

    public ActorView(Prompt prompt, ActorFile aFile, ShowFile sFile) throws Exception {
        super(prompt);
        this.aFile = aFile;
        this.sFile = sFile;
        this.showID = -1;
    }

    @Override
    public String getName() {
        return "Atores";
    }

    @Override
    public String getPrompt(int depth) throws Exception {
        if (showID == -1) {
            String search = prompt.askForInput("Qual filme você deseja gerenciar: ");
            Show[] shows = sFile.read(search);
            int n = 0;

            while (shows == null) {
                search = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                shows = sFile.read(search);
            }

            if (shows.length > 1) {
                for (int i = 0; i < shows.length; i++)
                    System.out.println((i + 1) + ") " + shows[i].getName());

                boolean valid = false;
                while (!valid) {
                    try {
                        n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                        if(n < 1 || n > shows.length)
                            throw new Exception();

                        valid = true;
                        --n;
                    } catch (Exception e) {
                        System.out.println("Insira um número válido");
                    }
                }

                showID = shows[n].getID();
                System.out.println("Série escolhida: " + shows[n].getName());
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
        switch (input) {

            case 1: {
                String name = prompt.askForInput("Nome: ");
                Actor actor = new Actor(name);
                aFile.create(actor);
                System.out.println("Operação finalizada com sucesso");
                break;
            }
                
            case 2: {
                String search = prompt.askForInput("Busca: ");
                Actor[] actors = aFile.read(search);

                if (actors == null || actors.length == 0)
                    System.out.println("Nenhum ator/atriz encontrada");

                if (actors.length == 1) {
                    System.out.println(actors[0].toString());
                    break;
                }

                String result = "";
                for (int i = 0; i < actors.length; i++)
                    result += (i + 1) + ") " + actors[i].getName() + "\n"; 

                System.out.println(result);

                int n = 0;
                boolean valid = false;
                while (!valid) {
                    try {
                        n = Integer.parseInt(prompt.askForInput("Número: "));
                        if (n < 1 || n > actors.length) 
                            throw new Exception();
                        valid = true;
                        n--;
                    } catch (Exception e) {
                        System.out.println("Insira um número válido");
                    }
                }

                System.out.println(actors[n].toString());

                break;
            }

            case 3: {
                String search = prompt.askForInput("Nome: ");
                Actor[] actors = aFile.read(search);
                int n = 0;

                while (actors == null) {
                    search = prompt.askForInput("Nenhum ator/atriz encontrado(a), tente novamente: "); 
                    actors = aFile.read(search);
                }

                if (actors.length > 1) {
                    for (int i = 0; i < actors.length; i++)
                        System.out.println((i + 1) + ") " + actors[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Diversos atores/atrizes encontrados(as), escolha um(a): "));
                            if(n < 1 || n > actors.length) throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception e) {
                            System.out.println("Insira um número válido");
                        }
                    }

                    Actor actor = actors[n];

                    String name = prompt.askForInput("Nome (vazio para não alterar): ");
                    if(!name.isEmpty())
                        actor.setName(name);

                    aFile.update(actor);

                    System.out.println("Operação finalizada com sucesso");

                    break;
                }
            }

            case 4: {
                String search = prompt.askForInput("Nome: ");
                Actor[] actors = aFile.read(search);
                int n = 0;

                while (actors == null) {
                    search = prompt.askForInput("Nenhuma série encontrada, tente novamente: "); 
                    actors = aFile.read(search);
                }

                if (actors.length > 1) {
                    for (int i = 0; i < actors.length; i++)
                        System.out.println((i + 1) + ") " + actors[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                            if (n < 1 || n > actors.length) 
                                throw new Exception();

                            valid = true;
                            --n;
                        } catch(Exception ex) { 
                            System.out.println("Insira um número válido"); 
                        }
                    }
                }

                int id = actors[n].getID();

                if(!aFile.delete(id))
                    System.out.println("Não foi possível concluir a operação");
                else
                    System.out.println("Operação finalizada com sucesso");

                break;
            }
        }


    }

}
