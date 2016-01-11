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
import static java.util.stream.Collectors.toList;

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
    public List<IServiceInfo> discover(String serviceName, Map<String, Object> attributes, Map<String,Object> options) throws ServiceNotFoundException {
        IFn fn = Clojure.var("crow.discovery", "discover");

        Map<Keyword,Object> serviceAttr = Utils.toKeywordMap(attributes);
        Map<Keyword,Object> serviceOpt = Utils.toKeywordMap(options);

        try {
            @SuppressWarnings("unchecked")
            List<Map<Keyword,Object>> result = (List<Map<Keyword,Object>>) fn.invoke(cljServiceFinder, serviceName, serviceAttr, serviceOpt);
            return result.stream().map(m -> {
                String address = (String) m.get(Keyword.intern("address"));
                Number portVal = (Number) m.get(Keyword.intern("port"));
                Long port = Optional.ofNullable(portVal).map(Number::longValue).orElse(null);
                String name = (String) m.get(Keyword.intern("name"));

                @SuppressWarnings("unchecked")
                Map<Keyword,Object> attr = (Map<Keyword,Object>) m.get(Keyword.intern("attributes"));
                Map<String,Object> convertedAttr = Utils.toNameMap(attr);

                return new ServiceInfo(address, port.intValue(), name, Optional.ofNullable(convertedAttr));
            }).collect(toList());
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
