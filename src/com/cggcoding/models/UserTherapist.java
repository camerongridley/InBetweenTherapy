package com.cggcoding.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgrid_000 on 8/8/2015.
 */
public class UserTherapist extends User{
    private int userID;
    private String userName;
    private String email;
    private List<TreatmentPlan> therapistTxPlans;
    private Map<Integer, UserClient> clientMap;

    public UserTherapist(int userID, String email){
        super(userID, email);
        this.therapistTxPlans = new ArrayList<>();
        this.clientMap = new HashMap<>();
    }

}
