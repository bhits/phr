# Patient Health Record Service

The Patient Health Record (PHR) service is a component of Consent2Share. It is a core service that manages and retains information about each patient. It does **not** store patients' consents or added providers. (That is handled by the [Patient Consent Management (PCM)](https://github.com/bhits-dev/pcm) service). PHR also manages any C32 and/or C-CDA documents that a **patient** has uploaded to his or her own account for use in testing their consents using the [Try My Policy](https://github.com/bhits-dev/try-policy) feature.

## Build

### Prerequisites

+ [Oracle Java JDK 8 with Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
+ [Docker Engine](https://docs.docker.com/engine/installation/) (for building a Docker image from the project)

### Commands

This is a Maven project and requires [Apache Maven](https://maven.apache.org/) 3.3.3 or greater to build it. It is recommended to use the *Maven Wrapper* scripts provided with this project. *Maven Wrapper* requires an internet connection to download Maven and project dependencies for the very first build.

To build the project, navigate to the folder that contains the `pom.xml` using the terminal/command line.

+ To build a JAR:
    + For Windows, run `mvnw.cmd clean install`
    + For *nix systems, run `mvnw clean install`
+ To build a Docker Image (this will create an image with `bhitsdev/phr:latest` tag):
    + For Windows, run `mvnw.cmd clean install & cd web & ..\mvnw.cmd clean package docker:build & cd..`
    + For *nix systems, run `mvnw clean install; cd ./web; ../mvnw clean package docker:build; cd ..`

## Run

### Prerequisites

This project uses *[MySQL](https://www.mysql.com/)* for persistence and *[Flyway](https://flywaydb.org/)* for database migration. It requires having a database user account with Object and DDL Rights to a schema with the default name `phr`. Please see [Configure](#configure) section for details of configuring the data source. 

[SQL files](phr-db-sample/) are provided with this project to populate it with a small set of sample lookup data.

### Commands

This is a [Spring Boot](https://projects.spring.io/spring-boot/) project and serves the project via an embedded Tomcat instance. Therefore, there is no need for a separate application server to run this service.
+ Run as a JAR file: `java -jar phr-x.x.x-SNAPSHOT.jar <additional program arguments>`
+ Run as a Docker Container: `docker run -d bhitsdev/phr:latest <additional program arguments>`

*NOTE: In order for this Service to fully function as a microservice in the Consent2Share application, it is required to setup the dependency microservices and the support level infrastructure. Please refer to the Consent2Share Deployment Guide in the corresponding Consent2Share release (see [Consent2Share Releases Page](https://github.com/bhits-dev/consent2share/releases)) for instructions to setup the Consent2Share infrastructure.*


## Configure

This project utilizes [`Configuration Server`](https://github.com/bhits-dev/config-server) which is based on [Spring Cloud Config](https://github.com/spring-cloud/spring-cloud-config) to manage externalized configuration, which is stored in a `Configuration Data Git Repository`. We provide a [`Default Configuration Data Git Repository`]( https://github.com/bhits-dev/c2s-config-data).

This project can run with the default configuration, which is targeted for a local development environment. Default configuration data is from three places: `bootstrap.yml`, `application.yml`, and the data which `Configuration Server` reads from `Configuration Data Git Repository`. Both `bootstrap.yml` and `application.yml` files are located in the `resources` folder of this source code.

We **recommend** overriding the configuration as needed in the `Configuration Data Git Repository`, which is used by the `Configuration Server`.

Also, please refer to [Spring Cloud Config Documentation](https://cloud.spring.io/spring-cloud-config/spring-cloud-config.html) to see how the config server works, [Spring Boot Externalized Configuration](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) documentation to see how Spring Boot applies the order to load the properties, and [Spring Boot Common Properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html) documentation to see the common properties used by Spring Boot.

### Other Ways to Override a Configuration

#### Override a Configuration Using Program Arguments While Running as a JAR:

+ `java -jar phr-x.x.x-SNAPSHOT.jar --server.port=80 --spring.datasource.password=strongpassword`

#### Override a Configuration Using Program Arguments While Running as a Docker Container:

+ `docker run -d bhitsdev/phr:latest --server.port=80 --spring.datasource.password=strongpassword`

+ In a `docker-compose.yml`, this can be provided as shown below:
```yml
version: '2'
services:
...
  phr.c2s.com:
    image: "bhitsdev/phr:latest"
    command: ["--server.port=80","--spring.datasource.password=strongpassword"]
...
```
*NOTE: Please note that these additional arguments will be appended to the default `ENTRYPOINT` specified in the `Dockerfile` unless the `ENTRYPOINT` is overridden.*

### Configuring Sample C32/C-CDA Documents

The PHR allows patients to upload C32 and/or C-CDA documents to their account for use when testing their consents using Try My Policy. The PHR can also be configured to provide one or more sample C32 and/or C-CDA documents which will be made available to all patients to use when testing their consents. By default, the PHR is configured to provide a single sample document named `"C-CDA_R2_CCD_2_MODIFIED.xml"` to all patients. That sample document is built into the PHR application itself, and the default `application.yml` file is set to use that built-in `"C-CDA_R2_CCD_2_MODIFIED.xml"` file as the sample document for patients.

To use your own file(s) as the sample document(s) for patients, override the `application.yml` file's `c2s.phr.patient-document-uploads.sample-uploaded-documents` property as follows:
```yml
...
c2s:
  phr:
    patientDocumentUploads:
      ...
      sampleUploadedDocuments:
        - file: "<FULL PATH TO YOUR SAMPLE FILE, INCLUDING FILE NAME>"
          fileName: "<FILE NAME OF YOUR SAMPLE FILE, WITHOUT THE ENTIRE PATH>"
          documentName: "<NAME OF DOCUMENT TO SHOW TO USERS>"
          contentType: "<MIME TYPE OF YOUR SAMPLE FILE>"
...
```

You can also configure PHR to provide more than one sample document. To do so, see the following example:
```yml
...
c2s:
  phr:
    patientDocumentUploads:
      ...
      sampleUploadedDocuments:
        - file: "/usr/local/custom_sample_docs/sample_doc_1.xml"
          fileName: "sample_doc_1.xml"
          documentName: "Sample Document 1"
          contentType: "text/xml"
        - file: "/usr/local/custom_sample_docs/sample_doc_2.xml"
          fileName: "sample_doc_2.xml"
          documentName: "Sample Document 2"
          contentType: "text/xml"
...
```

**IMPORTANT NOTES:**
1. For the `file` property, you need to specify the entire path to your sample file **and** the file name itself (e.g. `"/usr/local/custom_sample_docs/sample_doc_1.xml"`).
2. For the `fileName` property, you need to specify just the file name **without** the path (e.g. `"sample_doc_1.xml"`). **The file name you use here must be the same as the file name at the end of the path in the `file` property.**
3. For the `documentName` property, this can be any user friendly string to use as the document name which is displayed to users.
4. For the `contentType` property, specify the correct [MIME type](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types) of your sample file. This will usually be `"text/xml"`.

### Enable SSL

For simplicity in development and testing environments, SSL is **NOT** enabled by default configuration. SSL can easily be enabled following the examples below:

#### Enable SSL While Running as a JAR

+ `java -jar phr-x.x.x-SNAPSHOT.jar --spring.profiles.active=ssl --server.ssl.key-store=/path/to/ssl_keystore.keystore --server.ssl.key-store-password=strongkeystorepassword`

#### Enable SSL While Running as a Docker Container

+ `docker run -d -v "/path/on/dockerhost/ssl_keystore.keystore:/path/to/ssl_keystore.keystore" bhitsdev/phr:latest --spring.profiles.active=ssl --server.ssl.key-store=/path/to/ssl_keystore.keystore --server.ssl.key-store-password=strongkeystorepassword`
+ In a `docker-compose.yml`, this can be provided as follows:
```yml
version: '2'
services:
...
  phr.c2s.com:
    image: "bhitsdev/phr:latest"
    command: ["--spring.profiles.active=ssl","--server.ssl.key-store=/path/to/ssl_keystore.keystore", "--server.ssl.key-store-password=strongkeystorepassword"]
    volumes:
      - /path/on/dockerhost/ssl_keystore.keystore:/path/to/ssl_keystore.keystore
...
```

*NOTE: As seen in the examples above, `/path/to/ssl_keystore.keystore` is made available to the container via a volume mounted from the Docker host running this container.*

### Override Java CA Certificates Store In Docker Environment

Java has a default CA Certificates Store that allows it to trust well-known certificate authorities. For development and testing purposes, one might want to trust additional self-signed certificates. In order to override the default Java CA Certificates Store in a Docker container, one can mount a custom `cacerts` file over the default one in the Docker image as follows: `docker run -d -v "/path/on/dockerhost/to/custom/cacerts:/etc/ssl/certs/java/cacerts" bhitsdev/phr:latest`

*NOTE: The `cacerts` references given in the volume mapping above are files, not directories.*

## Contact

If you have any questions, comments, or concerns please see [Consent2Share](https://bhits-dev.github.io/consent2share/) project site.

## Report Issues

Please use [GitHub Issues](https://github.com/bhits-dev/phr/issues) page to report issues.
