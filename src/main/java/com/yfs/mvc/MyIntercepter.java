package com.yfs.mvc;

import com.yfs.mvc.handleradapter.MyRequestMappingHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyIntercepter  extends HandlerInterceptorAdapter {

    @Resource(name = "myRequestMappingHandlerAdapter")
    private MyRequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println("url = " + requestURI);
        System.out.println("MyIntercepter  preHandle >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyIntercepter  postHandle >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>start");
        if (modelAndView != null) {
            System.out.println("modelName="+modelAndView.getViewName());
            Map<String, Object> model = modelAndView.getModel();
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                System.out.println("model key = " + entry.getKey() + ", val = " + entry.getValue());
            }
        }
        super.postHandle(request, response, handler, modelAndView);
        System.out.println("MyIntercepter  postHandle >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyIntercepter  afterCompletion >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        super.afterCompletion(request, response, handler, ex);
    }

    @PostConstruct
    public void init() {
        System.out.println("add RequestResponseBodyMethodProcessorExt to HandlerMethodReturnValueHandlers...");
        // 消息转换器，在此处可以对转换器进行自定义配置
        List<HttpMessageConverter<?>> httpMessageConverters = requestMappingHandlerAdapter.getMessageConverters();
        // 添加自定义 responseBody 处理器，并放在第一位
        ResponseBodyReturnValueHandlerExt processorExt = new ResponseBodyReturnValueHandlerExt(httpMessageConverters);
        List<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>(handlerMethodReturnValueHandlers.size() + 1);
        newReturnValueHandlers.add(processorExt);
        newReturnValueHandlers.addAll(handlerMethodReturnValueHandlers);
        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}

class ResponseBodyReturnValueHandlerExt extends RequestResponseBodyMethodProcessor {
    public ResponseBodyReturnValueHandlerExt(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
        System.out.println("自定义ResponseBody处理器，在此处可以针对ResponseBody进行再封装.........................start");
        System.out.println(returnValue);
        System.out.println("自定义ResponseBody处理器，在此处可以针对ResponseBody进行再封装.........................end");
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
