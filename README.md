# Description EN

The RESTful web interface prototype for a banking app was implemented using the **Java 21** and **Spring Boot**.
Added persistence implementation with **JPA**. Used **H2** Database because of quick configuration and deployment.

### Database

URL to access H2-Console: `/h2-console`.
<br>
Default credentials:

```
"jdbc-url": "jdbc:h2:mem:testdb"
"login": "sa"
```

### Docker

_.sh_ and _.bat_ scripts to run the application via Docker are located in `docker` folder.

### Swagger

RESTful API documentation can be accessed through `/openapi`

## How to run

### Docker

If you would like to run this service via Docker use the scripts in `docker` folder.<br>
`bat` folder for Windows scripts.`sh` folder for Shell scripts.

- build-image - script to build the docker image
- start-container - script to run the container with default parameters.

If you're using a UNIX system, give executable permissions to
scripts: `chmod +x build-image.sh`, `chmod +x start-container.sh`
and `chmod +x mvnw` (_mvnw_ is located in the project root directory).<br>
To run the service first build the docker image using the `build-image` script and then use the
corresponding `start-container` script.
Once container is started, the service is available under default port `8080`.

### Standalone

Spring Boot allows to run a service as standalone JAR application.

1) Download and install JDK/JRE for Java 21.
   [JDK Download](https://www.oracle.com/de/java/technologies/downloads/#java21).
2) Download and install Maven. [Maven Download](https://maven.apache.org/download.cgi).
3) Build the project using the next command in root project directory: `mvnw clean install` for UNIX
   and `mvnw.cmd clean install` for Windows.
4) Run the service using `java -jar boot/webapps/markant-bank-prototype.jar`.
5) Access the service under default port `8080`.

# Beschreibung DE

Der Prototyp einer RESTful-Webschnittstelle für eine Bankanwendung wurde unter Verwendung von **Java 21**
und **Spring Boot** entwickelt.
Die Persistenzschicht wurde mithilfe von **JPA** umgesetzt. Die **H2-Datenbank** wurde aufgrund ihrer einfachen
Konfiguration und schnellen Bereitstellung gewählt.

### Datenbank

URL zur H2-Konsole: `/h2-console`.
<br>
Standardmäßige Zugangsdaten:

```
"jdbc-url": "jdbc:h2:mem:testdb"
"login": "sa"
```

### Docker

Die _.sh_ und _.bat_ Skripten zum Starten des Services unter Docker befinden sich im Ordner `docker`<br>

### Swagger

Die Dokumentation der RESTful-API ist unter der URL `/openapi` verfügbar.

## Starten

### Docker

Zum Starten des Services unter Docker verwenden die im Ordner `docker` befindlichen Skripte.<br>
Der Ordner `bat` ist für Windows. Der Ordner `sh` enthält Shell-Skripte.

- build-image - Skript zum Erstellen eines Docker-Image.
- start-container - Skript zum Starten eines Docker-Containers mit den Standardoptionen.

Falls ein UNIX-System verwendet wird, erteilen Sie den Skripten die
Ausführungserlaubnis: `chmod +x build-image.sh`, `chmod +x start-container.sh`
und `chmod +x mvnw` (_mvnw_ liegt im Wurzelverzeichnis des Projektes).<br>
Zum Starten des Services zunächst das Docker-Image erstellen mithilfe des Skriptes `build-image` und anschließend das
entsprechende Skript `start-container` ausführen,
um den Docker-Container zu starten. Sobald der Docker-Container läuft, ist der Service unter dem Port `8080` verfügbar.

### Standalone

Spring Boot ermöglicht das Ausführen des Services als eigenständige JAR-Anwendung.

1) Herunterladen und Installieren des JDK/JRE für
   Java21. [JDK Download](https://www.oracle.com/de/java/technologies/downloads/#java21).
2) Herunterladen und Installieren von Maven. [Maven Download](https://maven.apache.org/download.cgi).
3) Projektaufbau im Wurzelverzeichnis des Projektes mit dem Befehl: `mvnw clean install` für UNIX
   und `mvnw.cmd clean install` für Windows.
4) Starten des Services durch Ausführen des Kommandos: `java -jar boot/webapps/markant-bank-prototype.jar`.
5) Der Service ist unter dem Port `8080` verfügbar.