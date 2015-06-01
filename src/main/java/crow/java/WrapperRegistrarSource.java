package crow.java;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Tsutomu Yano
 */
public class WrapperRegistrarSource implements IRegistrarSource {
    public static WrapperRegistrarSource urlRegistrarSource(String sourceUrl) {
        IFn requireFn = Clojure.var("clojure.core", "require");
        requireFn.invoke(Clojure.read("crow.registrar-source"));
        
        IFn fn = Clojure.var("crow.registrar-source", "url-registrar-source");
        crow.registrar_source.RegistrarSource source = (crow.registrar_source.RegistrarSource) fn.invoke(sourceUrl);
        return new WrapperRegistrarSource(source);
    }

    public static WrapperRegistrarSource staticRegistrarSource(String address, int port) {
        IFn requireFn = Clojure.var("clojure.core", "require");
        requireFn.invoke(Clojure.read("crow.registrar-source"));

        IFn fn = Clojure.var("crow.registrar-source", "static-registrar-source");
        crow.registrar_source.RegistrarSource source = (crow.registrar_source.RegistrarSource) fn.invoke(address, Long.valueOf(port));
        return new WrapperRegistrarSource(source);
    }
    
    private final crow.registrar_source.RegistrarSource cljRegistrarSource;

    public WrapperRegistrarSource(crow.registrar_source.RegistrarSource cljRegistrarSource) {
        this.cljRegistrarSource = cljRegistrarSource;
    }
    
    @Override
    public List<IRegistrarInfo> getRegistrarInfo() {
        List<List<Object>> registrars = (List<List<Object>>) cljRegistrarSource.registrars();
        return registrars.stream().map((List<Object> registrar) -> {
            assert registrar != null;
            assert registrar.size() >= 2;
            String address = (String) registrar.get(0);
            Number port = (Number) registrar.get(1);
            return new RegistrarInfo(address, port.intValue());
        }).collect(Collectors.toList());
    }
    
    public crow.registrar_source.RegistrarSource getClojureRegistrarSource() {
        return this.cljRegistrarSource;
    }
}
