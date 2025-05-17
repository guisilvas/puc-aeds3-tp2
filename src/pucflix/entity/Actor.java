package pucflix.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import pucflix.aeds3.EntidadeArquivo;

public class Actor implements EntidadeArquivo {

    private int id;
    private String name;

    public Actor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Actor(String name) {
        this(-1, name);
    }

    public Actor() {
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

    public void setName(String name) {
        this.name = name;
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

    public String toString() {
        return name;
    }
}
