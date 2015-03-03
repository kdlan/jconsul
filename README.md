# JConsul

JConsul is a builder style Java client for [Consul](https://github.com/hashicorp/consul), using [async-http-client](https://github.com/AsyncHttpClient/async-http-client) and Netty for http request and will have limited dependencies of 3rd libs.

JConsul will have a recipes module contains some utils such as Spring bean auto reload support when configuration or service registration change.


JConsul support consul 0.5.x now.

## Build

JConsul need jdk 1.7+ to build

```
git checkout v0.1.0
mvn clean install -Dmaven.test.skip=true
```

Maven:
```xml
<dependencies>
    <dependency>
        <groupId>com.loukou.jconsul</groupId>
        <artifactId>jconsul-client</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

## Usage

### JConsul Client

Note: Only JConsul instance is thread safe, all the other instances have no thread safe guarantee

Agent:

```java
JConsul jconsul=new JConsul();

Agent agent=jconsul.agent().self();
Map<String, Service> services = jconsul.agent().services();
Map<String, HealthCheck> checks = jconsul.agent().checks();
List<Member> list = jconsul.agent().members();


//check register
RegisterStatus checkStatus = jconsul.agent().registerCheck("test").id("test-check").notes("test").ttl(10);

checkStatus.pass();
checkStatus.fail("fail reason");

checkStatus.deregister();

//service register
RegisterStatus serviceStatus = jconsul.agent().registerService("test-service").port(1234).tags("test","example").ttl(10);

serviceStatus.pass();
serviceStatus.fail("reason");
serviceStatus.deregister();

```


Key Value:

```java

//get
JConsulResponse<Value> response = jconsul.keyValue().get("testKey").poll(1, 1)
                .consistency(ConsistencyMode.STALE).response();
Optional<Value> valueOptional = jconsul.keyValue().get("testKey").value();

Optional<String> rawValue = jconsul.keyValue().get("testKey").raw().value();

jconsul.keyValue().get("testKey").poll(1, index).async(new JConsulResponseCallback<Value>() {

    @Override
    public void onFailure(Throwable throwable) {
        LOGGER.warn(throwable.getMessage(),throwable);
    }

    @Override
    public void onComplete(JConsulResponse<Value> consulResponse) {
        //do something
        
        //async call again
        jconsul.keyValue().get(key).poll(1, consulResponse.getIndex()).async(this);
});

//keys
JConsulResponse<List<String>> response = jconsul.keyValue().get("test").keys().response();

//put
boolean success=jconsul.keyValue().put("test/key").flags(123).value("test value");

//delete
boolean result=jconsul.keyValue().delete("test/abc").cas(index).execute();
```


Catalog:

```java
JConsulResponse<List<Node>> nodeListResponse = jconsul.catalog().nodes().response();

Optional<CatalogNode> nodeOptional = jconsul.catalog().node("nodename").value();

jconsul.catalog().register("testNode", "127.0.0.1").service("testServiceId", "testService").port(8080).check("testServiceCheckId", "testServiceCheck")
                .forService("testNode", "testServiceId").status("passing").execute();
                
                

```

Health:

```java
List<HealthCheck> list = jconsul.health().node("testNode").value().get();

List<HealthCheck> list = jconsul.health().checks("serviceName").value().get();

List<HealthService> list = jconsul.health().service("serviceName").value().get();

List<HealthCheck> list=jconsul.health().status("passing").value().get();
```

Session:
```
Optional<Session> sessionOpional = jconsul.session().info(sessionId).value();

SessionStatus session = jconsul.session().create().name("testSession").behavior("delete").lockDelay(1000).ttl(20).node(nodename).execute();

Session s=session.renew();
session.destroy();

List<Session> sessionList = jconsul.session().list().value();
List<Session> nodeSessions = jconsul.session().node(nodename).value();


```


Status

```java
String leader=jconsul.status().leader();
List<String> peers=jconsul.status().peers();
```