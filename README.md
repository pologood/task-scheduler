## Introduction
基于SpringBoot+Quartz的分布式任务调度系统，支持多种消息通道(channel)自由灵活配置，架构设计图：

## Features
1. 调度中心使用分布式quartz，支持集群模式，保证HA和负载均衡，有效避免单点。
2. redis、mq channel支持集群模式，避免单点。
3. 调度中心有可视化管理控制台，方便的对每个任务进行管理，并能清晰的看到每个任务的执行细节、失败原因、报警人等。
4. 如果任务失败，系统自动重试指定次数，并报警相关责任人（短信、邮件等）。另外，可以手动的在控制台触发任务，但要保证任务的幂等性。
5. task实例和调度中心通信是通过channel，系统支持redis、mq、http三种channel，可灵活配置，建议使用redis channel。
6. 如果task实例集群部署，那么同一任务是互斥的，即同一任务只会在集群中的一个实例上执行，调度中心也只会保留一个任务。
7. 调度中心重要的性能指标图形化，如平均响应时间、任务积压情况等。另外，也提供channel的监控，如redis性能监控、redis消息情况监控等。
8. 调度中心支持权限验证，只有授权的人才可进入调度中心控制台。
9. task实例要及时上报自己的状态给调度中心，避免两者之间的异步通信带来问题，如果task实例在指定时间没有上报自己的状态，调度中心会重发任务到task实例。
10. task实例使用线程池技术执行任务，提升性能。

## Quick Start

1. 项目使用maven构建，下载后编译：
```java
mvn clean install -Dmaven.test.skip=true
```
2. task实例：
    * 引入maven依赖：
    ```java
    <dependency>
        <groupId>com.games.infrastructure</groupId>
        <artifactId>scheduler-client</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    * 在配置文件中加入group属性：
    ```java
    spring.quartz.group=demo
    ```
    * 默认使用redis channel通信，配置redis connection，要和调度中心的redis配置一致：
    ```java
    spring.redis.address=127.0.0.1:6379
    spring.redis.password:
    spring.redis.timeout=10000
    spring.redis.jobChannel=quartz_job_channel
    spring.redis.jobStatusChannel=quartz_job_status_channel
    ```
    * 实现```com.games.job.client.job.Job```接口，并在类上加```@Quartz(jobName = "myJob",cronExpression = "0 0/1 * * * ?",retryCount = 4)```
3. 调度中心：
    * 默认使用redis channel通信，配置redis connection，要和task实例的redis配置一致：
    ```java
    spring.redis.address=127.0.0.1:6379
    spring.redis.password:
    spring.redis.timeout=10000
    spring.redis.jobChannel=quartz_job_channel
    spring.redis.jobStatusChannel=quartz_job_status_channel
    ```
    * 部署并启动调度中心

## Wiki & Usage
调度中心详细设计及使用手册参考wiki。https://github.com/gitHubLjh/jobCenter/wiki

## TODO
1. 支持任务间的依赖关系、执行顺序设定。
2. task实例和调度中心直接通信，去除channel通信带来的异步问题。
3. 调度中心ACK机制、任务去重。

## FAQ
由于目前task实例和调度中心是通过channel异步通信的，那么task实例上报任务是否成功也就无法知晓了。

解决方案：增加调度中心的确认机制，一旦调度中心收到task实例上报的任务，就要给出确认反馈，
如果task实例在指定时间内没有收到确认反馈，就在上报任务，同时调度中心也要对相同的任务去重，以免重复上报。

## License
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)

## Contributors
name:liujianhui&nbsp;&nbsp;&nbsp;&nbsp;
qq:583597394&nbsp;&nbsp;&nbsp;&nbsp;
email:583597394qq.com
