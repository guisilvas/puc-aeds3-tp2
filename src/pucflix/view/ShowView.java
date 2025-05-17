package pucflix.view;

import pucflix.entity.Show;
import pucflix.entity.Actor;
import pucflix.entity.Episode;
import pucflix.model.ShowFile;
import pucflix.model.ActorFile;
import pucflix.model.EpisodeFile;

public class ShowView extends View {
    private ShowFile file;
    private EpisodeFile eFile;
    private ActorFile aFile;

    public ShowView(Prompt prompt, ShowFile file, EpisodeFile eFile, ActorFile aFile) throws Exception {
        super(prompt);
        this.file = file;
        this.eFile = eFile;
        this.aFile = aFile;
    }

    @Override
    public String getName() {
        return "Séries";
    }

    @Override
    public String getPrompt(int depth) throws Exception {
        return "1) Incluir\n" +
                "2) Buscar\n" +
                "3) Alterar\n" +
                "4) Excluir\n" +
                "5) Listar episódios\n" +
                "6) Adicionar ator\n" +
                "7) Remover ator\n" +
                "8) Listar atores";
    }

    @Override
    public void eval(int input, int depth) throws Exception {
        switch (input) {
            case 1: {
                String name = prompt.askForInput("Nome: ");
                int releaseYear = Integer.parseInt(prompt.askForInput("Ano de lançamento: "));
                String sinopsys = prompt.askForInput("Sinopse: ");
                String streaming = prompt.askForInput("Serviço de streaming: ");
                Show show = new Show(name, releaseYear, sinopsys, streaming);
                file.create(show);
                System.out.println("Operação finalizada com sucesso");
                break;
            }
            case 2: {
                String search = prompt.askForInput("Busca: ");
                Show[] shows = file.read(search);

                if (shows == null || shows.length == 0) {
                    System.out.println("Nenhuma série encontrada");
                    break;
                }

                if (shows.length == 1) {
                    System.out.println(shows[0].toString());
                    break;
                }

                String result = "";

                for (int i = 0; i < shows.length; i++)
                    result += (i + 1) + ") " + shows[i].getName() + "\n";

                System.out.println(result);

                int n = 0;
                boolean valid = false;

                while (!valid) {
                    try {
                        n = Integer.parseInt(prompt.askForInput("Número: "));
                        if (n < 1 || n > shows.length)
                            throw new Exception();
                        valid = true;
                        n--;
                    } catch (Exception ex) {
                        System.out.println("Insira um número válido");
                    }
                }

                System.out.println(shows[n].toString());

                break;
            }
            case 3: {
                String search = prompt.askForInput("Nome: ");

                Show[] shows = file.read(search);
                int n = 0;

                while (shows == null) {
                    search = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(search);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                Show show = shows[n];

                String name = prompt.askForInput("Nome (vazio para não alterar): ");
                if (!name.isEmpty())
                    show.setName(name);

                String strYear = prompt.askForInput("Ano de lançamento (vazio para não alterar): ");
                if (!strYear.isEmpty())
                    show.setReleaseYear(Integer.parseInt(strYear));

                String synopsis = prompt.askForInput("Sinopse (vazio para não alterar): ");
                if (!synopsis.isEmpty())
                    show.setSynopsis(synopsis);

                String streaming = prompt.askForInput("Serviço de streaming (vazio para não alterar): ");
                if (!streaming.isEmpty())
                    show.setStreamingService(streaming);

                file.update(show);

                System.out.println("Operação finalizada com sucesso");
                break;
            }
            case 4: {
                String search = prompt.askForInput("Nome: ");

                Show[] shows = file.read(search);
                int n = 0;

                while (shows == null) {
                    search = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(search);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                int id = shows[n].getID();

                Episode[] showEpisodes = eFile.readAllFromShow(id);

                if (showEpisodes != null && showEpisodes.length > 0) {
                    System.out.println("A série não pode ser excluída pois está vinculada a episódios");
                    break;
                }

                Actor[] showActors = aFile.readByShow(id);

                if (showActors != null && showActors.length > 0) {
                    if (!aFile.removeAllActors(id)) {
                        System.out.println("Operação falhou");
                        break;
                    }

                    for (Actor actor : showActors) {
                        if (!file.removeActor(id, actor.getID())) {
                            System.out.println("Não foi possível excluir a relação entre a série e o ator "
                                    + actor.getName() + ", prosseguindo com a operação mesmo assim");
                        }
                    }
                }

                if (!file.delete(id))
                    System.out.println("Operação falhou");
                else
                    System.out.println("Operação finalizada com sucesso");
                break;
            }
            case 5: {
                String search = prompt.askForInput("Nome: ");

                Show[] shows = file.read(search);
                int n = 0;

                while (shows == null) {
                    search = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(search);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                System.out.println("Episódios de " + shows[n].getName());

                Episode[] episodes = eFile.readAllFromShow(shows[n].getID());

                for (int i = episodes.length - 1; i > 0; i--) {
                    for (int j = 0; j < i; j++) {
                        if (episodes[j].getSeason() > episodes[j + 1].getSeason()) {
                            Episode t = episodes[j];
                            episodes[j] = episodes[j + 1];
                            episodes[j + 1] = t;
                        }
                    }
                }

                int season = 0;

                for (int i = 0; i < episodes.length; i++) {
                    if (episodes[i].getSeason() != season) {
                        season = episodes[i].getSeason();
                        System.out.println("\tTemporada " + season);
                    }

                    System.out.println("\t\t" + episodes[i].getName());
                }

                break;
            }
            case 6: {
                String showName = prompt.askForInput("Nome da série: ");
                Show[] shows = file.read(showName);
                int n = 0;

                while (shows == null) {
                    showName = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(showName);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Escolha uma série: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                int showId = shows[n].getID();

                String actorName = prompt.askForInput("Nome do ator: ");
                Actor[] actors = aFile.readByName(actorName);
                int m = 0;

                while (actors == null) {
                    actorName = prompt.askForInput("Nenhum ator encontrado, tente novamente: ");
                    actors = aFile.readByName(actorName);
                }

                if (actors.length > 1) {
                    for (int i = 0; i < actors.length; i++)
                        System.out.println((i + 1) + ") " + actors[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            m = Integer.parseInt(prompt.askForInput("Escolha um ator: "));
                            if (m < 1 || m > actors.length)
                                throw new Exception();
                            valid = true;
                            --m;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                int actorId = actors[m].getID();

                if (file.addActor(showId, actorId) && aFile.addShow(actorId, showId))
                    System.out.println("Ator adicionado à série com sucesso");
                else
                    System.out.println("Erro ao adicionar ator à série");

                break;
            }
            case 7: {
                String showName = prompt.askForInput("Nome da série: ");
                Show[] shows = file.read(showName);
                int n = 0;

                while (shows == null) {
                    showName = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(showName);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Escolha uma série: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                int showId = shows[n].getID();
                Actor[] actors = aFile.readByShow(showId);

                if (actors == null || actors.length == 0) {
                    System.out.println("Nenhum ator vinculado a esta série");
                    break;
                }

                for (int i = 0; i < actors.length; i++)
                    System.out.println((i + 1) + ") " + actors[i].getName());

                int m = 0;
                boolean valid = false;
                while (!valid) {
                    try {
                        m = Integer.parseInt(prompt.askForInput("Escolha o ator a ser removido: "));
                        if (m < 1 || m > actors.length)
                            throw new Exception();
                        valid = true;
                        --m;
                    } catch (Exception ex) {
                        System.out.println("Insira um número válido");
                    }
                }

                int actorId = actors[m].getID();

                if (file.removeActor(showId, actorId) && aFile.removeShow(actorId, showId))
                    System.out.println("Ator removido da série com sucesso");
                else
                    System.out.println("Erro ao remover ator da série");

                break;
            }
            case 8: {
                String showName = prompt.askForInput("Nome da série: ");
                Show[] shows = file.read(showName);
                int n = 0;

                while (shows == null) {
                    showName = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                    shows = file.read(showName);
                }

                if (shows.length > 1) {
                    for (int i = 0; i < shows.length; i++)
                        System.out.println((i + 1) + ") " + shows[i].getName());

                    boolean valid = false;
                    while (!valid) {
                        try {
                            n = Integer.parseInt(prompt.askForInput("Escolha uma série: "));
                            if (n < 1 || n > shows.length)
                                throw new Exception();
                            valid = true;
                            --n;
                        } catch (Exception ex) {
                            System.out.println("Insira um número válido");
                        }
                    }
                }

                int showId = shows[n].getID();
                Actor[] actors = aFile.readByShow(showId);

                if (actors == null || actors.length == 0)
                    System.out.println("Nenhum ator vinculado a esta série");
                else {
                    System.out.println("Atores de " + shows[n].getName() + ":");
                    for (Actor a : actors)
                        System.out.println("- " + a.getName());
                }

                break;
            }

        }
    }
}
