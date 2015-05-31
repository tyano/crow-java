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

import classprocessor.annotation.GenerateClass;

/**
 *
 * @author Tsutomu Yano
 */
@GenerateClass
public interface IRegistrarInfo {
    String getAddress();
    int getPort();
}
