package mapreduce;

import com.google.appengine.tools.mapreduce.MapSettings;

/**
 * Created by vinaymavi on 31/12/15.
 */
public class MapReduceSettings {
    private MapReduceSettings() {
    }

    /**
     * Job Settings.
     *
     * @return {{MapSettings}}
     */
    public static MapSettings getSettings() {
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("bulk_uploader")
                .setModule("default")
                .build();
        return settings;
    }
}
