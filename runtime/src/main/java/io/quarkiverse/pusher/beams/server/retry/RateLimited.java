package io.quarkiverse.pusher.beams.server.retry;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

@Target({ TYPE, METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@InterceptorBinding
public @interface RateLimited {

}
