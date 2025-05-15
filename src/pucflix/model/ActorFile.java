package pucflix.model;

import pucflix.aeds3.Arquivo;
import pucflix.aeds3.HashExtensivel;
import pucflix.aeds3.ArvoreBMais;
import pucflix.aeds3.ParIdId;
import pucflix.entity.Actor;
import java.util.ArrayList;

public class ActorFile extends Arquivo<Actor> {

  Arquivo<Actor> arqAtores;

  ArvoreBMais<NameIdPair> nameIndex;

  ArvoreBMais<ParIdId> indiceRelacaoSerieAtor;

  public ActorFile() throws Exception {

    super("episodes", Actor.class.getConstructor());

    File directory = new File("./dados/ator");
    if (!directory.exists()) {
        directory.mkdirs(); 
    }

    nameIndex = new BPlusTree<>(
      NameIdPair.class.getConstructor(), 5, "./dados/ator" + "indiceNome.db");

    actorSeriesRelationIndex = new BPlusTree<>(
      IdIdPair.class.getConstructor(), 5, "./dados/ator" + "/indiceRelacaoSerieAtor.db");
  }

  @Override
  public int create(Actor actor) throws Exception {

    int id = super.create(actor);

    nameIndex.create(new NameIdPair(actor.getActorName(), id));

    actorSeriesRelationIndex.create(new IdIdPair(actor.getSeriesID(), id));

    return id;
  }

  public Actor[] readByName(String name) throws Exception {

    if(name.length() == 0) return null;

    ArrayList<NameIdPair> pairs = nameIndex.read(new NameIdPair(name, -1));

    if(!pairs.isEmpty()){

      Actor[] actors = new Actor[pairs.size()];

      int i = 0;

      for(NameIdPair pair : pairs){
        actors[i++] = read(pair.getId());
      }

      return actors;

    } else {
      return null;
    }
  }

  @Override
  public boolean delete(int id) throws Exception {

    Actor actor = read(id);

    if (actor != null){

      if(super.delete(id)){

        return nameIndex.delete(new NameIdPair(actor.getActorName(), id)) &&
               actorSeriesRelationIndex.delete(new IdIdPair(actor.getSeriesID(), id));
      }
    }

    return false;
  }

  @Override
  public boolean update(Actor updatedActor) throws Exception {

    Actor currentActor = read(updatedActor.getID());

    if(currentActor != null){

      if(super.update(updatedActor)){

        if(!currentActor.getActorName().equals(updatedActor.getActorName())){

          nameIndex.delete(new NameIdPair(currentActor.getActorName(), currentActor.getID()));
          nameIndex.create(new NameIdPair(updatedActor.getActorName(), updatedActor.getID()));

        }
        return true;
      }
    }
    return false;
  }

  public Actor[] readBySeries(int seriesId) throws Exception {

    ArrayList<IdIdPair> pairs = actorSeriesRelationIndex.read(new IdIdPair(seriesId, -1));

    if (pairs.isEmpty()) {
      return null;
    }

    Actor[] actors = new Actor[pairs.size()];
    int i = 0;

    for (IdIdPair pair : pairs) {
      actors[i++] = read(pair.getId2());
    }

    return actors;
  }
}
