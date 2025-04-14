package pucflix.view;

import pucflix.entity.Episode;
import pucflix.entity.Show;
import pucflix.model.EpisodeFile;
import pucflix.model.ShowFile;
import java.time.LocalDate;

public class EpisodeView extends View 
{
    private EpisodeFile eFile; 
    private ShowFile sFile;
    private int showID;
    private String name;

    public EpisodeView(Prompt prompt, EpisodeFile eFile, ShowFile sFile) throws Exception
    {
        super(prompt);
        showID = -1;
        this.eFile = eFile;
        this.sFile = sFile;
    }

    @Override
    public String getName()
    {
        return "Episódios";
    }

    @Override
    public String getPrompt(int depth) throws Exception
    {
        if(showID == -1)
        {
            String search = prompt.askForInput("Escolha a série para gerenciar os episódios: ");

            Show[] shows = sFile.read(search);
            int n = 0;

            while(shows == null)
            {
                search = prompt.askForInput("Nenhuma série encontrada, tente novamente: ");
                shows = sFile.read(search);
            }

            if(shows.length > 1)
            {
                for(int i = 0; i < shows.length; i++)
                    System.out.println((i + 1) + ") " + shows[i].getName());

                boolean valid = false;
                while(!valid)
                {
                    try
                    {
                        n = Integer.parseInt(prompt.askForInput("Diversas séries encontradas, escolha uma: "));
                        if(n < 1 || n > shows.length) throw new Exception();
                        valid = true;
                        --n;
                    }
                    catch(Exception ex)
                    { System.out.println("Insira um número válido"); }
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

    @Override
    public void eval(int input, int depth) throws Exception
    {

        switch(input)
        {
            case 1: 
            {
                String name = prompt.askForInput("Nome: ");
                int season = Integer.parseInt(prompt.askForInput("Temporada: "));
                int day = Integer.parseInt(prompt.askForInput("Dia do lançamento: "));
                int month = Integer.parseInt(prompt.askForInput("Mês do lançamento: "));
                int year = Integer.parseInt(prompt.askForInput("Ano do lançamento: "));
                int durationTime = Integer.parseInt(prompt.askForInput("Tempo de duração (em minutos): "));
                Episode episode = new Episode(name, season, LocalDate.of(year, month, day), durationTime, showID); 
                eFile.create(episode);
                System.out.println("Operação feita com sucesso!");
                break;
            }
            case 2: 
            {
                String search = prompt.askForInput("Busca: ");
                Episode[] episodes = eFile.read(search);

                if(episodes == null || episodes.length == 0)
                    System.out.println("Nenhum episódio encontrado");

                if(episodes.length == 1)
                {
                    System.out.println(episodes[0].toString());
                    break;
                }
                    
                String result = "";
                
                for(int i = 0; i < episodes.length; i++)
                    result += (i + 1) + ") " + episodes[i].getName() + "\n"; 

                System.out.println(result);

                int n = 0;
                boolean valid = false;

                while(!valid)
                {
                    try
                    {
                        n = Integer.parseInt(prompt.askForInput("Número: "));
                        if(n < 1 || n > episodes.length) throw new Exception();
                        valid = true;
                    }
                    catch(Exception ex) { System.out.println("Insira um número válido"); }
                }

                System.out.println(episodes[n - 1].toString());
                
                break;
            }
            case 3: 
            {
                String search = prompt.askForInput("Nome: ");

                Episode[] episodes = eFile.read(search);
                int n = 0;

                while(episodes == null)
                {
                    search = prompt.askForInput("Nenhum episódio encontrado, tente novamente:");
                    episodes = eFile.read(search);
                }

                if(episodes.length > 1)
                {
                    for(int i = 0; i < episodes.length; i++)
                        System.out.println((i + 1) + ") " + episodes[i].getName());

                    boolean valid = false;
                    while(!valid)
                    {
                        try
                        {
                            n = Integer.parseInt(prompt.askForInput("Diversos episódios encontrados, escolha um: "));
                            if(n < 1 || n > episodes.length) throw new Exception();
                            valid = true;
                            --n;
                        }
                        catch(Exception ex) { System.out.println("Insira um número válido"); }
                    }
                }

                Episode episode = episodes[n];

                String name = prompt.askForInput("Nome (vazio para não alterar): ");
                if(!name.isEmpty())
                    episode.setName(name);

                String season = prompt.askForInput("Temporada (vazio para não alterar): ");
                if(!season.isEmpty())
                    episode.setSeason(Integer.parseInt(season));

                String day = prompt.askForInput("Dia do lançamento (vazio para não alterar): ");
                String month = prompt.askForInput("Mês do lançamento (vazio para não alterar): ");
                String year = prompt.askForInput("Ano do lançamento (vazio para não alterar): ");
                
                LocalDate releaseDate = episode.getReleaseDate();
                
                int newDay = day.isEmpty() ? releaseDate.getDayOfMonth() : Integer.parseInt(day);
                int newMonth = month.isEmpty() ? releaseDate.getMonthValue() : Integer.parseInt(month);
                int newYear = year.isEmpty() ? releaseDate.getYear() : Integer.parseInt(year);
                
                episode.setReleaseDate(LocalDate.of(newYear, newMonth, newDay));

                String durationTime = prompt.askForInput("Tempo de duração em minutos (vazio para não alterar): ");
                if(!durationTime.isEmpty())
                    episode.setDurationTime(Integer.parseInt(durationTime));
                
                eFile.update(episode);
                break;
            }
            case 4:
            {
                String search = prompt.askForInput("Nome: ");

                Episode[] episodes = eFile.read(search);
                int n = 0;

                while(episodes == null)
                {
                    search = prompt.askForInput("Nenhum episódio encontrado, tente novamente: ");
                    episodes = eFile.read(search);
                }

                if(episodes.length > 1)
                {
                    for(int i = 0; i < episodes.length; i++)
                        System.out.println((i + 1) + ") " + episodes[i].getName());

                    boolean valid = false;
                    while(!valid)
                    {
                        try
                        {
                            n = Integer.parseInt(prompt.askForInput("Diversos episódios encontrados, escolha um: "));
                            if(n < 1 || n > episodes.length) throw new Exception();
                            valid = true;
                            --n;
                        }
                        catch(Exception ex) { System.out.println("Insira um número válido"); }
                    }
                }

                int id = episodes[n].getID();

                if(!eFile.delete(id))
                    System.out.println("Não foi possível concluir a operação");
                else
                    System.out.println("Operação finalizada com sucesso");
                break;
            }
        }
    }

    @Override
    public void exit()
    {
        showID = -1;
    }
}
