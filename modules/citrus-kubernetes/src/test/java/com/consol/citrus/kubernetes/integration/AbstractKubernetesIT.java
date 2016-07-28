/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.kubernetes.integration;

import com.consol.citrus.testng.AbstractTestNGCitrusTest;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IHookCallBack;
import org.testng.ITestResult;

import java.util.concurrent.*;

/**
 * @author Christoph Deppisch
 * @since 2.7
 */
public class AbstractKubernetesIT extends AbstractTestNGCitrusTest {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(AbstractKubernetesIT.class);

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        try {
            Future<Boolean> future = Executors.newSingleThreadExecutor().submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    KubernetesClient kubernetesClient = new DefaultKubernetesClient();
                    kubernetesClient.pods().list();
                    return true;
                }
            });

            future.get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("Skipping Kubernetes test execution as no proper Kubernetes environment is available on host system!");
            return;
        }

        super.run(callBack, testResult);
    }
}
