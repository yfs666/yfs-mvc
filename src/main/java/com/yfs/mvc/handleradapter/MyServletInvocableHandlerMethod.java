package com.yfs.mvc.handleradapter;

import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.util.NestedServletException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;

public class MyServletInvocableHandlerMethod extends ServletInvocableHandlerMethod {


    private HandlerMethodReturnValueHandlerComposite returnValueHandlerList;

    private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call");

    public MyServletInvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    public MyServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }


    MyServletInvocableHandlerMethod wrapConcurrentResult(Object result) {
        return new ConcurrentResultHandlerMethod(result, new ConcurrentResultMethodParameter(result));
    }


    /**
     * A nested subclass of {@code ServletInvocableHandlerMethod} that uses a
     * simple {@link Callable} instead of the original controller as the handler in
     * order to return the fixed (concurrent) result value given to it. Effectively
     * "resumes" processing with the asynchronously produced return value.
     */
    private class ConcurrentResultHandlerMethod extends MyServletInvocableHandlerMethod {

        private final MethodParameter returnType;

        public ConcurrentResultHandlerMethod(final Object result, ConcurrentResultMethodParameter returnType) {
            super(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    if (result instanceof Exception) {
                        throw (Exception) result;
                    }
                    else if (result instanceof Throwable) {
                        throw new NestedServletException("Async processing failed", (Throwable) result);
                    }
                    return result;
                }
            }, CALLABLE_METHOD);
            setHandlerMethodReturnValueHandlers(MyServletInvocableHandlerMethod.this.returnValueHandlerList);
            this.returnType = returnType;
        }

        /**
         * Bridge to actual controller type-level annotations.
         */
        @Override
        public Class<?> getBeanType() {
            return MyServletInvocableHandlerMethod.this.getBeanType();
        }

        /**
         * Bridge to actual return value or generic type within the declared
         * async return type, e.g. Foo instead of {@code DeferredResult<Foo>}.
         */
        @Override
        public MethodParameter getReturnValueType(Object returnValue) {
            return this.returnType;
        }

        /**
         * Bridge to controller method-level annotations.
         */
        @Override
        public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
            return MyServletInvocableHandlerMethod.this.getMethodAnnotation(annotationType);
        }
    }


    /**
     * MethodParameter subclass based on the actual return value type or if
     * that's null falling back on the generic type within the declared async
     * return type, e.g. Foo instead of {@code DeferredResult<Foo>}.
     */
    private class ConcurrentResultMethodParameter extends HandlerMethodParameter {

        private final Object returnValue;

        private final ResolvableType returnType;

        public ConcurrentResultMethodParameter(Object returnValue) {
            super(-1);
            this.returnValue = returnValue;
            this.returnType = ResolvableType.forType(super.getGenericParameterType()).getGeneric(0);
        }

        @Override
        public Class<?> getParameterType() {
            return (this.returnValue != null ? this.returnValue.getClass() : this.returnType.getRawClass());
        }

        @Override
        public Type getGenericParameterType() {
            return this.returnType.getType();
        }
    }

}
