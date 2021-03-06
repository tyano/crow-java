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

import java.util.List;
import java.util.Map;

/**
 *
 * @author Tsutomu Yano
 */
public interface IServiceFinder {
    List<IServiceInfo> discover(String serviceName, Map<String,Object> attributes, Map<String,Object> options) throws ServiceNotFoundException;
}
