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

import clojure.java.api.Clojure;
import clojure.lang.ExceptionInfo;
import clojure.lang.IFn;
import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import crow.discovery.ServiceFinder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Tsutomu Yano
 */
public class WrapperServiceFinder implements IServiceFinder {
    private final ServiceFinder cljServiceFinder;

    static {
        IFn requireFn = Clojure.var("clojure.core", "require");
        requireFn.invoke(Clojure.read("crow.discovery"));
    }

    public WrapperServiceFinder(WrapperRegistrarSource registrarSource) {
        IFn fn = Clojure.var("crow.discovery", "service-finder");
        this.cljServiceFinder = (ServiceFinder) fn.invoke(registrarSource.getClojureRegistrarSource());
    }

    @Override
    public List<IServiceInfo> discover(String serviceName, Map<String, Object> attributes) throws ServiceNotFoundException {
        IFn fn = Clojure.var("crow.discovery", "discover");

        Map<Keyword,Object> serviceAttr =
                (attributes == null)
                    ? null
                    : attributes.entrySet().stream().collect(
                        Collectors.toMap(
                                e -> Keyword.intern(null, e.getKey()),
                                e -> e.getValue()));

        try {
            List<Map<Keyword,Object>> result = (List<Map<Keyword,Object>>) fn.invoke(cljServiceFinder, serviceName, serviceAttr);
            return result.stream().map(m -> {
                String address = (String) m.get(Keyword.intern(null, "address"));
                Number portVal = (Number) m.get(Keyword.intern(null, "port"));
                Long port = portVal == null ? null : portVal.longValue();
                String name = (String) m.get(Keyword.intern(null, "name"));
                Map<Keyword,Object> attr = (Map<Keyword,Object>) m.get(Keyword.intern(null, "attributes"));
                Map<String,Object> convertedAttr =
                        (attr == null)
                        ? null
                        : attr.entrySet().stream()
                            .collect(
                                Collectors.toMap(
                                        e -> e.getKey().getName(),
                                        e -> e.getValue()));

                return new ServiceInfo(address, port.intValue(), name, Optional.ofNullable(convertedAttr));
            }).collect(Collectors.toList());
        } catch(ExceptionInfo ex) {
            IPersistentMap data = ex.getData();
            Object errorType = data.valAt(Keyword.intern(null, "type"));
            if(errorType != null && errorType.equals(Keyword.intern("crow.discovery", "service-not-found"))) {
                throw new ServiceNotFoundException(serviceName, attributes, ex);
            } else {
                throw ex;
            }
        }
    }
}
