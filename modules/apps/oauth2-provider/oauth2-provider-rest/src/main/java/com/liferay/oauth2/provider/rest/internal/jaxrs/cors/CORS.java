package com.liferay.oauth2.provider.rest.internal.jaxrs.cors;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CORS {

	boolean allowCredentials() default true;

	String[] allowHeaders() default {"*"};

	String allowOrigin() default "*";

	String[] allowMethods() default {"*"};

}
