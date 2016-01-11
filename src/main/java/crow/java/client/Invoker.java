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

import clojure.core.async.impl.protocols.ReadPort;
import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;
import clojure.lang.PersistentVector;
import crow.java.IServiceFinder;
import crow.java.IServiceInfo;
import crow.java.ServiceNotFoundException;
import crow.java.Utils;
import crow.remote.CallDescriptor;
import crow.remote.ServiceDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tsutomu Yano
 */
public class Invoker implements IInvoker {
    private static final IFn invokeFn;
    private static final IFn chanFn;
    private static final IFn readChannelFn;
    private static final IFn tryCallFn;

    static {
        IFn requireFn = Clojure.var("clojure.core", "require");
        requireFn.invoke(Clojure.read("crow.remote"));
        requireFn.invoke(Clojure.read("clojure.core.async"));
        invokeFn = Clojure.var("crow.remote", "invoke");
        readChannelFn = Clojure.var("crow.remote", "<!!+");
        chanFn = Clojure.var("clojure.core.async", "chan");
        tryCallFn = Clojure.var("crow.remote", "try-call");
    }

    private final IServiceFinder finder;

    public Invoker(IServiceFinder finder) {
        if(finder == null) throw new IllegalArgumentException("'finder' must not be null.");
        this.finder = finder;
    }

    private ReadPort invoke(Long timeoutMs, Map<Keyword,Object> serviceMap, CallDescriptor callDesc) {
        ReadPort ch = (ReadPort) chanFn.invoke();
        return (ReadPort) invokeFn.invoke(ch, timeoutMs, serviceMap, callDesc);
    }

    private ReadPort invoke(Map<Keyword,Object> serviceMap, CallDescriptor callDesc) {
        return invoke(null, serviceMap, callDesc);
    }

    private <R> R remoteCallWithService(Class<R> clazz, Map<Keyword,Object> serviceMap, String namespace, String fnName, Object... args) {
        CallDescriptor callDesc = new CallDescriptor(namespace, fnName, PersistentVector.create(args));
        ReadPort ch = invoke(serviceMap, callDesc);
        return clazz.cast(readChannelFn.invoke(ch));
    }

    private Map<Keyword,Object> toServiceMap(IServiceInfo info) {
        Map<Keyword,Object> service = new HashMap<>();
        service.put(Keyword.intern("address"), info.getAddress());
        service.put(Keyword.intern("port"), (long) info.getPort());
        return service;
    }

    @Override
    public <R> R withService(Class<R> clazz, IServiceInfo serviceInfo, String namespace, String fnName, Object... args) {
        return remoteCallWithService(clazz, toServiceMap(serviceInfo), namespace, fnName, args);
    }

    @Override
    public <R> R withFinder(Class<R> clazz, IServiceFinder serviceFinder, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException {
        List<IServiceInfo> services = serviceFinder.discover(serviceName, attributes, null);
        IServiceInfo serviceInfo = services.get(0);

        return remoteCallWithService(clazz, toServiceMap(serviceInfo), namespace, fnName, args);
    }

    @Override
    public <R> R invoke(Class<R> clazz, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException {
        ReadPort ch = (ReadPort) chanFn.invoke();
        ServiceDescriptor serviceDesc = new ServiceDescriptor(serviceName, Utils.toKeywordMap(attributes));
        CallDescriptor callDesc = new CallDescriptor(namespace, fnName, PersistentVector.create(args));
        return clazz.cast(tryCallFn.invoke(ch, serviceDesc, callDesc, null));
    }
}
