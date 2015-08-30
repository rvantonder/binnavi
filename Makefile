setup:
	mvn dependency:copy-dependencies

all:
	ant -f src/main/java/com/google/security/zynamics/build.xml   build-binnavi-fat-jar

run: 
	java -jar target/binnavi-all.jar
