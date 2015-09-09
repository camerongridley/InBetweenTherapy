package com.cggcoding.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgrid_000 on 8/8/2015.
 */
public class UserTherapist extends User{
    private Map<Integer, UserClient> clientMap;
    private List<TreatmentIssue> defaultTreatmentIssues;

    public UserTherapist(int userID, String email){
        super(userID, email);
        this.clientMap = new HashMap<>();
        this.defaultTreatmentIssues = new ArrayList<>();
    }

}
