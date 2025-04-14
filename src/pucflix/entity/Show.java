package pucflix.entity;

import pucflix.aeds3.EntidadeArquivo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Show implements EntidadeArquivo 
{
    private int id;
    private String name;
    private int releaseYear;
    private String synopsis;
    private String streamingService;

    public Show()
    {
        this(-1, "", 0, "", "");
    }

    public Show(String name, int releaseYear,
                    String synopsis, String streamingService)
    {
        this(-1, name, releaseYear, synopsis, streamingService);
    }

    public Show(int id, String name, int releaseYear,
                    String synopsis, String streamingService)
    {
        this.id = id;
        this.name = name;
        this.releaseYear = releaseYear;
        this.synopsis = synopsis;
        this.streamingService = streamingService;
    }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getStreamingService() { return streamingService; }
    public void setStreamingService(String streamingService) { this.streamingService = streamingService; }

    public byte[] toByteArray() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(name);
        dos.writeInt(releaseYear);
        dos.writeUTF(synopsis);
        dos.writeUTF(streamingService);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] raw) throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(raw);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        name = dis.readUTF();
        releaseYear = dis.readInt();
        synopsis = dis.readUTF();
        streamingService = dis.readUTF();
    }

    public String toString()
    {
        return
            name + 
            "\n\tid: " + id +
            "\n\tano de lançamento: " + releaseYear +
            "\n\tsinopse: " + synopsis +
            "\n\tserviço de streaming: " + streamingService;
    }
}
