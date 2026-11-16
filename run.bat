@echo off
echo Compilando SGTH...
javac -d out src/main/java/com/sgth/model/*.java src/main/java/com/sgth/repository/*.java src/main/java/com/sgth/Main.java

if %errorlevel% equ 0 (
    echo Executando SGTH...
    java -cp out com.sgth.Main
) else (
    echo Erro na compilacao!
    pause
)