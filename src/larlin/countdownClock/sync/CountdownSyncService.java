package larlin.countdownClock.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CountdownSyncService extends Service {
    // Storage for an instance of the sync adapter
    private static CountdownSyncAdapter syncAdapter = null;
    // Lock for syncAdapter for thread safety...
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (syncAdapterLock) {
            if (syncAdapter == null) {
                syncAdapter = new CountdownSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return syncAdapter.getSyncAdapterBinder();
    }


}
