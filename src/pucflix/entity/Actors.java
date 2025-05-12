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

    public Actors(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Actors() {
        this(-1, "");
    }

    // GET SET
    public int getID() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
    }
}
