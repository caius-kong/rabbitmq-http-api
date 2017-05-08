package augtek.rabbitmq.api;

import com.alibaba.fastjson.JSONObject;
import augtek.rabbitmq.config.ServerConfig;
import augtek.rabbitmq.exception.ResponseException;
import augtek.rabbitmq.req.BindingOptions;
import augtek.rabbitmq.req.ExchangeOptions;
import augtek.rabbitmq.req.QueueOptions;
import com.rabbitmq.client.MessageProperties;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import static org.junit.Assert.*;

/**
 * Created by kongyunhui on 2017/4/1.
 */
public class RabbitMQTest {
    private ServerConfig serverConfig;
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQTest.class);

    @Before
    public void setUp() throws Exception {
        serverConfig = new ServerConfig("localhost", 15672, "kong", "123");
    }

    /**
     * UserAPIs
     */
    @Test
    public void findAllUsers() {
        try {
            assertNotNull(UserAPIs.findAllUsers(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findUser() {
        try {
            assertNotNull(UserAPIs.findUser(serverConfig, "kong"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void createUser() {
        try {
            assertTrue(UserAPIs.createUser(serverConfig, "kong3", "123", "company2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteUser() {
        try {
            assertTrue(UserAPIs.deleteUser(serverConfig, "kong3"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findUserPermissions() {
        try {
            assertNotNull(UserAPIs.findUserPermissions(serverConfig, "kong"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findCurrentAuthenticatedUser() {
        try {
            assertNotNull(UserAPIs.findCurrentAuthenticatedUser(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * VhostAPIs
     */
    @Test
    public void createVhost() {
        try {
            assertTrue(VhostAPIs.createVhost(serverConfig, "/test_v5", true));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findAllVhost() {
        try {
            assertNotNull(VhostAPIs.findAllVhost(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findVhost() {
        try {
            assertNotNull(VhostAPIs.findVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteVhost() {
        try {
            assertTrue(VhostAPIs.deleteVhost(serverConfig, "test_v5"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findPermissionsByVhost() {
        try {
            assertNotNull(VhostAPIs.findPermissionsByVhost(serverConfig, "test_v5"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * PermissionAPIs
     */
    @Test
    public void findAllUserPermissions() {
        try {
            assertNotNull(PermissionAPIs.findAllUserPermissions(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void createUserPermission() {
        try {
            assertTrue(PermissionAPIs.createUserPermission(serverConfig, "test_v1", "kong", ".*", ".*", ".*"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findUserPermission() {
        try {
            assertNotNull(PermissionAPIs.findUserPermission(serverConfig, "test_v1", "kong"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteUserPermission() {
        try {
            assertTrue(PermissionAPIs.deleteUserPermission(serverConfig, "test_v4", "kong1"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }



    /**
     * ConnectionAPIs
     */
    @Test
    public void findConnections() {
        try {
            assertNotNull(ConnectionAPIs.findConnections(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findConnectionsByVhost() {
        try {
            assertNotNull(ConnectionAPIs.findConnectionsByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findConnection() {
        try {
            String encode = URLEncoder.encode("127.0.0.1:52480 -> 127.0.0.1:5672", "utf-8");
            assertNotNull(ConnectionAPIs.findConnection(serverConfig, encode));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteConnection() {
        try {
            String encode = URLEncoder.encode("127.0.0.1:52480 -> 127.0.0.1:5672", "utf-8");
            assertTrue(ConnectionAPIs.deleteConnection(serverConfig, encode));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * ChannelAPIs
     */
    @Test
    public void findChannels() {
        try {
            assertNotNull(ChannelAPIs.findChannels(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findChannel() {
        try {
            String encode = URLEncoder.encode("127.0.0.1:52482 -> 127.0.0.1:5672 (1)", "utf-8");
            assertNotNull(ChannelAPIs.findChannel(serverConfig, encode));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findChannelsByVhost() {
        try {
            assertNotNull(ChannelAPIs.findChannelsByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * ConsumerAPIs
     */
    @Test
    public void findConsumers() {
        try {
            assertNotNull(ConsumerAPIs.findConsumers(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findConsumerCountByQueue() {
        try {
            assertNotNull(ConsumerAPIs.findConsumerCountByQueue(serverConfig, "%2f", "amq.gen-cg7h7tC-EHsZzSmokGfsWQ"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findConsumersByVhost() {
        try {
            assertNotNull(ConsumerAPIs.findConsumersByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * ExchangeAPIs
     */
    @Test
    public void findExchanges() {
        try {
            assertNotNull(ExchangeAPIs.findExchanges(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findExchangesByVhost() {
        try {
            assertNotNull(ExchangeAPIs.findExchangesByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findExchange() {
        try {
            assertNotNull(ExchangeAPIs.findExchange(serverConfig, "%2f", "topic_logs"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void createExchange() {
        try {
            ExchangeOptions options = new ExchangeOptions();
            options.setAuto_delete(false);
            options.setDurable(true);
            options.setInternal(false);
            options.setArguments(new JSONObject());
            assertTrue(ExchangeAPIs.createExchange(serverConfig, "%2f", "direct_test", "direct", options));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteExchange() {
        try {
            assertTrue(ExchangeAPIs.deleteExchange(serverConfig, "%2f", "direct_test"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findBindingsByExchange() {
        try {
            assertNotNull(ExchangeAPIs.findBindingsByExchange(serverConfig, "%2f", "topic_logs", true));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void sendMessageToExchange() {
        try {
            assertNotNull(ExchangeAPIs.sendMessageToExchange(serverConfig, "%2f", "direct_test", MessageProperties.PERSISTENT_TEXT_PLAIN, "level1", "系统处于危险等级1_1", "string"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * QueueAPIs
     */
    @Test
    public void findQueues() {
        try {
            assertNotNull(QueueAPIs.findQueues(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findQueuesByVhost() {
        try {
            assertNotNull(QueueAPIs.findQueuesByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findQueue() {
        try {
            assertNotNull(QueueAPIs.findQueue(serverConfig, "%2f", "queue2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void createQueue() {
        try {
            QueueOptions options = new QueueOptions();
            options.setDurable(true);
            assertTrue(QueueAPIs.createQueue(serverConfig, "%2f", "queue2", options));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteQueue() {
        try {
            assertTrue(QueueAPIs.deleteQueue(serverConfig, "%2f", "queue2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findBindingsByQueue() {
        try {
            assertNotNull(QueueAPIs.findBindingsByQueue(serverConfig, "%2f", "amq.gen-9KoWjcUmXVDfiOk37-dFzg"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void receiveMessageFromQueue() {
        try {
            assertNotNull(QueueAPIs.receiveMessageFromQueue(serverConfig, "%2f", "queue2", 5, true, "auto", 50000));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findMessageCountByQueue() {
        try {
            assertNotEquals(-1, QueueAPIs.findMessageCountByQueue(serverConfig, "%2f", "queue2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    /**
     * BindingAPIs
     */
    @Test
    public void findBindings() {
        try {
            assertNotNull(BindingAPIs.findBindings(serverConfig));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findBindingsByVhost() {
        try {
            assertNotNull(BindingAPIs.findBindingsByVhost(serverConfig, "%2f"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findBindingsBetweenXAndQ() {
        try {
            assertNotNull(BindingAPIs.findBindingsBetweenXAndQ(serverConfig, "%2f", "topic_logs", "amq.gen-9KoWjcUmXVDfiOk37-dFzg"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void createBindingBetweenXAndQ() {
        try {
            BindingOptions options = new BindingOptions();
            options.setRouting_key("level2");
            assertNotNull(BindingAPIs.createBindingBetweenXAndQ(serverConfig, "%2f", "direct_test", "queue2", options));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void findBindingBetweenXAndQ() {
        try {
            assertNotNull(BindingAPIs.findBindingBetweenXAndQ(serverConfig, "%2f", "direct_test", "queue2", "level2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

    @Test
    public void deleteBindingBetweenXAndQ() {
        try {
            assertTrue(BindingAPIs.deleteBindingBetweenXAndQ(serverConfig, "%2f", "direct_test", "queue2", "level2"));
        } catch (ResponseException e) {
            LOG.error("error: {}", e.getMessage());
        }
    }

}
