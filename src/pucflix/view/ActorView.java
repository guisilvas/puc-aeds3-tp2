package pucflix.view;

import pucflix.aeds3.ArvoreBMais;
import pucflix.entity.Actor;
import pucflix.entity.Show;
import pucflix.model.ActorFile;
import pucflix.model.ShowFile;

public class ActorView extends View {
    private ActorFile aFile;
    private ShowFile sFile;
    private String name;

    private ArvoreBMais<ActorShowPair> actorShowPair;
    private ArvoreBMais<ShowActorPair> showActorPair;

    public ActorView(Prompt prompt, ActorFile aFile, ShowFile sFile) throws Exception {
        super(prompt);
        this.aFile = aFile;
        this.sFile = sFile;

        this.actorShowPair = new ArvoreBMais<>(
            ActorShowPair.class.getConstructor(),
            4, 
            "./dados/actor/actorShow.db"
        );

        this.showActorPair = new ArvoreBMais<>(
            showActorPair.class.getConstructor(),
            4, 
            "./dados/show/showActor.db"
        );
    }

    @Override
    public String getName() {
        return "Atores";
    }

    @Override
    public String getPrompt(int depth) throws Exception {
        return
            "1) Incluir\n" +
            "2) Buscar\n" +
            "3) Alterar\n" +
            "4) Excluir";    
    }

    @Override
    public void eval(int input, int depth) throws Exception {
        switch (input) {
            case 1: {
                String name = prompt.askForInput("Nome: ");
                Actor actor = new Actor(name);
                int actorID = aFile.create(actor);
                int showID = selectShow();
                if (showID != -1) {
                    actorShowFile.create(actorID, showID);
                    showActorFile.create(showID, actorID);
                    System.out.println("Operação finalizada com sucesso");
                }
                break;
            }
                
            case 2: {
                String search = prompt.askForInput("Busca: ");
                Actor[] actors = aFile.read(search);

                if (actors == null || actors.length == 0) {
                    System.out.println("Nenhum ator/atriz encontrado(a)");
                    break;
                }

                for (int i = 0; i < actors.length; i++)
                    System.out.println((i + 1) + ") " + actors[i].getName());

                int choice = Integer.parseInt(prompt.askForInput("Número: ")) - 1;
                if (choice < 0 || choice >= actors.length) {
                    System.out.println("Número inválido");
                    return;
                }

                Actor selectedActor = actors[choice];
                int[] showIDs = actorShowFile.getShows(selectedActor.getID());
                System.out.println(selectedActor);
                for (int id : showIDs) {
                    Show show = sFile.read(id);
                    if (show != null)
                        System.out.println(" - " + show.getName());
                }

                break;
            }

            case 3: {
                String search = prompt.askForInput("Nome: ");
                Actor[] actors = aFile.read(search);
                if (actors == null || actors.length == 0) {
                    System.out.println("Nenhum ator/atriz encontrado(a)");
                    return;
                }

                for (int i = 0; i < actors.length; i++)
                    System.out.println((i + 1) + ") " + actors[i].getName());

                int choice = Integer.parseInt(prompt.askForInput("Número: ")) - 1;
                if (choice < 0 || choice >= actors.length) {
                    System.out.println("Número inválido");
                    return;
                }

                Actor selectedActor = actors[choice];
                String newName = prompt.askForInput("Novo nome (vazio para não alterar): ");
                if (!newName.isEmpty()) {
                    selectedActor.setName(newName);
                    aFile.update(selectedActor);
                    System.out.println("Nome atualizado com sucesso");
                }

                break;
            }

            case 4: {
                String search = prompt.askForInput("Nome: ");
                Actor[] actors = aFile.read(search);
                if (actors == null || actors.length == 0) {
                    System.out.println("Nenhum ator/atriz encontrado(a)");
                    return;
                }

                for (int i = 0; i < actors.length; i++)
                    System.out.println((i + 1) + ") " + actors[i].getName());

                int choice = Integer.parseInt(prompt.askForInput("Número: ")) - 1;
                if (choice < 0 || choice >= actors.length) {
                    System.out.println("Número inválido");
                    return;
                }

                int actorID = actors[choice].getID();
                actorShowFile.deleteActor(actorID);
                aFile.delete(actorID);
                System.out.println("Ator removido com sucesso");

                break;
            }
        }
    }
}
