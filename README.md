<h1>KWAF</h1>
<h3 style="margin-top:-15px;">Kotlin Web Application Framework</h3>
<hr>
KWAF is meant to be a lightweight replacement for Springboot. It is currently entirely written in Kotlin with cross-compatibility and interoperability in mind.
<br>
<br>
The main benefit intended by KWAf is to remove the bloat of other frameworks with an easy to learn syntax. KWAF effectively prevents misconfigurations by only shipping with the necessary tools to get started. There's no point in being vulnerable from features you don't even want to use!
<hr>
<h2>Getting Started</h2>
<hr>
KWAF uses Jitpack for maven and gradle and can be found at the following site:
<a href="https://jitpack.io/#SpencerNold/KWAF">KWAF JitPack</a>

<h3>Gradle:</h3>
First, add JitPack to your project in `settings.gradle.kts`:

``` kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

```
Finally, add the dependency to `build.gradle.kts`:

``` kotlin
dependencies {
    implementation("com.github.SpencerNold:KWAF:-SNAPSHOT")
}
```

<h3>Maven:</h3>
First, add JotPack as a repository in `pom.xml`:

``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Finally, add the dependency to `pom.xml`:

``` xml
<dependency>
    <groupId>com.github.SpencerNold</groupId>
    <artifactId>KWAF</artifactId>
    <version>Tag</version>
</dependency>
```

<hr>
<h2>Your first Web Application</h2>
<hr>
<h3>Controller:</h3>
Controller classes handle inbound and outbound communication from the HttpServer, and use annotated methods to define routes.

``` kotlin
// Example
@Service.Controller
class ControllerExample {

    // Would display index.html from the /resources directory
    // at the root page.
    @Route.File(path = "/")
    fun rootRoute(): InputStream? {
        return Resource.get("index.html")
    }
    
    // This method would be called if a post request is sent to this
    // defined path, the body decoded depending on the body type and
    // the type of the object in the method
    @Route(Http.Method.POST, "/post_request", input = true)
    fun postRequestRoute(request: JsonElement) {
        
    }
}
```

<h3>Running the Server</h3>
To start the Web Application, similar code should be run:

``` kotlin
fun main() {
    val server = WebServer.Builder(Protocol.HTTP, 80)
        .executor(Executors.newCachedThreadPool())
        .services(ControllerExample::class.java)
        .build()
    server.start()
}
```