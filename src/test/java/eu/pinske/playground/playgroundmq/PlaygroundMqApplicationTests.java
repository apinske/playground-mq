package eu.pinske.playground.playgroundmq;

import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

// docker run --rm -e LICENSE=accept -e MQ_QMGR_NAME=QM1 -p 1414:1414 -p 9443:9443 -it ibmcom/mq
@SpringBootTest
class PlaygroundMqApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        RestTemplate rest = new RestTemplate();
        rest.getInterceptors().add(new BasicAuthenticationInterceptor("admin", "passw0rd"));
        rest.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().set("ibm-mq-csrf-token", "");
            return execution.execute(request, body);
        });
        rest.postForEntity("https://localhost:9443/ibmmq/console/internal/ibmmq/qmgr/QM1/queue/DEV.QUEUE.1/messages",
                "{\"message\":\"x\"}", String.class);
        Thread.sleep(2000);
    }

    public static class DummyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] cert, String authType) {
            // everything is trusted
        }

        @Override
        public void checkServerTrusted(X509Certificate[] cert, String authType) {
            // everything is trusted
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    static {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new DummyTrustManager() }, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
