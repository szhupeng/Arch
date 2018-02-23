Arch [![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=14)
============

![Logo](http://ob0r3vf26.bkt.clouddn.com/arch-logo.png)

Arch is a flexible base frame to develop an app. It mainly included:

* MVP
    * Repository
    * BaseView
    * BasePresenter
* BaseActivity
* BaseToolbarActivity
* BaseFragment
* BaseListFragment
* BaseDialog

You just need to simply inherit them and implement some methods will be able to quickly complete some work.

Download
--------
Download [the latest JAR][2] or grab via Maven:

First:

```xml
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>

    <profiles>
        <profile>
            <repositories>
                <repository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-zhupeng-maven</id>
                    <name>bintray</name>
                    <url>https://dl.bintray.com/zhupeng/maven</url>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                    <id>bintray-zhupeng-maven</id>
                    <name>bintray-plugins</name>
                    <url>https://dl.bintray.com/zhupeng/maven</url>
                </pluginRepository>
            </pluginRepositories>
            <id>bintray</id>
        </profile>
    </profiles>
    <activeProfiles>
        <activeProfile>bintray</activeProfile>
    </activeProfiles>
</settings>
```
Then:

```xml
<dependency>
  <groupId>space.zhupeng.arch</groupId>
  <artifactId>arch</artifactId>
  <version>2.0</version>
  <type>pom</type>
</dependency>
```
or Gradle:

First:

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/zhupeng/maven"
    }
}
```

Then:

```groovy
compile 'space.zhupeng.arch:arch:2.0'
```
Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

Arch requires at minimum Java 7 or Android 4.0.

License
-------

Arch is MIT licensed.
