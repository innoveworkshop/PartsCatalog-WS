# PartsCatalog Web Service

A web service to manage your electronic components library.


## Consuming the Web Service

Unfortunately I don't have time to properly document all of the API endpoints,
but you can use the following projects to interact with the web service:

  - [Windows Application](https://github.com/innoveworkshop/PartsCatalog-Desktop)
  - [.NET Library](https://github.com/innoveworkshop/PartsCatalog.NET)


## Installation

This project is based on JSP/Servlets and was designed to be compiled with
[Java 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=openj9)
and run on a [Tomcat 9](https://tomcat.apache.org/download-90.cgi) server, so be
sure to have those installed first. The database used in this project is
[SQL Server Express 2019](https://go.microsoft.com/fwlink/?linkid=866658).
In order to compile and deploy this project you'll also need
[Maven](https://maven.apache.org/).

### Configuration

The first thing you'll need to do is to create a `hibernate.cfg.xml` file
in `src/main/resources` directory using the provided
`example.hibernate.cfg.xml` file with your SQL Server information.

Next up is the actual database configuration. This can be done by opening the
[SQL Server Management Studio](https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver15)
and importing the `sql/Initialize.sql` file into your instance which will
create the database and its associated tables, but also the `partscatalog`
for use by the web application.

Now you're ready to deploy the project to the server!

### Deployment

All that's left to do is to deploy the application to your Tomcat server. Which
can be done by creating a WAR file the following command:

    $ mvn package

After that you can use Tomcat's Manager web application to deploy the WAR file.


## License

This project is licensed under the **MIT License**.
