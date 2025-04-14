/*
REGISTRO HASH EXTENSÍVEL

Esta interface apresenta os métodos que os objetos
a serem incluídos na tabela hash extensível devem 
conter.

Implementado pelo Prof. Marcos Kutova
v1.1 - 2021
*/
package pucflix.aeds3;

public interface RegistroHashExtensivel {

  public int hashCode(); // chave numérica positiva para ser usada no diretório

  public short size(); // tamanho FIXO do registro

  public byte[] toByteArray() throws Exception; // representação do elemento em um vetor de bytes

  public void fromByteArray(byte[] ba) throws Exception; // vetor de bytes a ser usado na construção do elemento

}
