package com.example.conversationsdk;

import com.moxtra.sdk.MXChatCustomizer;

/**
 * Created by Administrator on 2015/9/8.
 */
public class CustomizeUISettings {
    public static void setUISettings(){
//        MXChatCustomizer.getCustomizeUIConfig().getComponentFlags().filesEnabled = true;
//        MXChatCustomizer.getCustomizeUIConfig().getComponentFlags().todoEnabled = true;
//        MXChatCustomizer.getCustomizeUIConfig().getComponentFlags().meetEnabled = true;
//        MXChatCustomizer.getCustomizeUIConfig().getComponentFlags().searchInBinderEnabled = true;
//        MXChatCustomizer.getCustomizeUIConfig().getComponentFlags().bindersSettingEnabled = true;
//
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().noteEnabled = false;            // note
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().whiteBoardEnabled = true;     //white board
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().galleryEnabled = false;        //gallery
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().cameraEnabled = false;         //camera
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().webClipEnabled = false;        //web clip
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().binderPageEnabled = true;    //binder page
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().localSDCardEnabled = true;   //from local sd
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().moreEnabled = false;           //more button
//        MXChatCustomizer.getCustomizeUIConfig().getAddFileFlags().addFileEnabled = true;       //add file button enable
//
//        MXChatCustomizer.getCustomizeUIConfig().getBinderSettingFlags().binderEmailAddressEnabled = false;

        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().telephonyEnabled = true;     //telephony
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().voiceIPEnabled = true;       //VOIP
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().videoEnabled = true;         //video
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().shareScreenEnabled = true;   //share screen
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().shareDocumentEnabled = true;  //share document
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().meetRecordingEnabled = true;  //meet recording
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().autoJoinVOIP = false;              //set this will override telephonyEnabled/voiceIPEnabled
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().inviteParticipants = true;      //invite somebody for host directly, if false, invite for attendee is disabled too.
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().inviteViaSMS = true;            //invite somebody using SMS
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().inviteForAttendee = true;      //invite for attendee
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().meetLinkEnabled = true;        //enable the meet link
        MXChatCustomizer.getCustomizeUIConfig().getMeetFlags().enableAnnotateOfPartcipants = true;  //enable attendee annotate

//        MXChatCustomizer.getCustomizeUIConfig().getShareFlags().publicLinkEnabled = false;  //public Link
//        MXChatCustomizer.getCustomizeUIConfig().getShareFlags().openInEnabled = false;  //open In
//        MXChatCustomizer.getCustomizeUIConfig().getShareFlags().saveToCameraRollsEnabled = false;  //save To Camera Rolls
    }
}
