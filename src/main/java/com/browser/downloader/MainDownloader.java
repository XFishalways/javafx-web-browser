package com.browser.downloader;

import com.browser.downloader.DownloadManager;
import com.browser.downloader.DownloadMission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainDownloader {

    private DownloadManager myDownloadManager;
    private List<Integer> MissionIdList = new ArrayList<>();
    public MainDownloader(){
        myDownloadManager =  DownloadManager.getInstance();
    }
    public void addDownloadTask(String url,String filePath,String fileName) throws IOException {
        DownloadMission newMission = new DownloadMission(url,filePath,fileName);
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


}
