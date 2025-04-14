package pucflix.aeds3;

import java.io.*;
import java.lang.reflect.Constructor;

public class Arquivo<T extends EntidadeArquivo> {

    public String nomeEntidade;
    RandomAccessFile arquivo;
    HashExtensivel<ParIDEndereco> indiceDireto;
    Constructor<T> construtor;
    final int TAMANHO_CABECALHO = 13;

    public Arquivo(String ne, Constructor<T> c) throws Exception {
        this.nomeEntidade = ne;
        this.construtor = c;
        File f = new File("./dados");
        if(!f.exists())
            f.mkdir();
        f = new File("./dados/"+nomeEntidade);
        if(!f.exists())
            f.mkdir();
        arquivo = new RandomAccessFile("./dados/"+nomeEntidade+"/"+nomeEntidade +".db", "rw");
        if(arquivo.length()<TAMANHO_CABECALHO) {
            arquivo.writeByte(2);  // versão do Arquivo
            arquivo.writeInt(0);   // último ID
            arquivo.writeLong(-1);   // ponteiro para primeiro registro excluído
            (new File("./dados/"+nomeEntidade+"/indiceDireito.d.db")).delete();
            (new File("./dados/"+nomeEntidade+"/indiceDireito.c.db")).delete();
        }
        indiceDireto = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(),
            4,
            "./dados/"+nomeEntidade+"/indiceDireito.d.db",
            "./dados/"+nomeEntidade+"/indiceDireito.c.db");
    }

    public int create(T entidade) throws Exception {
        // Obtem o ID da nova entidade
        arquivo.seek(1);
        int novoID = arquivo.readInt() + 1;
        entidade.setID(novoID);
        arquivo.seek(1);
        arquivo.writeInt(novoID);

        // Grava o novo registro
        byte[] vb = entidade.toByteArray();
        long endereco = getDeleted(vb.length);
        if(endereco==-1) {
            endereco = arquivo.length();
            arquivo.seek(endereco);
            arquivo.writeByte(' ');
            arquivo.writeShort(vb.length);
            arquivo.write(vb);
        } else {
            arquivo.seek(endereco);
            arquivo.writeByte(' ');
            arquivo.skipBytes(2);
            arquivo.write(vb);
        }
        indiceDireto.create(new ParIDEndereco(novoID, endereco));
        return novoID;
    }

    public T read(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if(pie==null)
            return null;
        long endereco = pie.getEndereco();
        if(endereco==-1)
            return null;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if(lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(dados);
            if(entidade.getID() == id)
                return entidade;
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        ParIDEndereco pie = indiceDireto.read(id);
        if(pie==null)
            return false;
        long endereco = pie.getEndereco();
        if(endereco==-1)
            return false;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if(lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(dados);
            if(entidade.getID() == id) {
                arquivo.seek(endereco);
                arquivo.writeByte('*');
                addDeleted(tamanho, endereco);
                indiceDireto.delete(id);
                return true;
            }
        }
        return false;
    }

    public boolean update(T novaEntidade) throws Exception {
        ParIDEndereco pie = indiceDireto.read(novaEntidade.getID());
        if(pie==null)
            return false;
        long endereco = pie.getEndereco();
        if(endereco==-1)
            return false;

        arquivo.seek(endereco);
        byte lapide = arquivo.readByte();
        short tamanho = arquivo.readShort();
        if(lapide == ' ') {
            byte[] dados = new byte[tamanho];
            arquivo.read(dados);
            T entidade = construtor.newInstance();
            entidade.fromByteArray(dados);
            if(entidade.getID() == novaEntidade.getID()) {
                byte[] dados2 = novaEntidade.toByteArray();
                short tamanho2 = (short)dados2.length;
                if(tamanho2<=tamanho) {
                    arquivo.seek(endereco+3);
                    arquivo.write(dados2);
                } else {
                    arquivo.seek(endereco);
                    arquivo.writeByte('*');
                    addDeleted(tamanho, endereco);

                    endereco = getDeleted(tamanho2);
                    if(endereco==-1) {
                        endereco = arquivo.length();
                        arquivo.seek(endereco);
                        arquivo.writeByte(' ');
                        arquivo.writeShort(tamanho2);
                        arquivo.write(dados2);
                    } else {
                        arquivo.seek(endereco);
                        arquivo.writeByte(' ');
                        arquivo.skipBytes(2);
                        arquivo.write(dados2);
                    }
                    indiceDireto.update(new ParIDEndereco(novaEntidade.getID(), endereco));
                }
                return true;
            }
        } else {
            arquivo.skipBytes(tamanho);
        }
        return false;
    }

    public void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        long anterior = 5;
        long proximo = -1;
        byte lapide;
        short tamanho;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        if(endereco==-1) {
            arquivo.seek(5);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco+3);
            arquivo.writeLong(proximo);  // -1
        } else {
            do {
                arquivo.seek(endereco);
                lapide = arquivo.readByte();
                tamanho = arquivo.readShort();
                proximo = arquivo.readLong();
                if(lapide == '*' && tamanhoEspaco < tamanho) {
                    if(anterior == 5) {   // ajustar o cabeçalho do arquivo
                        arquivo.seek(anterior);
                    } else {
                        arquivo.seek(anterior+3);
                    }
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(endereco);
                    break;
                }
                if(proximo==-1) {   // fim da lista 
                    arquivo.seek(endereco+3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco+3);
                    arquivo.writeLong(-1);
                    break;
                }
                anterior = endereco;
                endereco = proximo;
            } while(endereco!=-1);
        }
    }

    public long getDeleted(int tamanhoNecessario) throws Exception {
        long anterior = 5;
        arquivo.seek(anterior);
        long endereco = arquivo.readLong();
        byte lapide;
        int tamanho;
        long proximo;
        while(endereco != -1) {
            arquivo.seek(endereco);
            lapide = arquivo.readByte();
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();
            if(lapide == '*' && tamanho >= tamanhoNecessario) {
                if(anterior == 5)
                    arquivo.seek(anterior);
                else
                    arquivo.seek(anterior+3);
                arquivo.writeLong(proximo);
                break;
            }
            anterior = endereco;
            endereco = proximo;
        }
        return endereco;
    }

}
