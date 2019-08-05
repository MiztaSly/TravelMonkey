package challenge.two.alc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
    public static boolean checkInternetConnection(Context context){

        ConnectivityManager con_Manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = con_Manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return  true;
            } else {
                return false;
            }


    }}


