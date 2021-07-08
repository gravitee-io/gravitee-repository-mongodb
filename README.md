# ⚠️ DEPRECATED

This repository is no longer active, all the sources have been moved to a [new monorepo](https://github.com/gravitee-io/gravitee-api-management/tree/master/gravitee-apim-repository/gravitee-apim-repository-mongodb).

The new repository will be become the single GitHub repository for everything related to Gravitee.io API Management.

## Gravitee Mongo Repository

Mongo repository based on MongoDB

### Requirements

The minimum requirements are:
* Maven3
* Jdk8

To use Gravitee.io snapshots, you need to declare the following repository in your maven settings:
`https://oss.sonatype.org/content/repositories/snapshots`

### Building

```shell
git clone https://github.com/gravitee-io/gravitee-repository-mongodb.git
cd gravitee-repository-mongodb
mvn clean package
```

### Installing

Unzip the gravitee-repository-mongodb-x.y.z-SNAPSHOT.zip in the gravitee home directory.
 


### Configuration

repository.mongodb options : 

| Parameter                                        |   default  |
| ------------------------------------------------ | ---------: |
| host                                             |  localhost |
| port                                             |      9200  |
| username                                         |            |
| password                                         |            |
| connectionPerHost                                |            |
| connectTimeout                                   |            |
| writeConcern                                     |      1     |
| wtimeout                                         |    0       |
| journal                                          |            |
| maxWaitTime                                      |            |
| socketTimeout                                    |            |
| socketKeepAlive                                  |            |
| maxConnectionLifeTime                            |            |
| maxConnectionIdleTime                            |            |
| minHeartbeatFrequency                            |            |
| description                                      |            |
| heartbeatConnectTimeout                          |            |
| heartbeatFrequency 	                           |            |
| heartbeatsocketTimeout                           |            |
| localThreshold 	                               |            |
| minConnectionsPerHost                            |            |
| sslEnabled 		                               |            |
| threadsAllowedToBlockForConnectionMultiplier     |            |
| cursorFinalizerEnabled                           |            |
| keystorePassword                                 |            |
| keystore                                         |            |
| keyPassword                                      |            |

NB: writeConcern possible value are 1,2,3... (the number of node) or 'majority' 