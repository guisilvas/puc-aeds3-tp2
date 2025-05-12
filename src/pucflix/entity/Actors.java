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
    private String name;

    public Actors(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Actors() {
        this(-1, "");
    }

    // GET SET
    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.name = dis.readUTF();
    }
}
