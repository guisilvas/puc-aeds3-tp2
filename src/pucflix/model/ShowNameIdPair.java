package pucflix.model;

import pucflix.aeds3.RegistroArvoreBMais;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class ShowNameIdPair implements RegistroArvoreBMais<ShowNameIdPair>
{
    private String name;
    private int id;
    private short SIZE = 104;
    private short NAME_SIZE = 100;

    public ShowNameIdPair() throws Exception
    {
        this("", -1);
    }

    public ShowNameIdPair(String name) throws Exception
    {
        this(name, -1);
    }

    public ShowNameIdPair(String name, int id) throws Exception
    {
        this.name = "";
        this.id = id;

        if(name.isEmpty()) return;

        this.name = name;

        byte[] buff = name.getBytes(StandardCharsets.UTF_8);

        if(buff.length <= NAME_SIZE) return;

        byte[] secureBuff = new byte[NAME_SIZE];
        System.arraycopy(buff, 0, secureBuff, 0, secureBuff.length);

        int lastChar = NAME_SIZE - 1;
        while(lastChar > 0 && 
            (secureBuff[lastChar] < 0 || secureBuff[lastChar] > 127)) lastChar--;

        byte[] finalBuff = new byte[lastChar + 1];
        System.arraycopy(secureBuff, 0, finalBuff, 0, finalBuff.length);

        this.name = new String(finalBuff);
    }

    public String getName() { return name; }
    public int getID() { return id; }

    @Override
    public ShowNameIdPair clone()
    {
        try { return new ShowNameIdPair(name, id); }
        catch(Exception e) { e.printStackTrace(); }

        return null;
    }

    public short size() { return SIZE; }

    public int compareTo(ShowNameIdPair other)
    {
        String str1 = transform(name);
        String str2 = transform(other.name);

        if(str2.length() > str1.length())
            str2 = str2.substring(0, str1.length());

        if(str1.compareTo(str2) == 0) 
        {
            if(id == -1)
                return 0;
            else
                return id - other.id;
        }
        else
            return str1.compareTo(str2);
    }

    public byte[] toByteArray() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] buff = new byte[NAME_SIZE];
        byte[] nameBuff = name.getBytes();
        int i = 0;

        while(i < nameBuff.length)
        {
            buff[i] = nameBuff[i];
            i++;
        }

        while(i < NAME_SIZE)
        {
            buff[i] = ' ';
            i++;
        }

        dos.write(buff);
        dos.writeInt(id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] buffer) throws IOException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        DataInputStream dis = new DataInputStream(bais);
        byte[] nameBuff = new byte[NAME_SIZE];
        
        dis.read(nameBuff);
        name = (new String(nameBuff)).trim();
        id = dis.readInt();
    }

    public String toString() 
    {
        return name + ";" + String.format("%-3d", id);
    }

    public static String transform(String str)
    {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("").toLowerCase();
    }
}
