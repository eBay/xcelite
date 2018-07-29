package com.ebay.xcelite.resolver;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.annotations.XlsParam;
import com.ebay.xcelite.sheet.XceliteSheet;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Resolves the following method arguments:
 * <ul>
 * <li>Annotated with {@code @XlsParam}
 * <li>of type {@link java.util.Collection Collection&lt;Object&gt;} unless annotated with {@code @XlsParam}
 * </ul>
 *
 * @author Aleksey Vasiliev
 */
public class RequestXlsBeanMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * Supports the following:
     * <ul>
     * <li>annotated with {@code @XlsParam}
     * <li>of type {@link java.util.Collection Collection&lt;Object&gt;} unless annotated with {@code @XlsParam}
     * </ul>
     */
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(XlsParam.class) && Collection.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    @Nullable
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws IOException, ServletException {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");

        Collection<Part> parts = servletRequest.getParts();
        Assert.state(parts != null, "No uploaded files");

        return parts.stream()
                .flatMap(part -> parseXls(part, parameter))
                .collect(Collectors.toList());

    }

    @SneakyThrows(IOException.class)
    private Stream<?> parseXls(Part part, MethodParameter parameter) {
        Xcelite xcelite = new Xcelite(part.getInputStream());
        XceliteSheet xceliteSheet = xcelite.getSheet(0);

        return xceliteSheet
                .getBeanReader(getBeanType(parameter))
                .read()
                .stream();
    }

    @Nullable
    private Class<?> getBeanType(MethodParameter parameter) {
        Type parameterType = parameter.getGenericParameterType();

        if (parameterType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) parameterType;
            if (type.getActualTypeArguments().length != 1) {
                throw new IllegalArgumentException("Expected single generic parameter on '" +
                        parameter.getParameterName() + "' in method " + parameter.getMethod());
            }
            return (Class<?>) type.getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException(String.format("Parameter '%s %s' in method %s has no parameterized type",
                    parameter.getParameterType(), parameter.getParameterName(), parameter.getMethod()));
        }
    }
}
