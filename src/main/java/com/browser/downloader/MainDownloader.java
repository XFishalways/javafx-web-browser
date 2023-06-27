package com.browser.downloader;

import com.browser.Application.Main;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainDownloader {

    private DownloadManager myDownloadManager;
    private List<Integer> MissionIdList = new ArrayList<>();
    public MainDownloader(){
        myDownloadManager =  DownloadManager.getInstance();
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
    public void addDownloadTask(String url,String filePath) throws Exception {

        String fileName = getNameFromURL(url);
        DownloadMission newMission = new DownloadMission(url,filePath,fileName);

        myDownloadManager.addMission(newMission);
        myDownloadManager.start();
        MissionIdList.add(newMission.getMissionID());
    }

    public void addDownloadTask(String url) throws Exception {

        String fileName = getNameFromURL(url);
        DownloadMission newMission = new DownloadMission(url, Main.DOWNLOADS, fileName);

        myDownloadManager.addMission(newMission);
        myDownloadManager.start();
        MissionIdList.add(newMission.getMissionID());
    }

    public void start(){
        myDownloadManager.start();
        //DownloadMission tmpMission = myDownloadManager.getMission(missionID);
    }

    public void pause(int missionID){
        myDownloadManager.pauseMission(missionID);
    }

    public void cancel(int missionID){
        myDownloadManager.cancelMission(missionID);
    }

    public DownloadMission getMission(int missionID){
        return myDownloadManager.getMission(missionID);
    }

    public List<Integer> getIdList(){
        return MissionIdList;
    }

    public int getTotal() {
        return MissionIdList.size();
    }
}
