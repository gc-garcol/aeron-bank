package gc.garcol.common.core;

import io.aeron.cluster.ClusterTool;

import java.io.File;

/**
 * Trigger command in {@link io.aeron.cluster.ClusterTool}
 */
public class ClusterToolDispatcher {

    private final String clusterDirPath;

    public ClusterToolDispatcher(String clusterDir) {
        this.clusterDirPath = clusterDir;
    }

    public void triggerSnapshot() {
        ClusterTool.snapshot(new File(clusterDirPath), System.out);
    }

}
