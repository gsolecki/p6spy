/*
 * #%L
 * P6Spy
 * %%
 * Copyright (C) 2013 P6Spy
 * %%
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
 * #L%
 */
package com.p6spy.engine.common;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class P6Util {

    public static int parseInt(String i, int defaultValue) {
        if (i == null || i.equals("")) {
            return defaultValue;
        }
        try {
            return (Integer.parseInt(i));
        }
        catch(NumberFormatException nfe) {
            P6LogQuery.error("NumberFormatException occured parsing value "+i);
            return defaultValue;
        }
    }

    public static boolean isTrue(String s, boolean defaultValue) {
        if (s == null) {
            return defaultValue;
        }
        return(s.equals("1") || s.trim().equalsIgnoreCase("true"));
    }

  /**
   * Locates a file on the file system or on the classpath.  
   * <p>
   *   Search order - 
   *   <ol>
   *     <li>current working directory</li>
   *     <li>p6 home</li>
   *     <li>class path</li>
   *   </ol>
   * </p>
   * 
   * @param file the relative path of the file to locate
   * @return A URL to the file or null if not found
   */
  public static URL locateFile(String file) {
    File fp;
    String p6home = System.getProperty("p6.home");
    URL result = null;

    try {
      // try to find relative to current working directory first
      fp = new File(file);
      if (fp.exists()) {
        result = fp.toURI().toURL();
      }

      // next try relative to p6home
      if (result == null) {
        if (p6home != null) {
          fp = new File(p6home, file);
          if (fp.exists()) {
            result = fp.toURI().toURL();
          }
        }
      }

      // next try to load from context class loader
      if (result == null) {
        result = locateOnClassPath(file);
      }
    } catch (Exception e) {
    }

    return result;

  }

  /**
   * Locates a file on the classpath.
   * 
   * @param filename the relative path of the file to load
   * @return the URL of the file or null if not found
   */
  public static URL locateOnClassPath(String filename) {
    URL result;
    // first try to load from context class loader
    result = Thread.currentThread().getContextClassLoader().getResource(filename);

    // next try the current class loader which loaded p6spy
    if (result == null) {
      result = P6Util.class.getClassLoader().getResource(filename);
    }

    // finally try the system class loader
    if (result == null) {
      result = ClassLoader.getSystemResource(filename);
    }

    return result;
  }

    /**
     * A utility for using the current class loader (rather than the
     * system class loader) when instantiating a new class.
     * <p>
     * The idea is that the thread's current loader might have an
     * obscure notion of what your class path is (e.g. an app server) that
     * will not be captured properly by the system class loader.
     * <p>
     * taken from http://sourceforge.net/forum/message.php?msg_id=1720229
     *
     * @param name class name to load
     * @return the newly loaded class
     */
    public static Class forName(String name) throws ClassNotFoundException {
        ClassLoader ctxLoader = null;
        try {
            ctxLoader = Thread.currentThread().getContextClassLoader();
            return Class.forName(name, true, ctxLoader);

        } catch(ClassNotFoundException ex) {
            // try to fall through and use the default
            // Class.forName
            //if(ctxLoader == null) { throw ex; }
        } catch(SecurityException ex) {
        }
        return Class.forName(name);
    }

    public static String getPath(URL theURL) {
     	String file = theURL.getFile();
     	String path = null;
     	if (file != null) {
			int q = file.lastIndexOf('?');
	       	if (q != -1) {
	         path = file.substring(0, q);
			} else {
	       		path = file;
	     	}
   		}
     	return path;
     }

    
    public static Map<String, String> getPropertiesMap(Properties properties) {
      if (null == properties) {
        return null;
      }
      
      return new HashMap<String, String>((Map) properties);
    }
    
    public static List<String> parseCSVList(String csv) {
      if (csv == null || csv.isEmpty()) {
        return null;
      }
      
      return new ArrayList<String>(Arrays.asList(csv.split(",")));
    }

    public static Properties getProperties(Map<String, String> map) {
      if (map == null) {
        return null;
      }
      
      final Properties properties = new Properties();
      properties.putAll(map);
      return properties;
    }
}

