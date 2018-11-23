Spring Boot精要

Spring将很多魔法带入了Spring应用程序的开发之中，其中最重要的是以下四个核心：
自动配置：针对很多Spring应用程序常见的应用功能，Spring Boot能自动提供相关配置
起步依赖：告诉Spring Boot需要什么功能，它就能引入需要的库。
命令行界面：这是Spring Boot的可选特性，借此你只需写代码就能完成完整的应用程序，无需传统项目构建。
Actuator：让你能够深入运行中的Spring Boot应用程序，一套究竟。	

#####搭建方法

1. **idea**:File->new->project,选择Spring Initializr，选择web模块(最基础的web应用)，![](https://i.imgur.com/DmVgsub.png)或网页([https://start.spring.io](https://start.spring.io))![](https://i.imgur.com/QTFONXE.png)
    
     创建后项目基本没有什么代码，只有下面的代码结构
 >  - pom.xml：Maven构建说明文件。
 >  - Chapter1Application.java：一个带有main()方法的类，用于启动应用程序（关键）。
 >  - Chapter1ApplicationTests.java：一个空的Junit测试类，它加载了一个使用Spring Boot字典配置功能的Spring应用程序上下文。
>  - application.properties：一个空的properties文件，你可以根据需要添加配置属性。


#####Pom文件解析
` 

`
    