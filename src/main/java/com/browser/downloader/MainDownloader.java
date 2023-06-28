package com.browser.downloader;

import com.browser.Application.Main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainDownloader {

    private List<Downloader> MissionList = new ArrayList<>();
    public MainDownloader(){

    }

    private String getNameFromURL(String urlString) throws Exception{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String disposition = conn.getHeaderField("Content-Disposition");
        String fileName = null;
        if (disposition != null && disposition.indexOf("filename=") != -1) {
            fileName = disposition.substring(disposition.indexOf("filename=") + 9);
        } else {
            fileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
        }
        return fileName;
    }
    public void addDownloadTask(String url) throws Exception {

        String fileName = getNameFromURL(url);
        Downloader newMission = new Downloader(url,fileName);

        MissionList.add(newMission);
        newMission.start();
    }

//    public void start(){
//        myDownloadManager.start();
//        //DownloadMission tmpMission = myDownloadManager.getMission(missionID);
//    }
//
//    public void pause(int missionID){
//        myDownloadManager.pauseMission(missionID);
//    }
//
//    public void cancel(int missionID){
//        myDownloadManager.cancelMission(missionID);
//    }

    public Downloader getMission(String url){
        for( Downloader mission : MissionList) {
            if(mission.getUrl() == url){
                return mission;
            }
        }
        return null;
    }

    public List<Downloader> getMissionList(){
        return MissionList;
    }

    public int getTotal() {
        return MissionList.size();
    }
}
