package com.donkeyking.service.service;

import org.springframework.web.servlet.ModelAndView;

public interface MainService {
    ModelAndView displayMainPage(String id, String uuid);
    ModelAndView processRoomSelection(String sid, String uuid);
    ModelAndView displaySelectedRoom(String sid, String uuid);
    ModelAndView processRoomExit(String sid, String uuid);
    ModelAndView requestRandomRoomNumber(String uuid);
}
