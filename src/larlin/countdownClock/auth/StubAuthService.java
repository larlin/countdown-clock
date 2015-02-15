package larlin.countdownClock.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Stub Authentication Service to go with our Stub Authenticator.
 * @author larlin
 *
 */
public class StubAuthService extends Service {

    private StubAuth authenticator;
    @Override
    public void onCreate() {
        authenticator = new StubAuth(this);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }


}
