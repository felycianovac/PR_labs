package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import raft.RaftNode;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableJpaRepositories(basePackages ={ "product", "specification", "product_specification"})
@EntityScan(basePackages = {"product","specification", "product_specification"})

@ComponentScan(basePackages = {"product", "specification", "raft","product_specification"})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        try {
            InetSocketAddress node1 = new InetSocketAddress("laboratory_2-web-server-1-1", 3000);
            InetSocketAddress node2 = new InetSocketAddress("laboratory_2-web-server-2-1", 3001);
            InetSocketAddress node3 = new InetSocketAddress("laboratory_2-web-server-3-1", 3002);

            List<InetSocketAddress> nodes = Arrays.asList(node1, node2, node3);

            int udpPort = Integer.parseInt(System.getenv("RAFT_UDP_PORT"));
            int nodeId = udpPort - 3000;

            RaftNode raftNode = new RaftNode(nodeId, udpPort, nodes);
            raftNode.startRaftNode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
