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
import clojure.lang.Keyword;
import crow.java.IServiceFinder;
import crow.java.IServiceInfo;
import crow.java.ServiceNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tsutomu Yano
 */
public class Invoker implements IInvoker {
    private static final IFn invokeFn = Clojure.var("crow.remote", "invoke");
    
    private final IServiceFinder finder;

    public Invoker(IServiceFinder finder) {
        if(finder == null) throw new IllegalArgumentException("'finder' must not be null.");
        this.finder = finder;
    }
    
    private <R> R remoteCall(Class<R> clazz, Map<Keyword,Object> serviceMap, String namespace, String fnName, Object... args) {
        switch(args.length) {
            case 0:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName));
            case 1:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0]));
            case 2:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1]));
            case 3:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2]));
            case 4:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3]));
            case 5:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4]));
            case 6:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5]));
            case 7:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6]));
            case 8:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]));
            case 9:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]));
            case 10:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]));
            case 11:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10]));
            case 12:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11]));
            case 13:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12]));
            case 14:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13]));
            case 15:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14]));
            case 16:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15]));
            case 17:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16]));
            case 18:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17]));
            case 19:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18]));
            case 20:
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19]));
            default:
                Object[] rest = Arrays.copyOfRange(args, 20, args.length);
                return clazz.cast(invokeFn.invoke(serviceMap, namespace, fnName, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19], rest));
        }
    }    
    
    private Map<Keyword,Object> toServiceMap(IServiceInfo info) {
        Map<Keyword,Object> service = new HashMap<>();
        service.put(Keyword.intern(null, "address"), info.getAddress());
        service.put(Keyword.intern(null, "port"), (long) info.getPort());
        return service;
    }

    @Override
    public <R> R withService(Class<R> clazz, IServiceInfo serviceInfo, String namespace, String fnName, Object... args) {
        return remoteCall(clazz, toServiceMap(serviceInfo), namespace, fnName, args);
    }
    
    @Override
    public <R> R withFinder(Class<R> clazz, IServiceFinder serviceFinder, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException {
        List<IServiceInfo> services = serviceFinder.discover(serviceName, attributes);
        IServiceInfo serviceInfo = services.get(0);
        
        return remoteCall(clazz, toServiceMap(serviceInfo), namespace, fnName, args);
    }

    @Override
    public <R> R invoke(Class<R> clazz, String serviceName, Map<String,Object> attributes, String namespace, String fnName, Object... args) throws ServiceNotFoundException {
        return withFinder(clazz, finder, serviceName, attributes, namespace, fnName, args);
    }
}
