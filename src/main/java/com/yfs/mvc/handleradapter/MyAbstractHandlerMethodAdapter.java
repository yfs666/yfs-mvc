package com.yfs.mvc.handleradapter;

import org.springframework.core.Ordered;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.WebContentGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class MyAbstractHandlerMethodAdapter extends WebContentGenerator implements HandlerAdapter, Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE;


    public MyAbstractHandlerMethodAdapter() {
        // no restriction of HTTP methods by default
        super(false);
    }


    /**
     * Specify the order value for this HandlerAdapter bean.
     * <p>Default value is {@code Integer.MAX_VALUE}, meaning that it's non-ordered.
     * @see org.springframework.core.Ordered#getOrder()
     */
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }


    /**
     * This implementation expects the handler to be an {@link HandlerMethod}.
     * @param handler the handler instance to check
     * @return whether or not this adapter can adapt the given handler
     */
    @Override
    public final boolean supports(Object handler) {
        //TODO for yfs
        Boolean isHandlerMethod = handler instanceof HandlerMethod;
        boolean isSupportsInternal = supportsInternal((HandlerMethod) handler);
        return isHandlerMethod && isSupportsInternal;
    }

    /**
     * Given a handler method, return whether or not this adapter can support it.
     * @param handlerMethod the handler method to check
     * @return whether or not this adapter can adapt the given method
     */
    protected abstract boolean supportsInternal(HandlerMethod handlerMethod);

    /**
     * This implementation expects the handler to be an {@link HandlerMethod}.
     */
    @Override
    public final ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        return handleInternal(request, response, (HandlerMethod) handler);
    }

    /**
     * Use the given handler method to handle the request.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handlerMethod handler method to use. This object must have previously been passed to the
     * {@link #supportsInternal(HandlerMethod)} this interface, which must have returned {@code true}.
     * @return ModelAndView object with the name of the view and the required model data,
     * or {@code null} if the request has been handled directly
     * @throws Exception in case of errors
     */
    protected abstract ModelAndView handleInternal(HttpServletRequest request,
                                                   HttpServletResponse response, HandlerMethod handlerMethod) throws Exception;

    /**
     * This implementation expects the handler to be an {@link HandlerMethod}.
     */
    @Override
    public final long getLastModified(HttpServletRequest request, Object handler) {
        return getLastModifiedInternal(request, (HandlerMethod) handler);
    }

    /**
     * Same contract as for {@link javax.servlet.http.HttpServlet#getLastModified(HttpServletRequest)}.
     * @param request current HTTP request
     * @param handlerMethod handler method to use
     * @return the lastModified value for the given handler
     */
    protected abstract long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod);
}
