# AndroidStudio上传Androidlibrary到Jcentre
## 打包Jar相关事项
*    Android studio 只能对libary进行打包处理
*    相关操作

       1.对项目执行Make project,执行操作后我们可以在build目录下看到相关Class文件,在generated下看到AIDL文件生成的Class文件

       ![image](http://e.hiphotos.baidu.com/image/pic/item/3801213fb80e7bec00791443262eb9389a506bf4.jpg)
       ![image](http://c.hiphotos.baidu.com/image/pic/item/0b46f21fbe096b63ec0548f205338744ebf8ac77.jpg)

       2.配置library的Gradle文件

       ![image](http://f.hiphotos.baidu.com/image/pic/item/b2de9c82d158ccbf11c9a39810d8bc3eb135411e.jpg)

     ```
       task makeJar(type: org.gradle.api.tasks.bundling.Jar) {
       /**
     * 指定生成的jar名
     */

    baseName 'shopcart'
    /**
     * 从哪里打包class文件
     */
    from('build/intermediates/classes/debug/com/wz/cartlib')
    /**
     * 打包AIDL生成文件
     */
    from('build/generated/source/aidl/debug/com.wz.cartlib/')
    /**
     * 打包到jar后的目录结构
     */
    into('com/wz/cartlib/')
    /**
     * 去掉不需要打包的目录和文件
     */
    exclude('BuildConfig.class', 'R.class')
    /**
     * 去掉R$开头的文件
     */
    exclude { it.name.startsWith('R$'); }
    }

    /**
    在终端执行生成JAR包 gradlew makeJar
    *
     */
    makeJar.dependsOn(clearJar,build)
     ```
    3.打开Android studio终端执行gradlew makeJar，等待出现BUILD SUCCESSFUL之后代表打包成功

    4.打包成功后会在libary的build/libs目录下生成jar包

* 带有资源文件.aar文件

    1.将aar包复制到lib目录下

    2.配置工程app的build.gradle文件中加入声明
  ```
     repositories {
    flatDir {
    dirs 'libs'
    }
    compile(name:'httputils-debug', ext:'aar')
  ```
    3.在其他app中使用该aar文件时需要在该app的build.gradle文件的dependencies节点进行aar库build.gradle文件中依赖声明

* 参考文章

    http://blog.csdn.net/hard_working1/article/details/52639776

## 上传library到Jcentre

*   Android Studio 只能上传library到jcentre
*   注册Jcentre相关步骤

      1.Jcentre官网注册用户 https://bintray.com 个人版:https://bintray.com/signup/oss

      ![image](http://img.blog.csdn.net/20161118121817416)

      2.登录Jcentre个人中心,在Originsation下创建Repository

      ![image](http://img.blog.csdn.net/20161118121853368)
      ![image](http://img.blog.csdn.net/20161118121924869)

      Repository类似一个大的项目,在这个项目下可以容纳多个子项目,子项目可以进行版本控制

      注册成功后可以看到Repositroy主界面

      3.请牢记以下账号相关信息,在下面的配置中需要到:
    * Organization Id（组织ID）
    * Repository Name（仓库名称）
    * Bintray User（Bintray账号的用户名）
    * Bintray API Key（Bintray账号的API KEY）

*   上传Android studio Project lib  到Jcentre步骤

      1.配置Android项目下Gradle文件

    ```
      buildscript {
      repositories {
          jcenter()
      }
      dependencies {
          classpath 'com.android.tools.build:gradle:2.2.3'
          classpath 'com.github.dcendents:android-maven-gradle-plugin:1.4.1' //用于打包Maven所需文件
          classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.6' //用于上传Maven生成的文件到Bintray

          // NOTE: Do not place your application dependencies here; they belong
          // in the individual module build.gradle files
       }
      }
    ```
      2.在Module下创建bintray.properties文件配置Bintray相关信息

    ```
      #配置bintray账号相关信息
      #bintray用户名,不是登陆邮箱,是个人中心右上角显示的名字
      bintray.user=heyzhuyue
      #bintray的ApiKey
      bintray.apiKey=aaadf51f8d1c366b97e633a59c1f34f989d7dcc8
      #bintray的Organization Id(组织Id)
      bintray.organizationId=soup
      #配置开发者信息
      #昵称
      developer.id=heyzhuyue
      #姓名
      developer.name=heyzhuyue
      #邮箱
      developer.email=zy1301529626@gmail.com
    ```

      3.在Module下创建project.properties文件配置Project相关信息

    ```
      #project
      #仓库名称，就是在bintray官网建立的仓库的名称
      project.repositoryName=maven
      #项目名称
      project.name=shopcartlib
      #项目组id
      project.groupId=com.wz.shopcartlib
      #项目id,一般同project.name
      project.artifactId=shopcartlib
      #打包类型
      project.packaging=aar
      #项目官方网站地址
      project.siteUrl=https://github.com/zy1301529626/os_shopcart_lib
      #项目git地址
      project.gitUrlhttps://github.com/zy1301529626/os_shopcart_lib
      #生成的javadoc名称
      javadoc.name=shopcartlib
    ```
      4.在Moudle下Gradle文件下配置打包上传相关内容

    ```
      apply plugin: 'com.android.library'
      apply plugin: 'com.github.dcendents.android-maven'
      apply plugin: 'com.jfrog.bintray'

      android {
      compileSdkVersion 19
      buildToolsVersion "25.0.2"

      defaultConfig {
          minSdkVersion 15
          targetSdkVersion 25
          versionCode 1
          versionName "1.0"

          testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

      }
      buildTypes {
          release {
              minifyEnabled false
              proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
          }
      }
      }

      dependencies {
      compile fileTree(dir: 'libs', include: ['*.jar'])
      androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
          exclude group: 'com.android.support', module: 'support-annotations'
      })
      compile 'com.android.support:appcompat-v7:25.1.0'
      testCompile 'junit:junit:4.12'
      }

    /**
     * 加载bintray属性文件
     */
    Properties properties = new Properties()
    File localPropertiesFile = project.file("bintray.properties");
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.newDataInputStream())
    }
    /**
     * 加载项目属性文件
     */
    File projectPropertiesFile = project.file("project.properties");
    if (projectPropertiesFile.exists()) {
        properties.load(projectPropertiesFile.newDataInputStream())
    }

    /**
     * 读取项目属性
     */
    def projectRepositoryName = properties.getProperty("project.repositoryName") //仓库名称
    def projectName = properties.getProperty("project.name")//项目名称
    def projectGroupId = properties.getProperty("project.groupId")//项目Id
    def projectArtifactId = properties.getProperty("project.artifactId")//一般与项目名称相同
    def projectVersionName = android.defaultConfig.versionName //项目版本号
    def projectPackaging = properties.getProperty("project.packaging") //项目打包类型
    def projectSiteUrl = properties.getProperty("project.siteUrl") //项目地址
    def projectGitUrl = properties.getProperty("project.gitUrl")  //项目git地址

    /**
     * 读取bintray属性
     */
    def bintrayUser = properties.getProperty("bintray.user")  //bintray用户名
    def bintrayApikey = properties.getProperty("bintray.apiKey") //bintray Api Key
    def bintrayOrganizationId = properties.getProperty("bintray.organizationId") //组织Id
    def developerId = properties.getProperty("developer.id") //开发者Id
    def developerName = properties.getProperty("developer.name") //开发者名称
    def developerEmail = properties.getProperty("developer.email") //开发者email地址
    def javadocName = properties.getProperty("javadoc.name") //javadoc名字

    /**
     * 这句代码一定要加否则会出现如下错误
     * Could not upload to 'https://api.bintray.com/content/coolcode/maven/LibUiBase/1.0.0/CommonLibrary/LibUiBase/1.0.0/LibUiBase-1.0.0.pom': HTTP/1.1 400 Bad Request [
     message:Unable to upload files: Maven group, artifact or version defined in the pom file do not match the file path 'CommonLibrary/LibUiBase/1.0.0/LibUiBase-1.0.0.p
     om']
     */
    group = projectGroupId

    /**
     * 配置生成POM.xml文件的参数
     */
    install {
        repositories.mavenInstaller {
            pom {
                project {
                    name projectName //项目名
                    groupId projectGroupId //项目Id(一般为唯一包名)
                    artifactId projectArtifactId //一般与项目名相同
                    version projectVersionName //项目版本号
                    packaging projectPackaging //打包类型
                    url projectSiteUrl  //项目地址
                    licenses {
                        license {
                            name 'The Apache Software License, Version 2.0'
                            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                        }
                    }
                    developers {
                        developer {
                            id developerId //开发者id
                            name developerName //开发者名字
                            email developerEmail //开发者email地址
                        }
                    }
                    scm {
                        connection projectGitUrl
                        developerConnection projectGitUrl
                        url projectSiteUrl
                    }
                }
            }
        }
    }

    /**
     * 生成sources.jar
     */
    task sourcesJar(type: Jar) {
        from android.sourceSets.main.java.srcDirs
        classifier = 'sources'
    }

    task javadoc(type: Javadoc) {
        source = android.sourceSets.main.java.srcDirs
        classpath += project.files(android.getBootClasspath().join(File.pathSeparator))
    }

    /**
     * 生成javadoc.jar
     */
    task javadocJar(type: Jar, dependsOn: javadoc) {
        classifier = 'javadoc'
        from javadoc.destinationDir
    }

    artifacts {
        archives javadocJar
        archives sourcesJar
    }

    /**
     * javadoc的配置
     */
    javadoc {
        options {
            encoding "UTF-8"
            charSet 'UTF-8'
            author true
            version projectVersionName
            links "http://docs.oracle.com/javase/7/docs/api"
            title javadocName
        }
    }

    /**
     * userOrg为bintray账号信息里面的Organization Id
     * repo为你创建的仓库名称
     * 如果上述两个字段写错了，则会出现下面类似的错误
     * Could not create package 'huangxuanheng/maven/fragmentstack': HTTP/1.1 404 Not Found [message:Repo 'maven' was not found]
     *
     */
    bintray {
        user = bintrayUser
        key = bintrayApikey
        configurations = ['archives']
        pkg {
            userOrg = bintrayOrganizationId
            repo = projectRepositoryName
            name = projectName
            websiteUrl = projectSiteUrl
            vcsUrl = projectGitUrl
            licenses = ["Apache-2.0"]
            publish = true
        }
    }
    ​```

    4.在Terminal窗口下收入如下指令上传到Bintray
    ​```xml
    gradlew install
    gradlew bintrayUpload
    ​```
* 参考文章

  1.http://blog.csdn.net/huang_cai_yuan/article/details/53215131

  2.http://blog.csdn.net/lmj623565791/article/details/51148825


