package pucflix.model;

import pucflix.entity.Episode;
import pucflix.aeds3.Arquivo;
import pucflix.aeds3.HashExtensivel;
import pucflix.aeds3.ArvoreBMais;
import pucflix.aeds3.ParIdId;
import java.util.ArrayList;

public class ActorFile extends File 
{
    ArvoreBMais<ActorNameIdPair> nameIndex;
    ArvoreBMais<Pair
    
    public ActorFile() throws Exception
    {
        super("actor", Actor.class.getConstructor());
        nameIndex = new ArvoreBMais<>(
            ActorNameIdPair.class.getConstructor(),
            4,
            "./dados/" + nomeEntidade + "/nameIndex.db"
        );
    }
	
    @Override
    public int create(Actor actor) throws Exception 
    {
        int id = super.create(actor);
        nameIndex.create(new ActorNameIdPair(episode.getName(), id));
        showRelIndex.create(new ParIdId(actor.getShow(), id));
        return id;
    }

    @Override
    public boolean update(Actor actor) throws Exception 
    {
        Actor a = read(actor.getID());

        if(a == null) return false;
        if(!super.update(actor)) return false; 
        
        if(!a.getName().equals(actor.getName()))
        {
            nameIndex.delete(new ActorNameIdPair(a.getName(), a.getID()));
            nameIndex.create(new ActorNameIdPair(actor.getName(), actor.getID()));
        }

        return true;
    }

    public Actor[] read(String name) throws Exception
    {
        if(name.isEmpty()) return null;

        ArrayList<ActorNameIdPair> pairs = nameIndex.read(new ActorNameIdPair(name, -1));
        if(pairs.size() == 0) return null;

        Actor[] actors = new Actor[pairs.size()];
        
        for(int i = 0; i < pairs.size(); i++)
        {
            actors[i] = read(pairs.get(i).getID());
        }

        return actors;
    }

    @Override
    public boolean delete(int id) throws Exception 
    {
        Actor actor = read(id);
        
        if(actor == null) return false;
        if(!super.delete(id)) return false;
        return nameIndex.delete(new ActorNameIdPair(actor.getName(), id));
    }

    public boolean delete(String name) throws Exception
    {
        if(name.isEmpty())
            return false;

        ArrayList<ActorNameIdPair> pairs = nameIndex.read(new ActorNameIdPair(name, -1));
        for(ActorNameIdPair pair : pairs)
        {
            if(pair.getName().equals(name))
                return delete(pair.getID());
        }

        return false;
    }
}
