package pucflix.model;

import pucflix.entity.Episode;
import pucflix.aeds3.Arquivo;
import pucflix.aeds3.HashExtensivel;
import pucflix.aeds3.ArvoreBMais;
import pucflix.aeds3.ParIdId;
import java.util.ArrayList;

public class EpisodeFile extends Arquivo<Episode> 
{
    ArvoreBMais<EpisodeNameIdPair> nameIndex;    
    ArvoreBMais<ParIdId> showRelIndex;

    public EpisodeFile() throws Exception
    {
        super("episodes", Episode.class.getConstructor());
        nameIndex = new ArvoreBMais<>(
            EpisodeNameIdPair.class.getConstructor(),
            4,
            "./dados/" + nomeEntidade + "/nameIndex.db"
        );
        showRelIndex = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            4,
            "./dados/" + nomeEntidade + "/showRelIndex.db" 
        );
    }

    @Override
    public int create(Episode episode) throws Exception 
    {
        int id = super.create(episode);
        nameIndex.create(new EpisodeNameIdPair(episode.getName(), id));
        showRelIndex.create(new ParIdId(episode.getShow(), id));
        return id;
    }

    @Override
    public boolean update(Episode episode) throws Exception 
    {
        Episode s = read(episode.getID());

        if(s == null) return false;
        if(!super.update(episode)) return false; 
        
        if(!s.getName().equals(episode.getName()))
        {
            nameIndex.delete(new EpisodeNameIdPair(s.getName(), s.getID()));
            nameIndex.create(new EpisodeNameIdPair(episode.getName(), episode.getID()));
        }

        return true;
    }

    public Episode[] read(String name) throws Exception
    {
        return read(name, -1);
    }

    public Episode[] read(String name, int showID) throws Exception
    {
        if(name.isEmpty()) return null;

        ArrayList<EpisodeNameIdPair> pairs = nameIndex.read(new EpisodeNameIdPair(name, -1));
        if(pairs.size() == 0) return null;

        Episode[] episodes = new Episode[pairs.size()];
        int i = 0; 
        for(EpisodeNameIdPair pair : pairs) 
        {
            Episode episode = read(pair.getID());
             
            if(showID == -1 || episode.getShow() == showID)
            {
                episodes[i++] = episode;
            }
        }

        Episode[] finalArr = new Episode[i];

        System.arraycopy(episodes, 0, finalArr, 0, i);

        return finalArr;
    }

    public Episode[] readAllFromShow(int showID) throws Exception
    {
        ArrayList<ParIdId> pairs = showRelIndex.read(new ParIdId(showID, -1));
        if(pairs.size() == 0) return null;

        Episode[] episodes = new Episode[pairs.size()];

        for(int i = 0; i < pairs.size(); i++)
        {
            episodes[i] = read(pairs.get(i).getId2());
        }

        return episodes;
    }

    @Override
    public boolean delete(int id) throws Exception 
    {
        Episode episode = read(id);
        
        if(episode == null) return false;
        if(!super.delete(id)) return false;
        boolean nameR = nameIndex.delete(new EpisodeNameIdPair(episode.getName(), id));
        boolean relR = showRelIndex.delete(new ParIdId(episode.getShow(), id));
        return nameR && relR;
    }
}
