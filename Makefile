
DIR := $(shell basename `pwd`)

$(DIR).jar: src/test/*.java src/main/*.java build.gradle Makefile
	gradle build
	gradle shadowJar
	cp build/libs/$(DIR)-all.jar $(DIR).jar

run: $(DIR).jar
	java -jar $(DIR).jar

clean:
	rm -rf build $(DIR).jar 50URLS.txt .gradle

