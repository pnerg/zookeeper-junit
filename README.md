[![Build Status](https://travis-ci.org/pnerg/zookeeper-junit.svg?branch=master)](https://travis-ci.org/pnerg/zookeeper-junit)  [![codecov.io](https://codecov.io/github/pnerg/zookeeper-junit/coverage.svg?branch=master)](https://codecov.io/github/pnerg/zookeeper-junit?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dmonix.junit/zookeeper-junit/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/org.dmonix.junit/zookeeper-junit)  [![Javadoc](http://javadoc-badge.appspot.com/org.dmonix.junit/zookeeper-junit.svg?label=javadoc)](http://javadoc-badge.appspot.com/org.dmonix.junit/zookeeper-junit) 
# ZooKeeper JUnit
Utility for managing a ZooKeeper server during JUnit testing.  
There are other implementations out there. Some are baked into larger frameworks (such as Curator), others are standalone.  
I've at least found that all I've looked at have been cumbersome to work with or required an abundance of dependencies which I find undesirable.

## What's this?
This project implements a simple to use and in-process ZooKeeper starter.   
That is with a minimal amount of code you can control the life-cycle of a ZooKeeper server to use for your unit testing of applications needing a ZooKeper server.  
The project provides features such as:
* Effortless/automatic port allocation  
  Guarantees that there will never be port collisions on the port the server listens to.
* Efortless/automatic data file management  
  Guarantees unique data file paths to avoid collision.  
  Also provides means to delete the data files once the test is finished
* Full life-cycle management of the server  
  Includes start/stop and re-start of a server.  
  Allows for test cases where a server goes offline or re-starts.
* Utility operations to ease management of data  
  Provides means to assert existence of data, delete single or recursive paths
* Specialized interfaces allowing for mix-in compositions with more rich assert operations  
  

## The full manual
Refer to the [Wiki](https://github.com/pnerg/zookeeper-junit/wiki) for a full description on this project and how to use its features.

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
