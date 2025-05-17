package pucflix.model;

import pucflix.aeds3.Arquivo;
import pucflix.aeds3.ArvoreBMais;
import pucflix.aeds3.ParIdId;
import pucflix.entity.Actor;
import java.util.ArrayList;

public class ActorFile extends Arquivo<Actor> {

  Arquivo<Actor> arqAtores;

  ArvoreBMais<NameIdPair> nameIndex;

  ArvoreBMais<ParIdId> indiceRelacaoSerieAtor;

  public ActorFile() throws Exception {

    super("actors", Actor.class.getConstructor());

    nameIndex = new ArvoreBMais<>(
        NameIdPair.class.getConstructor(), 4, "./dados/" + nomeEntidade + "/nameIndex.db");

    indiceRelacaoSerieAtor = new ArvoreBMais<>(
        ParIdId.class.getConstructor(), 4, "./dados/" + nomeEntidade + "/showRelIndex.db");
  }

  @Override
  public int create(Actor actor) throws Exception {

    int id = super.create(actor);

    nameIndex.create(new NameIdPair(actor.getName(), id));

    return id;
  }

  public Actor[] readByName(String name) throws Exception {

    if (name.length() == 0)
      return null;

    ArrayList<NameIdPair> pairs = nameIndex.read(new NameIdPair(name, -1));

    if (!pairs.isEmpty()) {

      Actor[] actors = new Actor[pairs.size()];

      int i = 0;

      for (NameIdPair pair : pairs) {
        actors[i++] = read(pair.getID());
      }

      return actors;

    } else {
      return null;
    }
  }

  @Override
  public boolean delete(int id) throws Exception {

    Actor actor = read(id);

    if (actor != null) {

      if (super.delete(id)) {

        return nameIndex.delete(new NameIdPair(actor.getName(), id));
      }
    }

    return false;
  }

  @Override
  public boolean update(Actor updatedActor) throws Exception {

    Actor currentActor = read(updatedActor.getID());

    if (currentActor != null) {

      if (super.update(updatedActor)) {

        if (!currentActor.getName().equals(updatedActor.getName())) {

          nameIndex.delete(new NameIdPair(currentActor.getName(), currentActor.getID()));
          nameIndex.create(new NameIdPair(updatedActor.getName(), updatedActor.getID()));

        }
        return true;
      }
    }
    return false;
  }

  public boolean addShow(int actorId, int showId) throws Exception {
    return indiceRelacaoSerieAtor.create(new ParIdId(showId, actorId));
  }

  public boolean removeShow(int actorId, int showId) throws Exception {
    return indiceRelacaoSerieAtor.delete(new ParIdId(showId, actorId));
  }

  public boolean removeAllActors(int showId) throws Exception {
    return indiceRelacaoSerieAtor.delete(new ParIdId(showId, -1));
  }

  public Actor[] readByShow(int showId) throws Exception {

    ArrayList<ParIdId> pairs = indiceRelacaoSerieAtor.read(new ParIdId(showId, -1));

    if (pairs.isEmpty()) {
      return null;
    }

    Actor[] actors = new Actor[pairs.size()];
    int i = 0;

    for (ParIdId pair : pairs) {
      actors[i++] = read(pair.getId2());
    }

    return actors;
  }
}
