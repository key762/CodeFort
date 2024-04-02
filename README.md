<h1 align="center" style="text-align:center;">
<img src="https://gitee.com/anoraks/codefort/raw/master/codefort.png" width="250" height="250" />
<br />
CodeFort v1.0.5
</h1>
<p align="center">
	<strong>Java 字节码加密插件，配置简单、多种方式、远程管理！</strong>
</p>
<p align="center">
	<a href="https://gitee.com/anoraks/codefort">https://gitee.com/anoraks/codefort</a>
</p>

<p align="center">
    <a target="_blank" href="https://central.sonatype.com/search?q=io.gitee.anoraks%3Acodefort-plugin">
       <img src="https://img.shields.io/badge/Maven%20Central-v1.0.5-blue
" alt="Maven" />
    </a>
    <a target="_blank" href="LICENSE">
		<img src="https://img.shields.io/:AGPL-3.0-blue.svg" alt="AGPL-3.0" />
	</a>
    <a target="_blank" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8-green.svg" alt="jdk-8" />
	</a>
    <br />
    <a target="_blank" href='https://gitee.com/anoraks/codefort/stargazers'>
		<img src='https://gitee.com/anoraks/codefort/badge/star.svg' alt='gitee star'/>
	</a>
	<img src="https://img.shields.io/badge/QQ-2940397985-orange"/></a>
	</a>
</p>
<br/>

-------------------------------------------------------------------------------

## 📚简介

`CodeFort`是一个用于class加密的**Maven插件**，通过诸多实用的策略配置，旨在帮助开发者简便，快捷的保护代码安全。

### 🎁CodeFort名称的由来

CodeFort = Code + Fort，“Code”是代码的表示，Fort表示堡垒。CodeFort寓意保卫代码安全。
## 🛠️包含组件
一个字节码加密的Maven插件，项目以下组件：

| 模块                |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| codefort-core         |     核心代码模块                                              |
| codefort-plugin |     加密插件模块                                                |
| codefort-web       |     远程控制模块                                                                     |
-------------------------------------------------------------------------------

## 📦安装

### 🍊Maven
在项目的pom.xml的dependencies中加入以下内容:

```xml
<plugin>
    <groupId>io.gitee.anoraks</groupId>
    <artifactId>codefort-plugin</artifactId>
    <version>1.0.5</version>
    <configuration>
        <password>123456</password>
        <startTime>2017-03-01 22:33:23</startTime>
        <endTime>2027-03-01 22:33:23</endTime>
        <biosMark>FVFKD2YM1WFY</biosMark>
        <availableTime>1000</availableTime>
        <libs>license-client-2.0.jar</libs>
        <packages>host.skiree.springdemo</packages>
        <excludes>host.skiree.springdemo.excludes</excludes>
        <host>127.0.0.1</host>
        <port>7007</port>
        <explain>示例项目</explain>
        <isDebug>true</isDebug>
        <rsaPublicKey>RSA公钥</rsaPublicKey>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>CodeFort</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
-------------------------------------------------------------------------------

### 🐞提供bug反馈或建议

提交问题反馈请说明正在使用的JDK版本呢、CodeFort版本和相关依赖库版本。

- [Gitee issue](https://gitee.com/anoraks/codefort/issues)