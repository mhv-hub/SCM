package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Below annotation ( @SpringBootApplication ), is internally using many annotation. Below are the main ones :
// @SpringBootConfiguration
// @Configuration
// @ComponentScan
// @EnableAutoConfiguration

@SpringBootApplication
public class SmartcontactmanagerApplication extends SpringBootServletInitializer {

	// extended the class SpringBootServletInitializer
	// and overriden configure method to make the application work as a war
	// deployable.

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SmartcontactmanagerApplication.class);
	}

	// This is the main starting point of a spring boot application, execution
	// starts by calling this SpringApplication.run() method.
	// The run method does following operations :
	// 1. Identifies the type of application ( Web application, desktop application
	// etc )
	// 2. Based on the type of application, it instantiates the required context
	// classes.
	// 3. Calls the constructors of the context classes.
	// 4. Scans the classpath for the Beans which are having @Cofiguration
	// annotation and it registers them with the context.
	// 5. Inside those beans if there is any Object which is Autowired,
	// it will create object of them inside JVM ( specifically inside IOC container
	// )
	// 6. Starts the embedded tomcat and registers the context with it.
	//
	// Note : Iside Context classes there is a functionality which enable all the
	// features for the dependencies mentioned in POM.xml
	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}

}
