package se.BTH.webservice.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.annotation.Id;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Task {
    @Id
    private String id;
    private String name;
    private Integer priority;
    private Integer storyPoints;
    List<SubTask> subTasks;

    public int findSubTaskIndex(String subtaskid) {
        int index = -1;
        for (SubTask temp : this.subTasks) {
            index++;
            if (temp.getId().equals(subtaskid))
                return index;
        }
        return index;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
