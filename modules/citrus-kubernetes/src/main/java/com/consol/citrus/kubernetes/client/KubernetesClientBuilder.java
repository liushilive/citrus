/*
 * Copyright 2006-2016 the original author or authors.
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

package com.consol.citrus.kubernetes.client;

import com.consol.citrus.endpoint.AbstractEndpointBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;

/**
 * @author Christoph Deppisch
 * @since 2.7
 */
public class KubernetesClientBuilder extends AbstractEndpointBuilder<KubernetesClient> {

    /** Endpoint target */
    private KubernetesClient endpoint = new KubernetesClient();
    private ConfigBuilder config = new ConfigBuilder();

    @Override
    public KubernetesClient build() {
        endpoint.getEndpointConfiguration().setKubernetesClientConfig(config.build());
        return super.build();
    }

    @Override
    protected KubernetesClient getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the client url.
     * @param url
     * @return
     */
    public KubernetesClientBuilder url(String url) {
        config.withMasterUrl(url);
        return this;
    }

    /**
     * Sets the client version.
     * @param version
     * @return
     */
    public KubernetesClientBuilder version(String version) {
        config.withApiVersion(version);
        return this;
    }

    /**
     * Sets the client username.
     * @param username
     * @return
     */
    public KubernetesClientBuilder username(String username) {
        config.withUsername(username);
        return this;
    }

    /**
     * Sets the client password.
     * @param password
     * @return
     */
    public KubernetesClientBuilder password(String password) {
        config.withPassword(password);
        return this;
    }

    /**
     * Sets the client email.
     * @param email
     * @return
     */
    public KubernetesClientBuilder namespace(String email) {
        config.withNamespace(email);
        return this;
    }

    /**
     * Sets the client certFile.
     * @param certFile
     * @return
     */
    public KubernetesClientBuilder certFile(String certFile) {
        config.withCaCertFile(certFile);
        return this;
    }
}
