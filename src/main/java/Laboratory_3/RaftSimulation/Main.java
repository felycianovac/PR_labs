package Laboratory_3.RaftSimulation;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<InetSocketAddress> peers = Arrays.asList(
                    new InetSocketAddress("localhost", 8001),
                    new InetSocketAddress("localhost", 8002),
                    new InetSocketAddress("localhost", 8003),
                    new InetSocketAddress("localhost", 8004)
            );

            RaftNode node1 = new RaftNode(1, 8001, peers);
            RaftNode node2 = new RaftNode(2, 8002, peers);
            RaftNode node3 = new RaftNode(3, 8003, peers);
            RaftNode node4 = new RaftNode(4, 8004, peers);

            node1.startRaftNode();
            node2.startRaftNode();
            node3.startRaftNode();
            node4.startRaftNode();


            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
