/*
 * Licensed to David Pilato (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package fr.pilato.spring.elasticsearch.it.xml.transport;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.transport.TransportAddress;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TransportClientNamespaceTest extends AbstractXmlContextModel {
    private final String[] xmlBeans = {"models/transport/transport-client-namespace/transport-client-namespace-context.xml"};

    @Override
    String[] xmlBeans() {
        return xmlBeans;
    }

    @Override
    protected void checkUseCaseSpecific(Client client) {
        assertTransportClient(client, 2);

        TransportClient tClient = (TransportClient) client;
        List<TransportAddress> addresses = tClient.transportAddresses();
        assertThat("Nodes urls must not be empty...", addresses, not(emptyCollectionOf(TransportAddress.class)));

        // Testing if we are really connected to a cluster node
        assertThat("We should be connected at least to one node.", tClient.connectedNodes(), not(emptyCollectionOf(DiscoveryNode.class)));

        DiscoveryNode node = tClient.connectedNodes().get(0);
        assertThat("We should be connected to the master node.", node.isMasterNode(), is(true));
    }
}
