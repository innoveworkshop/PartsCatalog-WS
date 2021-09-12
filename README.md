# PartsCatalog Web Service

A port of the PartsCatalog application to a web service.


## Installation

This project is based on JSP/Servlets and was designed to be compiled with
[Java 11](https://openjdk.java.net/projects/jdk/11/) and run on a
[Tomcat 9](https://tomcat.apache.org/download-90.cgi) server, so be sure to have
those installed first. In order to compile and deploy this project you'll also
need [Ant 1.10](https://ant.apache.org/).

### Configuration

The first thing you'll need to do is to create a `build.properties` file in
the root of the project directory and fill in the appropriate fields below
(remember to have the `manager-script` role associated with the Tomcat
Manager user in order for this to work):

    catalina.home=/path/to/tomcat/
    manager.url=http://localhost:8080/manager/text
    manager.username=tomcatuser
    manager.password=tomcatpass
    app.path=/yourappcontext

Now you're ready to deploy the project to the server!

### Deployment

All that's left to do is to deploy the application to your Tomcat server. Which
can be done by running the following command:

    $ ant install

If you've updated something and want the application to be redeployed all you
have to do is run:

    $ ant update

And if you're tired of having this in your server:

    $ ant remove


## License

This project is licensed under the **MIT License**.
