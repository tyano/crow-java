/*
 * Copyright (c) 2015 Tsutomu Yano.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tsutomu Yano - initial API and implementation and/or initial documentation
 */
package crow.java;

import java.util.Map;

/**
 *
 * @author Tsutomu Yano
 */
public class ServiceNotFoundException extends Exception {
    private final String serviceName;
    private final Map<String,Object> attributes;

    public ServiceNotFoundException(String serviceName, Map<String, Object> attributes) {
        this.serviceName = serviceName;
        this.attributes = attributes;
    }

    public ServiceNotFoundException(String serviceName, Map<String, Object> attributes, String message) {
        super(message);
        this.serviceName = serviceName;
        this.attributes = attributes;
    }

    public ServiceNotFoundException(String serviceName, Map<String, Object> attributes, String message, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.attributes = attributes;
    }

    public ServiceNotFoundException(String serviceName, Map<String, Object> attributes, Throwable cause) {
        super(cause);
        this.serviceName = serviceName;
        this.attributes = attributes;
    }

    protected ServiceNotFoundException(String serviceName, Map<String, Object> attributes, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.serviceName = serviceName;
        this.attributes = attributes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "ServiceNotFoundException{" + "serviceName=" + serviceName + ", attributes=" + attributes + '}';
    }
}
