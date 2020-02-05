Setting up a EnchantmentTokens addon.

All addons will be loaded with EnchantmentTokens and the server jar.

Here is a simple Maven configuration:

```
<?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   
   <groupId>com.yourname.addonname</groupId>
   <artifactId>AddonName</artifactId>
   <packaging>jar</packaging>
   <version>VERSION</version>
   
   <repositories>
       <repository>
           <id>spigot-repo</id>
           <url>https://hub.spigotmc.org/nexus/content/repositories/snapshots/</url>
       </repository>
   </repositories>
   
   <dependencies>
       <dependency>
           <groupId>org.spigotmc</groupId>
           <artifactId>spigot-api</artifactId>
           <version>SPIGOTVERSION</version>
           <scope>provided</scope>
       </dependency>
       <dependency>
            <groupId>bigbade.enchantmenttokens</groupId>
            <artifactId>EnchantmentTokens</artifactId>
            <version>ENCHANTMENTVERSION</version>
            <scope>provided</scope>
       </dependency>
   </dependencies>
   
   <properties>
       <maven.compiler.source>1.8</maven.compiler.source>
       <maven.compiler.target>1.8</maven.compiler.target>
       <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
   </properties>
```

If you don't use Maven, feel free to just import the jar.
Make sure you install the jar, use:
mvn install -Dfile=(path)