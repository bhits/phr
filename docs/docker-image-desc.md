# Short Description

The Patient Health Record (PHR) is a core service which manages and retains non-consent related information about each patient.

# Supported Source Code Tags and Current `Dockerfile` Link

[`2.1.0 (latest)`](https://github.com/bhits-dev/phr/releases/tag/2.1.0), [`2.0.0`](https://github.com/bhits-dev/phr/releases/tag/2.0.0)

[`Current Dockerfile`](../phr/src/main/docker/Dockerfile)

For more information about this image, the source code, and its history, please see the [GitHub repository](https://github.com/bhits-dev/phr).

# What is the PHR?

The Patient Health Record (PHR) service is a component of Consent2Share. It is a core service that manages and retains information about each patient. It does **not** store a patients' consents or added providers. That is handled by the [Patient Consent Management (PCM)](https://github.com/bhits-dev/pcm) service. The PHR also manages any C32 and/or C-CDA documents that a **patient** has uploaded to their own account for use in testing their consents using the [Try My Policy](https://github.com/bhits-dev/try-policy) feature.

For more information and related downloads for Consent2Share, please visit [Consent2Share](https://bhits-dev.github.io/consent2share/).

# How to Use This Image

## Start a PHR Instance

Become familiar with the repository's [README.md](../README.md) file before starting the instance.

`docker run  --name phr -d bhitsdev/phr:latest <additional program arguments>`

*NOTE: In order for this project to fully function as a microservice in the Consent2Share application, it is required to setup the dependency microservices and the support level infrastructure. Please refer to the Consent2Share Deployment Guide in the corresponding Consent2Share release (see [Consent2Share Releases Page](https://github.com/bhits-dev/consent2share/releases)) for instructions to setup the Consent2Share infrastructure.*
 
## Configure

The Spring profiles `application-default` and `docker` are activated by default when building images.

This project can run with the default configuration which is from three places: `bootstrap.yml`, `application.yml`, and the data which the [`Configuration Server`](https://github.com/bhits-dev/config-server) reads from the `Configuration Data Git Repository`. Both `bootstrap.yml` and `application.yml` files are located in the class path of the running application.

We **recommend** overriding the configuration as needed in the `Configuration Data Git Repository`, which is used by the `Configuration Server`.

Also, [Spring Boot](https://projects.spring.io/spring-boot/) supports other ways to override the default configuration to configure the project for a certain deployment environment. 

The following is an example to override the default database password:

`docker run -d bhitsdev/phr:latest --spring.datasource.password=strongpassword`

## Environment Variables

When you start the PHR image, you can edit the configuration of the PHR instance by passing one or more environment variables on the command line. 

### JAR_FILE

This environment variable is used to setup which jar file will run. You need to mount the jar file to the root of container.

`docker run --name phr -e JAR_FILE="phr-latest.jar" -v "/path/on/dockerhost/phr-latest.jar:/phr-latest.jar" -d bhitsdev/phr:latest`

### JAVA_OPTS 

This environment variable is used to setup a JVM argument, such as memory configuration.

`docker run --name phr -e "JAVA_OPTS=-Xms512m -Xmx700m -Xss1m" -d bhitsdev/phr:latest`

### DEFAULT_PROGRAM_ARGS 

This environment variable is used to setup an application argument. The default value is: "--spring.profiles.active=application-default, docker".

`docker run --name phr -e DEFAULT_PROGRAM_ARGS="--spring.profiles.active=application-default,ssl,docker" -d bhitsdev/phr:latest`

# Supported Docker Versions

This image is officially supported on Docker version 1.13.0.

Support for older versions (down to 1.6) is provided on a best-effort basis.

Please see the [Docker installation documentation](https://docs.docker.com/engine/installation/) for details on how to upgrade your Docker daemon.

# License

View [license](../LICENSE) information for the software contained in this image.

# User Feedback

## Documentation
 
Documentation for this image is stored in the [bhitsdev/phr](https://github.com/bhits-dev/phr) GitHub repository. Be sure to familiarize yourself with the repository's README.md file before attempting a pull request.

## Issues

If you have any problems with or questions about this image, please contact us through a [GitHub issue](https://github.com/bhits-dev/phr/issues).
