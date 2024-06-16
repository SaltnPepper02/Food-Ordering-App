package com.project.mdpeats.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;

import com.project.mdpeats.Models.FSO;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.Models.Student;

import java.net.NetworkInterface;

public class Common { // this class is some common variables used
        //curent student account
        public static Student currentStudent;
        //curent food stall owner account
        public static FSO currentFSO;
        //current order request
        public static OrderRequest currentOrderRequest;

        public static final String UPDATE = "Update";

        public static final String DELETE = "Delete";
        // method to check if the device is connected to the internet or not
        public static boolean isConnectedToInternet(@NonNull Context context) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                if (connectivityManager != null) {
                        // For devices with Android M and above
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Network activeNetwork = connectivityManager.getActiveNetwork();
                                if (activeNetwork != null) {
                                        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                                        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
                                }
                        } else {
                                // For devices with Android Lollipop and below
                                NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
                                if (networkInfos != null) {
                                        for (NetworkInfo networkInfo : networkInfos) {
                                                if (networkInfo != null && networkInfo.isConnected()) {
                                                        return true;
                                                }
                                        }
                                }
                        }
                }

                return false;
        }

        // status conversion for order status
        public static String convertToStatus(String status) {
                if (status.equals("0")){
                        return "In Kitchen";
                }
                else if (status.equals("1")){
                        return "Ready for Pick Up";
                }
                else return "Order Picked Up";

        }

}
