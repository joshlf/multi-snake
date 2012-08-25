all = snake

libSrc = core/*.java

snakeSrc = snake/*.java

snake : $(snakeSrc) $(libSrc)
	javac snake/SnakeMain.java && jar -cf Snake.jar snake/*.java core/*.java && echo "Compile successful, executable jar created."

clean :
	rm */*.class -R
	rm Snake.jar
