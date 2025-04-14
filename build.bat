if not exist bin\ mkdir bin\

cd src

javac -cp . -d ../bin pucflix/Main.java pucflix/aeds3/*.java pucflix/view/*.java pucflix/model/*.java pucflix/entity/*.java

cd ..\bin

java -cp . pucflix.Main
