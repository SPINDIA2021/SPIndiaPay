package com.satmatgroup.newspindia.data;




import com.satmatgroup.newspindia.network.NetworkCall;
import com.satmatgroup.newspindia.network.ServiceCallBack;

import okhttp3.RequestBody;
import retrofit2.http.Part;


public interface DataSource {

    void getCategory(ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

    void saveRetry(String txnid, ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

}

