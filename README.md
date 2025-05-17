# Pucflix

O Pucflix é um pequeno banco de dados em arquivo que permite o gerenciamento, ou seja, a criação, exclusão, atualização de atributos e busca de séries e os elementos que a compõem, como episódios.

O sistema gerencia os relacionamentos entre os registros automaticamente, como a relação entre episódios e séries, além de manter a integridade dos dados regrando as operações de acordo com o estado dos relacionamentos.

## Participantes

- [Diego Feitosa Ferreira dos Santos](https://github.com/Sil3ncy)
- [Felipe Guerzoni Martins Flôres Maia](https://github.com/flp2113)
- [Guilherme Soares Silva](https://github.com/guisilvas)
- [Mateus Henrique Medeiros Diniz](https://github.com/mateushmd)

## Estrutura 

### Entidades

`Episode`

Representa o registro episódio. Contém os atributos, getters, setters e métodos para conversão entre objeto e binário.

`Show`

Representa o registro série. Contém atributos, getters e setters e métodos de conversão entre objeto e binário.

`Actor`

Representa o registro ator. Contém os atributos, getters, setters e métodos para conversão entre objeto e binário.

### Modelo

`ShowFile`

Classe da camada de modelo para séries. Contém os métodos do CRUD tanto para a própria série quanto para o relacionamento entre série e ator, o índice para o par nome-id para busca por nome, e índice id-id para relacionamento com ator.

`EpisodeFile`

Classe da camada de modelo para episódios. Contém os métodos do CRUD e um método que recupera todos os episódios de uma série ordenados por temporada, além do índice nome-id para busca por nome e o índice id-id para busca por série.

`ActorFile`

Clase da camada de modelo para atores. Contém os métodos CRUD tanto para o próprio ator quanto para o relacionamento entre ator e série, índice para o par nome-id para busca por nome, e índice id-id para relacionamento com série.

`NameIdPair`

Par nome-id genérico, utilizada em uma árvore B+ para busca de registros pelo nome.

### Visão

`Prompt`

Classe central da camada de visão. É responsável por exibir os prompts e ler a entrada, controlar o fluxo do programa e decidir qual visão vai ser utilizada. É possível acrescentar mais visões de forma escalável, uma vez que a classe ajusta dinamicamente o prompt para exibir todas as escolhas para visões e todas as opções de uma visão.

`View`

Classe abstrata molde para visões. Contém todos os métodos necessários para que uma visão possa ser utilizada pela classe `Prompt`, como o `getName()` para exibir a visão que está sendo utilizada no momento, o `getPrompt()` para recuperar as opções para serem impressas na tela, e o `eval()` para processar a entrada do usuário e executar o comando correspondente.

`ShowView`

Classe da camada de visão da série, implementação concreta da `View`. Realiza todo o pré-processamento, como a leitura dos inputs para executar todas as operações do CRUD e exibe os resultados das operações, como a exibição dos episódios por temporada de uma série específica.

`EpisodeView`

Classe da camada de visão do episódio, implementação concreta da `View`. Realiza todo o pré-processamento para executar todas as operações do CRUD e exibe o resultado das operações na tela.

`ActorView`

Classe da camada de visão do ator, implementação concreta da `View`. Realiza todo o pré-processamento para executar todas as operações do CRUD e exibe o resultado das operações na tela.

## Experiência

O desafio desta etapa foi implementar o relacionamento N:N, o que não se mostrou particularmente difícil, já que, com base na experiência prévia com a árvore B+ e relacionamentos 1:N, já tínhamos uma ideia de como proceder. Tivemos, no entanto, certa dificuldade inicial para compreender a ordem correta dos identificadores no par id-id, tanto do lado do ator quanto da série, uma vez que essa ordem afeta diretamente o comportamento da árvore. Após compreendermos como deveria ser implementado, conseguimos completar com relativa facilidade, apenas redobrando a atenção para atender às restrições mais rigorosas de remoção, tanto do lado dos atores quanto do lado das séries.

- [X] As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente?
- [X] O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades?
- [X] É possível consultar quais são os atores de uma série?
- [X] É posssível consultar quais são as séries de um ator?
- [X] A remoção de séries remove os seus vínculos de atores?
- [X] A inclusão de um ator em uma série em um episódio se limita aos atores existentes?
- [X] A remoção de um ator checa se há alguma série vinculado a ele?
- [X] O trabalho está funcionando corretamente?
- [X] O trabalho está completo?
- [X] O trabalho é original e não a cópia de um trabalho de outro grupo?

## Compilação e execução
Para compilar e executar o programa, utilize o script correspondente ao seu sistema operacional:
- *Linux*: `build.sh`
- *Windows*: `build.bat`

Alternativamente, execute a seguinte sequência de comandos na pasta raíz do projeto:

Crie a pasta `/bin` na pasta raíz do projeto caso não exista ainda:

```sh
mkdir bin
```
Ainda na pasta raíz, execute a seguinte sequência de comandos:

```sh
cd src

javac -cp . -d ../bin pucflix/Main.java pucflix/aeds3/*.java pucflix/view/*.java pucflix/model/*.java pucflix/entity/*.java

cd ../bin

java -cp . pucflix.Main
```
