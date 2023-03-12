## 어플리케이션 띄울때 실행과정

1) 최초실행
   ![img.png](img.png)

2) runApplication함수의 실제코드 -> SpringApplicationExtensions.kt
   ![img_1.png](img_1.png)

3) SpringApplication.run 호출 부 -> SpringApplication.java

```java
    /**
 * Static helper that can be used to run a {@link SpringApplication} from the
 * specified source using default settings.
 * @param primarySource the primary source to load
 * @param args the application arguments (usually passed from a Java main method)
 * @return the running {@link ApplicationContext}
 */
public static ConfigurableApplicationContext run(Class<?> primarySource,String...args){
        return run(new Class<?>[]{primarySource},args);
        }

/**
 * Static helper that can be used to run a {@link SpringApplication} from the
 * specified sources using default settings and user supplied arguments.
 * @param primarySources the primary sources to load
 * @param args the application arguments (usually passed from a Java main method)
 * @return the running {@link ApplicationContext}
 */
public static ConfigurableApplicationContext run(Class<?>[]primarySources,String[]args){
        return new SpringApplication(primarySources).run(args);
        }
```

- 여기서 `SpringApplication` 초기화 & run

5) SpringApplication
   **run**

- 여기서 리스너도 초기화 -> getRunListeners 함수
    - SpringApplicationRunListeners 에 EventPublishingRunListeners이 들어있음
- 리스너에 어플리케이션서버 정보를 연결? `listeners.starting(bootstrapContext, this.mainApplicationClass)`
- context 초기화 -> createApplicationContext()
    - applicationContextFactory (DEFAULT) 에서 생성
        - webApplicationType이 SERVLET이라 ApplicationFactory는 `AnnotationConfigServletWebServerApplicationContext`
        - ![img_2.png](img_2.png)
        - 따라서 SpringApplication의 applicationContextFactory필드는 AnnotationConfigServletWebServerApplicationContext 타입이 되는
          것
        - ..? 여기서 놓침
        - ![img_3.png](img_3.png)
- prepareContext 함수
    - postProcessApplicationContext함수
        - context의 beanFactory에서 setConversionService을 한다??
            - getBeanFactory -> GenericApplicationContext의 beanFactory (DefaultListableBeanFactory)
- load 함수
    - BeanDefinitionLoader 생성 -> createBeanDefinitionLoader 함수
    - source 배열에 있는 클래스를 annotationReader 필드애 등록 (source: [NewTilApplication])
- callRunners 함수
    - ApplicationRunner 클래스타입의 빈들을 ruuners에 추가 (ApplicationRunner 안만들었으면 아무것도 들어가지 않음)
    - CommandLineRunner 클래스타입의 빈들을 ruuners에 추가 (CommandLineRunner 안만들었으면 아무것도 들어가지 않음)
    - runners에 있는 원소들 실행