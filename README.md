# GuiceHibernatePlugin Template

This project is a template to facilitate the creation of plugins that use
guice as a dependency injection framework and hibernate as a JPA providing library.

This project works on Spigot 1.8.8. Projects for newer Minecraft versions
only need a version update in the spigot dependency.

## Implemented Functionality

The implemented functionality keeps track of players joining and killing each other.
With a command the current amount of kills and deaths is displayed. Some details about
the killing are stored in the database, however no implementation is in place to put
this data to any use.

### Commands

| Command      | Permission                       | Description                                                 |
|--------------|----------------------------------|-------------------------------------------------------------|
| /playerstats | guicehibernateplugin.playerstats | This command displays the current stats of the given player |

## Run configurations

This project comes with predefined run configurations for IntelliJ IDEA. They build the shaded jar, copy it to a 
spigot server installation and then run the server. This has the advantage that one can restart the minecraft server
and begin debugging with a single button click and doesn't have to manually build, copy and then run the plugin. 
Since the configs contain the absolute path of where your installation is located in your file system you will have
to adapt the location of the jar as well as the working directory. Also, you have to update your `build.gradle.kts` to
copy the built jar into the correct location.

## Quick Walkthrough of the code

This plugin demonstrates the use of Guice as a dependecy injection framework and
Hibernate as a library to take care of your database entites. Before diving into the
details of this setup, I want to highly recommend considering to use a Microservice as
a proper backend if you have complicated entity structures. This allows you to reduce
the load that your server puts on the database, but I'll leave that design choice up to you.

### Minecraft-Plugins

To learn about the general concepts of Spigot plugin development I highly recommend
https://www.spigotmc.org/wiki/spigot/ as a starter guide. A lot of concepts are laid out
in well written tutorials, and it's overall a good place to get started. I will not go into
detail how the server code actually works, this can be easily found out with the resource
I linked above. I will however leave some remarks on how some features interact with
Guice and/or hibernate. However, one thing shall be mentioned explicitly because it is SUPER IMPORTANT!

After adding a dependency make sure that you relocate the created packages in the jar. This is absolutely mandatory!
Minecraft Plugins are loaded into the classpath, if you do not relocate the dependency in your plugin jars there might
be version conflicts with other plugins that use the same library as you but in a different version. To find out which
plugins collide in their library versions is extremely tedious, and you really do not want to be responsible for your
server admin having to do that work.

### Guice

To quote the creators:

Guice (pronounced 'juice') is a lightweight dependency injection framework for Java 8 and above,
brought to you by Google.

https://github.com/google/guice

I will not explain the general concepts of dependency injection, and why this design pattern is great.
You can find an introduction here: https://github.com/google/guice/wiki.

To set up Guice, you need to pick a version that suits your needs. A list of tags is available on maven central,
please see the release details in the Guice wiki for details about the versions. For this project I chose to work
with  "kotlinised" Gradle as my build configuration system, but you can work with maven just as well.
Remember to relocate the created packages!

When using Guice in any environment you will need an injector somewhere, that allows you to manage your instances.
Here it makes only sense to put it into your main plugin class that extends `JavaPlugin`. The injector is created
from your own module, that we will create now.

Time has shown that you almost always need the instance of your plugin at some point, so saving the instance in
your Module is reasonable. Then you need to configure the Module to explain to Guice which classes are bound to
specific object instances (like our main plugin class) and which classes will be created using some custom process
using Guice providers. In case you decide to use your own structure for your data repositories or to ditch hibernate
altogether, you can also bind interfaces to a concrete implementation class of the interface. Please see the Guice
documentation for further use cases.

This leads us to bind the main plugin class to the instance that we got in the plugin's `onEnable` method. Then we
bind Gson to a provider created for it. In this case it might be dropped, but as soon as you need to configure Gson
any further (e.g. to add parsers for your own classes or some stupid time stamps in some strings) you will find it
easier to configure it with a provider. Finally, we have to create our objects that we need for Hibernate on our own,
since we have to tell Hibernate which connection data to use and where to find it. Details on this can be found in
the Hibernate section.

Minecraft plugins require you to register your listeners and commands when your plugin is enabled, so we will use
the injector we just created to obtain instances of the listeners and the commands. These classes are annotated with
two annotations:

```java
@Singleton
@RequiredArgsConstructor(onConstructor__ = @Inject)
```

The first one marks it as a Singleton. This tells Guice that only one instance of this class is allowed to exist.
The second one creates a constructor for all properties of the class that are marked as `final`. The constructor itself
is then annotated with the `@Inject` annotation. This means that all arguments of the constructor will be provided by
guice/the injector we created.

After that, all services, repositories and other classes, are simply written down as attributes of the class. Since we
do not create any instances of the classes ourselves (except in the provider classes), we can just assume them as given.
Only Singleton classes should be injected there, but most classes you need should be Singleton anyway. With Guice,
there is no need for helper classes that contain a lot of `public static` methods. They can be rewritten to a service
class which is a Singleton and which can be injected by Guice. In practical terms this makes few differences when
coding, but it avoids problems with static loaded classes. Sometimes class loading and reloading goes wrong on Minecraft
servers, but using dependency injecton you can be sure to get the correct instance of your class. This is relevant
when you store data in some static data structure like a

```private static final HashMap<String, String> myData = new HashMap<>();```

### Hibernate

Hibernate is an implementation for JPA, the standard API for object/relational persistence in Java with a number of
extensions to the specification of JPA. You can find details on https://Hibernate.org.

As someone who is used to have the comforts of Spring microservices as backends, it has always been a pain to write all
your SQL queries per hand and manage all entities and relations manually in Minecraft. Hibernate takes that work off
your shoulders. Even though it is not exactly the same thing as Spring's JPA module, it is the library that does all 
the heavy lifting for it. I've grown to like Hibernate to manage my database stuff, this is just a personal choice.

Hibernate makes use of the `javax.persistence` package to handle its entities. 

Every database connection need the connections details of how to reach the database. To provide the necessary details 
about the connection I created a data class that contains the connection url, the username and the password to use.
It can be converted to a `Properties` object that also contains a number of details about the connection (e.g. the
driver lass to use and more). This object is built in a `Provider` class. The provider itself gets the instance of the 
`GuiceHibernatePlugin` and the `Gson` object. The plugin provides the data folder which contains the `database.json`.
The connection details are parsed with Gson into the `HibernateConnectionPropeties` objects.

To create the database connections a provider is used for the `SessionFactory` objects according to the usage of Guice.
It receives the connection properties via the injector and then builds a session factory. This is done by first 
creating a `StandardServiceRegistry` with the connection properties. Then the classes that are representing the 
database entites are registered in an object containing metadata about the sources. After some final setup the 
`SessionFactory` object is built and returned.

The last things missing to work with the database entites that are now defined and configured are the repositories.
I constructed an abstract `Repository` that is built similar to the `JpaRepository` interface of Spring. However, it is 
not as feature rich and contains only the basic operations of `save`, `update`, `delete` and `findById`. The class
is using two templates, `T` and `S extends Serializable`. `T` is the entity class and `S` is its id. This usually is 
either a `Integer`, `Long` or `String`, but really any sensible id type can be used here as long as the database of your
choice supports it. If you want to perform additional actions you have to define your own methods and perform the 
queries there. Examples for how this can be done can be found in the `PlayerKillRepository`.





