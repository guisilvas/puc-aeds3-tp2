package pucflix.model;

import pucflix.entity.Show;
import pucflix.aeds3.Arquivo;
import pucflix.aeds3.ArvoreBMais;
import pucflix.aeds3.ParIdId;
import java.util.ArrayList;

public class ShowFile extends Arquivo<Show> {
    ArvoreBMais<NameIdPair> nameIndex;
    ArvoreBMais<ParIdId> actorRelIndex;

    public ShowFile() throws Exception {
        super("show", Show.class.getConstructor());
        nameIndex = new ArvoreBMais<>(
                NameIdPair.class.getConstructor(),
                4,
                "./dados/" + nomeEntidade + "/nameIndex.db");

        actorRelIndex = new ArvoreBMais<>(
                ParIdId.class.getConstructor(),
                4,
                "./dados/" + nomeEntidade + "/actorRelIndex.db");
    }

    @Override
    public int create(Show show) throws Exception {
        int id = super.create(show);
        nameIndex.create(new NameIdPair(show.getName(), id));
        return id;
    }

    @Override
    public boolean update(Show show) throws Exception {
        Show s = read(show.getID());

        if (s == null)
            return false;
        if (!super.update(show))
            return false;

        if (!s.getName().equals(show.getName())) {
            nameIndex.delete(new NameIdPair(s.getName(), s.getID()));
            nameIndex.create(new NameIdPair(show.getName(), show.getID()));
        }

        return true;
    }

    public Show[] read(String name) throws Exception {
        if (name.isEmpty())
            return null;

        ArrayList<NameIdPair> pairs = nameIndex.read(new NameIdPair(name, -1));
        if (pairs.size() == 0)
            return null;

        Show[] shows = new Show[pairs.size()];

        for (int i = 0; i < pairs.size(); i++) {
            shows[i] = read(pairs.get(i).getID());
        }

        return shows;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Show show = read(id);

        if (show == null)
            return false;
        if (!super.delete(id))
            return false;
        return nameIndex.delete(new NameIdPair(show.getName(), id));
    }

    public boolean delete(String name) throws Exception {
        if (name.isEmpty())
            return false;

        ArrayList<NameIdPair> pairs = nameIndex.read(new NameIdPair(name, -1));
        for (NameIdPair pair : pairs) {
            if (pair.getName().equals(name))
                return delete(pair.getID());
        }

        return false;
    }

    public boolean addActor(int showId, int actorId) throws Exception {
        return actorRelIndex.create(new ParIdId(actorId, showId));
    }

    public boolean removeActor(int showId, int actorId) throws Exception {
        return actorRelIndex.delete(new ParIdId(actorId, showId));
    }

    public Show[] readByActor(int actorId) throws Exception {
        ArrayList<ParIdId> pairs = actorRelIndex.read(new ParIdId(actorId, -1));

        if (pairs.isEmpty()) {
            return null;
        }

        Show[] shows = new Show[pairs.size()];
        int i = 0;

        for (ParIdId pair : pairs) {
            shows[i++] = read(pair.getId2());
        }

        return shows;
    }
}
