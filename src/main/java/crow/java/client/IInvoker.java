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
package crow.java.client;

import crow.java.IServiceFinder;
import crow.java.IServiceInfo;
import crow.java.ServiceNotFoundException;
import java.util.Map;

/**
 *
 * @author Tsutomu Yano
 */
public interface IInvoker {
    <R> R withService(Class<R> clazz, IServiceInfo serviceInfo, String namespace, String fnName, Object... args);
    <R> R withFinder(Class<R> clazz, IServiceFinder serviceFinder, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException;
    <R> R invoke(Class<R> clazz, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException;

    default <R> R invokeNs(Class<R> clazz, String serviceName, String namespace, String fnName, Object... args) throws ServiceNotFoundException {
        return invoke(clazz, serviceName, null, namespace, fnName, args);
    }

    default <R> R invokeSimple(Class<R> clazz, String serviceName, String fnName, Object... args) throws ServiceNotFoundException {
        return invoke(clazz, serviceName, null, serviceName, fnName, args);
    }
}
