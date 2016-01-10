[![Build Status](https://travis-ci.org/pnerg/zookeeper-junit.svg?branch=master)](https://travis-ci.org/pnerg/zookeeper-junit)  [![codecov.io](https://codecov.io/github/pnerg/zookeeper-junit/coverage.svg?branch=master)](https://codecov.io/github/pnerg/zookeeper-junit?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dmonix.junit/zookeeper-junit/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/org.dmonix.junit/zookeeper-junit)  [![Javadoc](http://javadoc-badge.appspot.com/org.dmonix.junit/zookeeper-junit.svg?label=javadoc)](http://javadoc-badge.appspot.com/org.dmonix.junit/zookeeper-junit) 
# ZooKeeper JUnit
Utility for managing a ZooKeeper server during JUnit testing.  
There are other implementations out there. Some are baked into larger frameworks (such as Curator), others are standalone.  
I've at least found that all I've looked at have been cumbersome to work with or required an abundance oof dependencies which I find undesirable.

## What's this?
This project implements a simple to use and in-process ZooKeeper starter.   
That is with a minimal amount of code you can control the life-cycle of a ZooKeeper server to use for your unit testing of applications needing a ZooKeper server.

## The factory
The _ZKFactory_ is the starting point.  
It's used to create instances of the ZooKeeper server.  
The factory builds on the builder pattern principle, that is you provide only the set of parameters/configuration you deem neccesary.
E.g.
```java
ZKInstance instance = ZKFactory.apply().withPort(6969).create();
```

## The instance
The _ZKInstance_ acts as a representation of a ZooKeeper server and its life-cycle.  
It's used to start/stop/destroy and possible re-start a server.  

### Starting the server
The _start_ method starts the ZooKeeper server using the properties set from the factory.  
The method returns a [Future](https://github.com/pnerg/java-scala-util/wiki/Future) which will be completed when the server has started.
```java
Future<Unit> future = instance.start();
```
### Stopping the server
The _stop_ method stops the running ZooKeeper server.  
Just as the _start_ method this returns a [Future](https://github.com/pnerg/java-scala-util/wiki/Future) which will be completed once the server is stopped.
```java
Future<Unit> future = instance.stop();
```
Should it be desired the server can be re-started using the _start_ method.  
Any previously stored data is retained.

### Destroying the server
The _destroy_ method both stops the server and removes all data files.  
This means that if the _start_ method is invoked after _destroy_ then a blank ZooKeeper instance is started.  
Just as the _start_/_stop_ methods this returns a [Future](https://github.com/pnerg/java-scala-util/wiki/Future) which will be completed once the server is stopped.

### Accessing server properties
The _ZKInstance_ provides access to certain runtime features such as:  
Accessing the port the server listens to:
```java
ZKInstance instance = ...
Option<Integer> port = instance.port();
```
Or the connect string used to connect to the server: 
```java
ZKInstance instance = ...
Option<String> connectString = instance.connectString();
```
The [Option](https://github.com/pnerg/java-scala-util/wiki/Option) interface represents an optional value which only is set if the server is started.

## References
Refer to the project [java-scala-util](https://github.com/pnerg/java-scala-util) for details and examples on how to work with features such as [Option](https://github.com/pnerg/java-scala-util/wiki/Option)/[Try](https://github.com/pnerg/java-scala-util/wiki/Try)/[Future](https://github.com/pnerg/java-scala-util/wiki/Future)

## Maven Central
```xml
<dependency>
    <groupId>org.dmonix.junit</groupId>
    <artifactId>zookeeper-junit</artifactId>
    <version>1.0</version>
</dependency>
```
## LICENSE

Copyright 2016 Peter Nerg.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
