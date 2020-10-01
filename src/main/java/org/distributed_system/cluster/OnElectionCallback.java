package org.distributed_system.cluster;

public interface OnElectionCallback {
    void onWorker();
    void onElectedToBeLeader();
}
