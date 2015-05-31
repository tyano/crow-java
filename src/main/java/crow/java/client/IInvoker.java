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

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import crow.java.IServiceFinder;
import crow.java.IServiceInfo;
import crow.java.ServiceNotFoundException;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author Tsutomu Yano
 */
public interface IInvoker {
    <R> R withService(Class<R> clazz, IServiceInfo serviceInfo, String namespace, String fnName, Object... args);
    <R> R withFinder(Class<R> clazz, IServiceFinder serviceFinder, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException;
    <R> R invoke(Class<R> clazz, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException;

    default <R> R withTimeout(int timeoutMs, Callable<R> call) {
        IFn timeoutFn = Clojure.var("crow.remote", "with-timeout-fn");
        return (R) timeoutFn.invoke(call);
    }

}