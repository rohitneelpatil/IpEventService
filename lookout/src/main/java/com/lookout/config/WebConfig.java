package com.lookout.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * @author rohit.patil MvcConfigurerAdapter (scans com.lookout.controller
 *         for @RestController mapping )
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.lookout.controller" })
public class WebConfig extends WebMvcConfigurerAdapter {

}
