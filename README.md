# Gryo介绍
https://plugins.jetbrains.com/plugin/20795-gyro

![11673191821_ pic](https://user-images.githubusercontent.com/26487411/211205023-e5c4bac4-e189-4ece-aa28-5cef01991f4f.jpg)

它是一个IDEA插件，安装之后，用Gyro Debug运行你的单测，第一次会正常启动Spring容器，第二次之后再次运行会复用之前启动过的Spring容器。
如果你安装了Jrebel热部署插件，Gyro自动使用其热部署能力。

![51673192009_ pic](https://user-images.githubusercontent.com/26487411/211205172-89f1aa66-3a4b-41e9-bff1-9be70a26cb48.jpg)
认准Gyro Debug的黄色小蜜蜂，无需你为此改动任何代码，用它来执行你的单测，你将开启一段美妙的体验。

视频演示：
https://youtu.be/2Kl0yGgz5f0

# 原理
**原理其实就一句话：想办法复用第一次启动的进程。**

在同一个中运行@SpringBootTest，Spring本身是做了缓存的，下面是我们在Idea中Debug运行@SpringBootTest的原理（以Junit4举例）：

```java
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {Application.class})
public abstract class TestDemo {
  
  @Test 
  public void test() {
    System.out.println("test");
  }
}
```

运行Debug的时候，Idea会启动一个进程，这个进程中通过反射获取注解@RunWith对应的类，然后启动Spring容器。
Spring在启动的时候，在TestContextManager中缓存了ApplicationContext，所以只要进程没有结束，再次运行的时候，Spring会从cache中获取Spring容器，并不会重新启动。
如下图所示：

![81673192253_ pic](https://user-images.githubusercontent.com/26487411/211205450-03d82533-6264-4d2e-8880-cdc5540eda5c.jpg)

这里问题在于Idea每次Debug执行完成之后，都会把进程结束掉，进程都结束了，Spring的TextContextManager中的cache当然也就回收掉了。
**所以其实只要复用第一次启动的进程，就能复用Spring容器！**

**Gyro Debug启动流程：**
Gyro自己写了一个GyroDebugRunner，通过本地文件通信的方式，和第一次启动的进程通信，把运行的参数写到本地文件中，第一次启动的进程监听这个文件的变化，读取其中的信息来执行。
第一次启动的时候，执行的是GyroTestStarter类，这个类会在执行完JunitStarter之后，会用While循环block住，监听本地文件的变化。

![21673191822_ pic](https://user-images.githubusercontent.com/26487411/211205034-84d96717-9ca3-455a-8e53-3afbec237430.jpg)
