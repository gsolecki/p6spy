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
package com.p6spy.engine.leak;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.proxy.Delegate;

import java.lang.reflect.Method;

class P6LeakStatementCloseDelegate implements Delegate {
  private final StatementInformation statementInformation;

  public P6LeakStatementCloseDelegate(final StatementInformation statementInformation) {
    this.statementInformation = statementInformation;
  }

  @Override
  public Object invoke(Object target, Method method, Object[] args) throws Throwable {
    P6Objects.close(statementInformation);
    return method.invoke(target, args);
  }
}
