package pucflix.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import pucflix.aeds3.EntidadeArquivo;
import pucflix.entity.Show;

public class Actors implements EntidadeArquivo {

    private int id;
    private String nome;
    private List<Show> shows;

    public Actors(int id, String nome, Show shows) { // TODO:alterar para uma forma melhor de adicionar os filmes que
                                                     // ator o participou
        this.id = id;
        this.nome = nome;
        this.shows.add(shows);
    }

    public Actors() {
        this(-1, "", null);
    }

    // GET SET
    public int getID() {
        return id;
    }

    public String getNOME() {
        return nome;
    }

    public List<Show> getSHOWS() {
        return shows;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setNOME(String nome) {
        this.nome = nome;
    }

    public boolean setSHOW(Show show) {
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getID() == show.getID() || shows.get(i).getName().equals(show.getName())) { /*
                                                                                                          * verifica se
                                                                                                          * o show ja
                                                                                                          * foi
                                                                                                          * adicionado
                                                                                                          */
                System.out.println("ja existe um show com o mesmo nome/ID que esse ator participou !");
                System.out.println("nome: " + shows.get(i).getName());
                System.out.println("id: " + shows.get(i).getID());
                return false;
            }
        }

        shows.add(show);
        return true;
    }

    public boolean removeSHOW(String name) { // remove o show pelo nome
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getName() == name) {
                shows.remove(i);
                System.out.println("Show removido com sucesso");
                return true;
            }
        }

        System.out.println("show nao encontrado para remoção");
        return false;
    }

    public boolean removeSHOW(int id) { // remove o show pelo ID
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getID() == id) {
                shows.remove(i);
                System.out.println("Show removido com sucesso");
                return true;
            }
        }

        System.out.println("show nao encontrado para remoção");
        return false;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);

        dos.writeInt(shows.size()); // numero de shows que o ator participou

        // Escreve cada show
        for (Show show : shows) {
            byte[] showBytes = show.toByteArray();
            dos.writeInt(showBytes.length);
            dos.write(showBytes);
        }

        return baos.toByteArray();

    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();

        int qtdshows = dis.readInt();

        for (int i = 0; i < qtdshows; i++) {
            Show sh = new Show(dis.readInt(), dis.readUTF(), dis.readInt(), dis.readUTF(), dis.readUTF());// id, nome,
                                                                                                          // release
                                                                                                          // year,
                                                                                                          // synopsis,
                                                                                                          // streaming
                                                                                                          // service
            this.shows.add(sh);
        }
    }

}
