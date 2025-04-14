package pucflix.entity;

import java.time.LocalDate;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.time.format.DateTimeFormatter;
import pucflix.aeds3.EntidadeArquivo;

public class Episode implements EntidadeArquivo {
    // Atributes
    private int id;
    private String name;
    private int season;
    private LocalDate releaseDate;
    private int durationTime;
    private int show;
    
    private DateTimeFormatter formatter;
    
    // Constructors
    public Episode(String name, int season, LocalDate releaseDate, int durationTime, int show) {
        this(-1, name, season, releaseDate, durationTime, show);
    }
    public Episode() {
        this(-1, "", -1, LocalDate.now(), 0, -1);
    }
    public Episode(int id, String name, int season, LocalDate releaseDate, int durationTime, int show) {
        this.id = id;
        this.name = name;
        this.season = season;
        this.releaseDate = releaseDate;
        this.durationTime = durationTime;
        this.show = show;
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    // Gets and Sets
    // Id
    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
    // Name
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    // Season
    public void setSeason(int season) {
        this.season = season;
    }
    public int getSeason() {
        return season;
    }
    // Release date
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    // Duration time
    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }
    public int getDurationTime() {
        return durationTime;
    }
    
    public void setShow(int show) {
        this.show = show;
    }
    public int getShow() {
        return show;
    }

    // Return atributes
    public String toString() {
        return name + 
                    "\n\tid: " + this.id +
                    "\n\tnome: " + this.name +
                    "\n\ttemporada: " + this.season +
                    "\n\tdata de lançamento: " + this.releaseDate.format(formatter) +
                    "\n\tduração: " + this.durationTime;
    }

    // Primitive type to byte array
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.name);
        dos.writeInt(this.season);
        dos.writeInt((int)this.releaseDate.toEpochDay());
        dos.writeInt(this.durationTime);
        dos.writeInt(this.show);
        return baos.toByteArray();
    }

    // From byte array to primitive type
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.name = dis.readUTF();
        this.season = dis.readInt();
        this.releaseDate = LocalDate.ofEpochDay(dis.readInt());
        this.durationTime = dis.readInt();
        this.show = dis.readInt();
    }
}
