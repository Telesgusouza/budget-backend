package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.anyRequest().permitAll());

		return http.build();
	}
}


/*


agora o projeto estava da seguinte forma

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.anyRequest().permitAll())
				.httpBasic(null);

		return http.build();
	}
}

[2m2026-08-17T21:08:19.947-03:00[0;39m [32m INFO[0;39m [35m15526[0;39m [2m--- [demo] [           main] [0;39m[36m.s.b.a.l.ConditionEvaluationReportLogger[0;39m [2m:[0;39m 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
[2m2026-08-17T21:08:19.961-03:00[0;39m [31mERROR[0;39m [35m15526[0;39m [2m--- [demo] [           main] [0;39m[36mo.s.boot.SpringApplication              [0;39m [2m:[0;39m Application run failed

org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration': Unsatisfied dependency expressed through method 'setFilterChains' parameter 0: Error creating bean with name 'securityFilterChain' defined in class path resource [com/example/demo/config/SecurityConfig.class]: Failed to instantiate [org.springframework.security.web.SecurityFilterChain]: Factory method 'securityFilterChain' threw exception with message: Cannot invoke "org.springframework.security.config.Customizer.customize(Object)" because "httpBasicCustomizer" is null
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.resolveMethodArguments(AutowiredAnnotationBeanPostProcessor.java:872) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.inject(AutowiredAnnotationBeanPostProcessor.java:827) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:146) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:493) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1444) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:602) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:333) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:371) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:331) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:196) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1225) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1191) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1121) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:994) ~[spring-context-7.0.8.jar:7.0.8]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:621) ~[spring-context-7.0.8.jar:7.0.8]
	at org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) ~[spring-boot-web-server-4.1.0.jar:4.1.0]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756) ~[spring-boot-4.1.0.jar:4.1.0]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445) ~[spring-boot-4.1.0.jar:4.1.0]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:321) ~[spring-boot-4.1.0.jar:4.1.0]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365) ~[spring-boot-4.1.0.jar:4.1.0]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354) ~[spring-boot-4.1.0.jar:4.1.0]
	at com.example.demo.DemoApplication.main(DemoApplication.java:10) ~[classes/:na]
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'securityFilterChain' defined in class path resource [com/example/demo/config/SecurityConfig.class]: Failed to instantiate [org.springframework.security.web.SecurityFilterChain]: Factory method 'securityFilterChain' threw exception with message: Cannot invoke "org.springframework.security.config.Customizer.customize(Object)" because "httpBasicCustomizer" is null
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:645) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1360) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1192) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:565) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:333) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:371) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:331) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:201) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:229) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.addCandidateEntry(DefaultListableBeanFactory.java:2015) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.findAutowireCandidates(DefaultListableBeanFactory.java:1978) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveMultipleBeanCollection(DefaultListableBeanFactory.java:1870) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveMultipleBeans(DefaultListableBeanFactory.java:1840) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1718) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1658) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.resolveMethodArguments(AutowiredAnnotationBeanPostProcessor.java:864) ~[spring-beans-7.0.8.jar:7.0.8]
	... 22 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.security.web.SecurityFilterChain]: Factory method 'securityFilterChain' threw exception with message: Cannot invoke "org.springframework.security.config.Customizer.customize(Object)" because "httpBasicCustomizer" is null
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:183) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:72) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:152) ~[spring-beans-7.0.8.jar:7.0.8]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-7.0.8.jar:7.0.8]
	... 39 common frames omitted
Caused by: java.lang.NullPointerException: Cannot invoke "org.springframework.security.config.Customizer.customize(Object)" because "httpBasicCustomizer" is null
	at org.springframework.security.config.annotation.web.builders.HttpSecurity.httpBasic(HttpSecurity.java:1719) ~[spring-security-config-7.1.0.jar:7.1.0]
	at com.example.demo.config.SecurityConfig.securityFilterChain(SecurityConfig.java:18) ~[classes/:na]
	at com.example.demo.config.SecurityConfig$$SpringCGLIB$$0.CGLIB$securityFilterChain$0(<generated>) ~[classes/:na]
	at com.example.demo.config.SecurityConfig$$SpringCGLIB$$FastClass$$1.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:258) ~[spring-core-7.0.8.jar:7.0.8]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:398) ~[spring-context-7.0.8.jar:7.0.8]
	at com.example.demo.config.SecurityConfig$$SpringCGLIB$$0.securityFilterChain(<generated>) ~[classes/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:155) ~[spring-beans-7.0.8.jar:7.0.8]
	... 42 common frames omitted


*/
