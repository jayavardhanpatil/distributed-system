package org.distributed_system.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class LeaderNode implements Watcher{

    private static final String ZOOKEEPER_HOST = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        LeaderNode leaderNode = new LeaderNode();
        leaderNode.connectZookeeper();
        leaderNode.run();
        leaderNode.close();
        System.out.println("disconnected from zookeeper, Exiting application");
    }

    public void run() throws InterruptedException {
        synchronized (zooKeeper){
            zooKeeper.wait();
        }
    }

    public void connectZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_HOST, SESSION_TIMEOUT, this);
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()){
            case None:
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                    System.out.println("Successfully Connected");
                }else{
                    synchronized (zooKeeper){
                        System.out.println("Disconnected");
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
