/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.jci.compilers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * Native Eclipse compiler settings
 * 
 * @author tcurdt
 */
public final class EclipseJavaCompilerSettings extends JavaCompilerSettings {

  final private Map<String, String> defaultEclipseSettings = new HashMap<String, String>();

  public EclipseJavaCompilerSettings() {
    defaultEclipseSettings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
    defaultEclipseSettings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
    defaultEclipseSettings.put(CompilerOptions.OPTION_ReportUnusedImport, CompilerOptions.IGNORE);
    defaultEclipseSettings.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
  }

  public EclipseJavaCompilerSettings(final JavaCompilerSettings pSettings) {
    super(pSettings);

    if (pSettings instanceof EclipseJavaCompilerSettings) {
      defaultEclipseSettings.putAll(((EclipseJavaCompilerSettings) pSettings).toNativeSettings());
    }
  }

  public EclipseJavaCompilerSettings(final Map<String, String> pMap) {
    defaultEclipseSettings.putAll(pMap);
  }

  private static Map<String, String> nativeVersions = new HashMap<String, String>() {
    private static final long serialVersionUID = 1L;
    {
      put("1.1", CompilerOptions.VERSION_1_1);
      put("1.2", CompilerOptions.VERSION_1_2);
      put("1.3", CompilerOptions.VERSION_1_3);
      put("1.4", CompilerOptions.VERSION_1_4);
      put("1.5", CompilerOptions.VERSION_1_5);
      put("1.6", CompilerOptions.VERSION_1_6);
      put("1.7", CompilerOptions.VERSION_1_7);
      put("1.8", CompilerOptions.VERSION_1_8);
      put("9", CompilerOptions.VERSION_9);
      put("10", CompilerOptions.VERSION_10);
      put("11", CompilerOptions.VERSION_11);
      put("12", CompilerOptions.VERSION_12);
      put("13", CompilerOptions.VERSION_13);
      put("14", CompilerOptions.VERSION_14);
    }
  };

  private String toNativeVersion(final String pVersion) {
    final String nativeVersion = nativeVersions.get(pVersion);

    if (nativeVersion == null) {
      throw new RuntimeException("unknown version " + pVersion);
    }

    return nativeVersion;
  }

  Map<String, String> toNativeSettings() {
    final Map<String, String> map = new HashMap<String, String>(defaultEclipseSettings);

    map.put(CompilerOptions.OPTION_SuppressWarnings,
        isWarnings() ? CompilerOptions.GENERATE : CompilerOptions.DO_NOT_GENERATE);
    map.put(CompilerOptions.OPTION_ReportDeprecation,
        isDeprecations() ? CompilerOptions.GENERATE : CompilerOptions.DO_NOT_GENERATE);
    map.put(CompilerOptions.OPTION_TargetPlatform, toNativeVersion(getTargetVersion()));
    map.put(CompilerOptions.OPTION_Source, toNativeVersion(getSourceVersion()));
    map.put(CompilerOptions.OPTION_Encoding, getSourceEncoding());

    return map;
  }

  @Override
  public String toString() {
    return toNativeSettings().toString();
  }
}
