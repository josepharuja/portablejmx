/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.leedsmet.studentresearch.jmx.export;

/**
 * Exception thrown when we are unable to register an MBean,
 * for example because of a naming conflict.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class UnableToRegisterMBeanException extends MBeanExportException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7486017065447356814L;

	/**
	 * Create a new <code>UnableToRegisterMBeanException</code> with the
	 * specified error message.
	 * @param msg the detail message
	 */
	public UnableToRegisterMBeanException(String msg) {
		super(msg);
	}

	/**
	 * Create a new <code>UnableToRegisterMBeanException</code> with the
	 * specified error message and root cause.
	 * @param msg the detail message
	 * @param cause the root caus
	 */
	public UnableToRegisterMBeanException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
