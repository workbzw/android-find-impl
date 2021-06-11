# android-ez-componentization

### Project structure:
### 项目结构

##### app_:app link up components 串连起所需的组件
##### component_ component of app 组件
##### lib_ DI frame of services @Service 的框架代码
##### plugin_ scan all class to generate the routing table 扫描全部class 找到@Service 所标注的类 集中放到routing table中 生成路由表
##### service_ service of components 组件的服务(如：图片加载服务)
