package org.example.model;

import lombok.*;
import org.example.Enum.BotState;
import org.example.Enum.TypeOfRequest;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private long chatId;
    private BotState state;
    private String last_topic;
    private TypeOfRequest typeOfRequest;
    private List<String> history ;
}
