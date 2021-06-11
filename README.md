# android-ez-componentization

## Project structure:
## 项目结构

### app_:
app link up components
<u>串连起所需的组件</u>
### component_:
component of app
<u>组件</u>
### lib_:
DI frame of services
<u>@Service 的框架代码</u>
### plugin_:
scan all class to generate the routing table
<u>扫描全部class 找到@Service 所标注的类 集中放到routing table中 生成路由表</u>
### service_:
service of components
<u>组件的服务(如：图片加载服务)</u>
