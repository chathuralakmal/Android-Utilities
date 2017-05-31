public class PingService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public PingService() {
        super();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service Started","-->");
            handler.postDelayed(runnable, 1000*60*8);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("## - Service Continuing"," - ##");
            //Do Stuff
            //pingLocation(getApplicationContext(),"B");
            handler.postDelayed(this, 1000*60*8);
        }
    };

}

/** Calling **/
startService(new Intent(this, PingService.class));
stopService(new Intent(this, PingService.class));
