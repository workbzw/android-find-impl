# android-component 安卓组件化 - apt + javapoet + gradle plugin
### architecture
- @AutoService
- AbstractProcessor -> generate global Router map (IService->ServiceImpl) 
- IService -> api
- @Service -> mark entity to generate component's local Router map
- ServiceImpl -> entity of IService
- find IService get ServiceImpl 

