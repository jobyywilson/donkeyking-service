package com.donkeyking.service.domain;

import com.donkeyking.service.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Service
public class RoomService {    
    private final Parser parser;
    // repository substitution since this is a very simple realization
    private final Set<Room> rooms = new TreeSet<>(Comparator.comparing(Room::getId));

    @Autowired
    public RoomService(final Parser parser) {
        this.parser = parser;
    }

    public Set<Room> getRooms() {
        final TreeSet<Room> defensiveCopy = new TreeSet<>(Comparator.comparing(Room::getId));
        defensiveCopy.addAll(rooms);

        return defensiveCopy;
    }

    public Boolean addRoom(final Room room) {
        return rooms.add(room);
    }

    public Room findRoomByStringId(final String sid) {
        // simple get() because of parser errors handling
        Optional<Room> roomOpty = rooms.stream().filter(r -> r.getId().equals(sid)).findAny();
        if(roomOpty.isPresent()){
            return roomOpty.get();
        }else{
            Room room = new Room(sid);
            rooms.add(room);
            return room;
        }
    }

    public String getRoomId(Room room) {
        return room.getId();
    }

    public Map<String, UserInfo> getClients(final Room room) {
        return Optional.ofNullable(room)
                .map(r -> Collections.unmodifiableMap(r.getClients()))
                .orElse(Collections.emptyMap());
    }

    public UserInfo addClient(final Room room, final String name,Map<String,String> joinInfo,String from ,final WebSocketSession session) {
        String nickName = joinInfo.get("nickName");
        String secretId = joinInfo.get("secretId");
        return room.getClients().put(name, new UserInfo(nickName,from,secretId,session));
    }

    public UserInfo removeClientByName(final Room room, final String name) {
        return room.getClients().remove(name);
    }
}
