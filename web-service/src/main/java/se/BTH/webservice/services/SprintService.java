package se.BTH.webservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.BTH.webservice.models.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SprintService {
    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    public List<List<Map<Object, Object>>> getCanvasjsDataList(String sprintid) {
        Sprint sprint = restTemplate.getForObject("http://Sprintmanag-SERVICE/getsprint/{sprintid}", Sprint.class);
        Map<Object, Object> map = null;
        List<List<Map<Object, Object>>> list = new ArrayList<List<Map<Object, Object>>>();
        List<Map<Object, Object>> dataPoints1 = new ArrayList<>();
        List<Map<Object, Object>> dataPoints2 = new ArrayList<Map<Object, Object>>();
        List<Double> aD = sprint.Actual_hours_today_sum();
        List<Double> pD = new ArrayList<>();
        for (int i = 0; i < sprint.getPlannedPeriod(); i++) {
            pD.add(sprint.Calculate_Planned_hours_today());
        }

        // planned todo daily
        for (int i = 0; i < sprint.getPlannedPeriod(); i++) {
            map = new HashMap<Object, Object>();
            map.put("label", "Day" + (i + 1));
            map.put("y", pD.get(i));
            dataPoints1.add(map);
        }
        //actual done daily
        for (int i = 0; i < sprint.getPlannedPeriod(); i++) {
            map = new HashMap<Object, Object>();
            map.put("label", "Day" + (i + 1));
            map.put("y", aD.get(i));
            dataPoints2.add(map);
        }
        list.add(dataPoints1);
        list.add(dataPoints2);
        return list;
    }
    public List<List<Map<Object, Object>>> getCanvasjsDataList1(String sprintid) {
        Sprint sprint = restTemplate.getForObject("http://Sprintmanag-SERVICE/getsprint/{sprintid}", Sprint.class);
        Map<Object, Object> map = null;
        List<List<Map<Object, Object>>> list = new ArrayList<List<Map<Object, Object>>>();
        List<Map<Object, Object>> dataPoints1 = new ArrayList<>();
        List<Map<Object, Object>> dataPoints2 = new ArrayList<Map<Object, Object>>();
        List<Double> aR = sprint.Calculate_actual_remaining();
        List<Double> pR = sprint.Calculate_planned_remaining();

        // planned todo daily
        for (int i = 0; i < sprint.getPlannedPeriod(); i++) {
            map = new HashMap<Object, Object>();
            map.put("label", "Day" + (i + 1));
            map.put("y", pR.get(i));
            dataPoints1.add(map);
        }
        //actual done daily
        for (int i = 0; i < sprint.getPlannedPeriod(); i++) {
            map = new HashMap<Object, Object>();
            map.put("label", "Day" + (i + 1));
            map.put("y", aR.get(i));
            dataPoints2.add(map);
        }
        list.add(dataPoints1);
        list.add(dataPoints2);
        return list;
    }
    public List<List<Map<Object, Object>>> getCanvasjsDataList2(String sprintid) {
        Sprint sprint = restTemplate.getForObject("http://Sprintmanag-SERVICE/getsprint/{sprintid}", Sprint.class);
        Map<Object, Object> map = null;
        List<List<Map<Object,Object>>> list = new ArrayList<List<Map<Object,Object>>>();
        List<Map<Object,Object>> dataPoints1 = new ArrayList<Map<Object,Object>>();

        for (User user:sprint.getTeam().getUsers()) {
            map = new HashMap<Object, Object>();
            map.put("label", user.getUsername());
            map.put("y", sprint.totalActualHoursPerUser().get(user.getUsername()).stream().mapToInt(i -> i.intValue()).sum());
            dataPoints1.add(map);
        }
        list.add(dataPoints1);

        return list;
    }


}
