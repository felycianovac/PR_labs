package Laboratory_3;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class RaftNode extends Thread{
    private final int nodeId;
    private final int port;
    private DatagramSocket socket;

    private final List<InetSocketAddress> peers;

    // Node state
    private enum State { FOLLOWER, CANDIDATE, LEADER }
    private State currentState = State.FOLLOWER;

    // Election parameters
    private long currentTerm = 0;
    private Integer votedFor = null;
    private long lastHeartbeatTime;
    private final int electionTimeoutMin = 150;
    private final int electionTimeoutMax = 300;
    private final int heartbeatInterval = 100;

    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private int votesReceived = 0;


    public RaftNode(int nodeId, int port, List<InetSocketAddress> peers) throws SocketException {
        this.nodeId = nodeId;
        this.port = port;
        this.peers = new ArrayList<>();

        for (InetSocketAddress peer : peers) {
            if (peer.getPort() != port) {
                this.peers.add(peer);
            }
        }

        this.socket = new DatagramSocket(port);
    }

    public void startRaftNode() {
        start();
        scheduler.scheduleAtFixedRate(this::checkElectionTimeout,
                randomTimeout(), randomTimeout(), TimeUnit.MILLISECONDS);

        scheduler.scheduleAtFixedRate(this::sendHeartbeats,
                heartbeatInterval, heartbeatInterval, TimeUnit.MILLISECONDS);

        scheduler.schedule(this::simulateLeaderFailure, randomFailureTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            receiveMessages();
        }
    }

    private void simulateLeaderFailure() {
        if (currentState == State.LEADER) {
            log("Leader failed. Stopping heartbeats...");
            scheduler.shutdownNow();
            this.interrupt();
        }
    }

    private int randomFailureTime() {
        return random.nextInt(10000) + 5000;
    }

    private void receiveMessages() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                socket.receive(packet);
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                processMessage(message, packet.getAddress(), packet.getPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message, InetAddress senderAddress, int senderPort) {
        String[] parts = message.split(":");
        String messageType = parts[0];

        switch (messageType) {
            case "VOTE_REQUEST":
                handleVoteRequest(
                        Long.parseLong(parts[1]),
                        Integer.parseInt(parts[2]),
                        senderAddress,
                        senderPort
                );
                break;
            case "VOTE_RESPONSE":
                handleVoteResponse(
                        Long.parseLong(parts[1]),
                        Boolean.parseBoolean(parts[2])
                );
                break;
            case "HEARTBEAT":
                handleHeartbeat(Long.parseLong(parts[1]));
                break;
        }
    }

    private void sendVoteResponse(InetAddress address, int port, long term, boolean voteGranted) {
        try {
            String response = String.format("VOTE_RESPONSE:%d:%s", term, voteGranted);
            byte[] buffer = response.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void handleHeartbeat(long leaderTerm) {
        if (leaderTerm >= currentTerm) {
            currentState = State.FOLLOWER;
            currentTerm = leaderTerm;
            lastHeartbeatTime = System.currentTimeMillis();
            log("Received heartbeat. Stepping down to Follower.");
        }
    }


    private void handleVoteRequest(long term, int candidateId, InetAddress senderAddress, int senderPort) {
        boolean voteGranted = false;

        if (term > currentTerm) {
            currentTerm = term;
            currentState = State.FOLLOWER;
            votedFor = null;
            lastHeartbeatTime = System.currentTimeMillis();
        }

        if (term == currentTerm && (votedFor == null || votedFor == candidateId)) {
            voteGranted = true;
            votedFor = candidateId;
            currentState = State.FOLLOWER;
            lastHeartbeatTime = System.currentTimeMillis();
        }

        sendVoteResponse(senderAddress, senderPort, term, voteGranted);
        log("Received vote request from Node " + candidateId + " (Term " + term + "). Vote granted: " + voteGranted);
    }

    private void handleVoteResponse(long term, boolean voteGranted) {
        if (currentState == State.CANDIDATE && term == currentTerm) {
            if (voteGranted) {
                votesReceived++;
                log("Vote received. Total votes: " + votesReceived);

                if (votesReceived > peers.size() / 2) {
                    becomeLeader();
                }
            } else {
                log("Vote not granted in Term " + term);
            }
        } else if (term > currentTerm) {
            currentTerm = term;
            currentState = State.FOLLOWER;
            votedFor = null;
            lastHeartbeatTime = System.currentTimeMillis();
        }
    }

    private void startElection() {
        currentState = State.CANDIDATE;
        currentTerm++;
        votedFor = nodeId;
        votesReceived = 1;

        log("Starting election for Term " + currentTerm);

        for (InetSocketAddress peer : peers) {
            sendVoteRequest(peer);
        }
    }

    private void checkElectionTimeout() {
        long currentTime = System.currentTimeMillis();

        if (currentState != State.LEADER && currentTime - lastHeartbeatTime > randomTimeout()) {
            startElection();
        }
    }

    private void sendVoteRequest(InetSocketAddress peer) {
        try {
            String message = String.format("VOTE_REQUEST:%d:%d", currentTerm, nodeId);
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, peer.getAddress(), peer.getPort());
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void becomeLeader() {
        if (currentState != State.LEADER) {
            currentState = State.LEADER;
            log("ELECTED LEADER for Term " + currentTerm);
        }
    }

    private void sendHeartbeats() {
        if (currentState == State.LEADER) {
            for (InetSocketAddress peer : peers) {
                try {
                    String message = String.format("HEARTBEAT:%d", currentTerm);
                    byte[] buffer = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, peer.getAddress(), peer.getPort());
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int randomTimeout() {
        return electionTimeoutMin + random.nextInt(electionTimeoutMax - electionTimeoutMin);
    }

    private void log(String message) {
        System.out.println("[Node " + nodeId + " | Port " + port + " | Term " + currentTerm + " | " + currentState + "] " + message);
    }

}