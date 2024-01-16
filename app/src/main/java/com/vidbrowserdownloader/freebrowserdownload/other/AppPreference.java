package com.vidbrowserdownloader.freebrowserdownload.other;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vidbrowserdownloader.freebrowserdownload.Model.VideoModel.VideoModel;

public class AppPreference {

    Gson gson = new Gson();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sEditor;
    Context context;

    public AppPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Gallery", Context.MODE_PRIVATE);
    }

    public void open_editor() {
        sEditor = sharedPreferences.edit();
    }

    ArrayList<VideoModel> VideoModelList = new ArrayList<>();

    public void setVideoModelItem(VideoModel VideoModel) {
        open_editor();
        VideoModelList = getVideoModelList();
        VideoModelList.add(0, VideoModel);
        sEditor.putString("VideoModel", gson.toJson(VideoModelList));
        sEditor.commit();
    }

    public void setRecentMap(Map<String, VideoModel> recentMap) {
        open_editor();
        sEditor.putString("RecentMap", gson.toJson(recentMap));
        sEditor.commit();
    }

    public Map<String, VideoModel> getRecentMap() {
        Map<String, VideoModel> map = null;
        try {
            String json = sharedPreferences.getString("RecentMap", "");

            map = gson.fromJson(json, new TypeToken<Map<String, VideoModel>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map == null ? new HashMap<>() : map;
    }

    public void removeRecentlyPlayedVideo(String key) {
//        ArrayList<VideoModel> recentlyPlayedVideos = getVideoModelList();
//        recentlyPlayedVideos.remove(videoModel);
//
//        setVideoModelItem(videoModel);

        open_editor();
        sEditor.remove(key);
        sEditor.apply();
    }

    public ArrayList<VideoModel> getVideoModelList() {
        String json = sharedPreferences.getString("VideoModel", "");
        ArrayList<VideoModel> list = gson.fromJson(json, new TypeToken<ArrayList<VideoModel>>() {
        }.getType());

        return list == null ? new ArrayList<>() : list;
    }

    public void setVideoModelList(ArrayList<VideoModel> newVideoModelList) {
        open_editor();
        sEditor.putString("VideoModel", gson.toJson(newVideoModelList));
        sEditor.commit();
    }

    public void clearVideoModelList() {
        open_editor();
        sEditor.remove("RecentMap");
        sEditor.commit();
    }

    public void removeAll() {
        open_editor();
        sEditor.clear();
        sEditor.commit();
    }

//   ArrayList<Order> orderList = new ArrayList<>();
//
//    public void setOrderListItem(Order order) {
//        open_editor();
//        orderList = getOrderList();
//        orderList.add(0, order);
//        sEditor.putString("Order", gson.toJson(orderList));
//        sEditor.commit();
//    }
//
//    public ArrayList<Order> getOrderList() {
//        String json = sharedPreferences.getString("Order", "");
//        ArrayList<Order> list = gson.fromJson(json, new TypeToken<List<Order>>() {
//        }.getType());
//
//        return list == null ? new ArrayList<>() : list;
//    }


//    public void setToken(String token) {
//        // TODO Auto-generated method stub
//        open_editor();
//        sEditor.putString("token", token);
//        sEditor.commit();
//    }
//
//    public String getToken() {
//        // TODO Auto-generated method stub
//        return sharedPreferences.getString("token", "");
//    }
//
//    public void setFcm(String fcm) {
//        // TODO Auto-generated method stub
//        open_editor();
//        sEditor.putString("fcm", fcm);
//        sEditor.commit();
//    }
//
//    public String getFcm() {
//        // TODO Auto-generated method stub
//        return sharedPreferences.getString("fcm", "");
//    }

//    //service,repair,In./Un.
//    public void setServiceDetail(String serviceDetail) {
//        // TODO Auto-generated method stub
//        open_editor();
//        sEditor.putString("serviceDetail", serviceDetail);
//        sEditor.commit();
//    }
//
//    public String getServiceDetail() {
//        // TODO Auto-generated method stub
//        return sharedPreferences.getString("serviceDetail", "");
//    }
//
//    //user selected radio address
//    public void setUserAddress(UserAddress userAddress) {
//        // TODO Auto-generated method stub
//        open_editor();
//        String ss = gson.toJson(userAddress);
//        sEditor.putString("userAddress", ss);
//        sEditor.commit();
//    }
//
//    public UserAddress getUserAddress() {
//        // TODO Auto-generated method stub
//        UserAddress userAddress = new UserAddress();
//        userAddress = gson.fromJson(sharedPreferences.getString("userAddress", ""), UserAddress.class);
//        if (userAddress == null)
//            return new UserAddress();
//        return userAddress;
//    }
//

//
//
//    /*public void setServiceList(List<Service> data) {
//        // TODO Auto-generated method stub
//        open_editor();
//        Gson gson = new Gson();
//        Log.i("ServiceList: ", "" + gson.toJson(data));
//        sEditor.putString("ServiceList", gson.toJson(data));
//        sEditor.commit();
//    }
//
//    public List<Service> getServiceList() {
//        // TODO Auto-generated method stub
//        List<Service> ServiceList = new ArrayList<Service>();
//
//        String json = sharedPreferences.getString("ServiceList", "" + new ArrayList<>());
//        Gson gson = new Gson();
//        Service[] ServiceArray = gson.fromJson(json, Service[].class);
//
//        ServiceList = Arrays.asList(ServiceArray);
//        ServiceList = new ArrayList<Service>(ServiceList);
//
//        return ServiceList;
//    }*/
//
//    public void setSelectedService(Service selectedService) {
//        // TODO Auto-generated method stub
//        open_editor();
//        String ss = gson.toJson(selectedService);
//        sEditor.putString("selectedService", ss);
//        sEditor.commit();
//    }
//
//    public Service getSelectedService() {
//        // TODO Auto-generated method stub
//        Service selectedService = new Service();
//        selectedService = gson.fromJson(sharedPreferences.getString("selectedService", ""), Service.class);
//        if (selectedService == null)
//            return new Service();
//        return selectedService;
//    }
//
//
//    public void setServiceRequestList(List<ServiceRequest> data) {
//        // TODO Auto-generated method stub
//        open_editor();
//        Gson gson = new Gson();
//        Log.i("ServiceRequest: ", "" + gson.toJson(data));
//        sEditor.putString("ServiceRequestList", gson.toJson(data));
//        sEditor.commit();
//    }
//
//    public List<ServiceRequest> getServiceRequestList() {
//        // TODO Auto-generated method stub
//        List<ServiceRequest> ServiceRequestList = new ArrayList<ServiceRequest>();
//
//        String json = sharedPreferences.getString("ServiceRequestList", "" + new ArrayList<>());
//        Gson gson = new Gson();
//        ServiceRequest[] ServiceRequestArray = gson.fromJson(json, ServiceRequest[].class);
//
//        ServiceRequestList = Arrays.asList(ServiceRequestArray);
//        ServiceRequestList = new ArrayList<ServiceRequest>(ServiceRequestList);
//
//        return ServiceRequestList;
//    }
//
//
//    public void setNotificationList(List<Notify> data) {
//        // TODO Auto-generated method stub
//        open_editor();
//        Gson gson = new Gson();
//        Log.i("NotificationList: ", "" + gson.toJson(data));
//        sEditor.putString("NotificationList", gson.toJson(data));
//        sEditor.commit();
//    }
//
//    public ArrayList<Notify> getNotificationListList() {
//        // TODO Auto-generated method stub
//        List<Notify> NotificationList = new ArrayList<Notify>();
//
//        String json = sharedPreferences.getString("NotificationList", "" + new ArrayList<>());
//        Gson gson = new Gson();
//        Notify[] NotifyArray = gson.fromJson(json, Notify[].class);
//
//        NotificationList = Arrays.asList(NotifyArray);
//        ArrayList<Notify> notifyArrayList = new ArrayList<Notify>(NotificationList);
//
//        return notifyArrayList;
//   }
//

}
